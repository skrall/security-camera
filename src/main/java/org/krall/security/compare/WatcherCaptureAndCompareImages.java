package org.krall.security.compare;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.krall.security.command.CaptureTimeLapseImage;
import org.krall.security.commandline.AppOptions;
import org.krall.security.image.FXImageCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WatcherCaptureAndCompareImages implements CaptureAndCompareImages {

    private static final Logger logger = LoggerFactory.getLogger(WatcherCaptureAndCompareImages.class);

    @Override
    public void captureAndCompareImages() {
        logger.info("Starting to run capture...");
        CaptureTimeLapseImage captureTimeLapseImage = new CaptureTimeLapseImage();
        captureTimeLapseImage.run();
        watchDirectory();
        logger.info("Finished running capture.");
    }

    private void watchDirectory() {
        try {
            FXImageCompare imageCompare = new FXImageCompare();
            AppOptions options = AppOptions.getInstance();
            imageCompare.setParameters(options.getVerticalRegions(), options.getHorizontalRegions(),
                                       options.getSensitivity(), options.getStabilizer());
            imageCompare.setDebugMode(2);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                                                                 new ArrayBlockingQueue<Runnable>(1));
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = AppOptions.getInstance().getImageDirectory().toPath();
            dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            for (; ; ) {

                // wait for key to be signaled
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // This key is registered only
                    // for ENTRY_CREATE events,
                    // but an OVERFLOW event can
                    // occur regardless if events
                    // are lost or discarded.
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    // The filename is the
                    // context of the event.
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    Path image = dir.resolve(filename);
                    logger.info("Got event {} for file {}", kind, image);
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        try {
                            executor.submit(new ImageProcessor(image, imageCompare));
                        } catch (Exception ree) {
                            logger.error(
                                    "The raspistill is capturing images too fast.  Need to slow down the speed of " +
                                    "capturing the images by increasing the -m flag.");
                            Files.delete(image);
                        }
                    }
                }

                // Reset the key -- this step is critical if you want to
                // receive further watch events.  If the key is no longer valid,
                // the directory is inaccessible so exit the loop.
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (Exception e1) {
            logger.error("Error while watching directory..", e1);
        }
    }

    private class ImageProcessor extends Task<Void> {

        private Logger logger = LoggerFactory.getLogger(ImageProcessor.class);

        FXImageCompare imageCompare;

        Path imageFile;

        private ImageProcessor(Path imageFile, FXImageCompare imageCompare) {
            super();
            this.imageCompare = imageCompare;
            this.imageFile = imageFile;
        }

        @Override
        protected Void call() throws Exception {
            try {
                logger.info("Beginning work in thread...");
                Thread.sleep(200);  // Let the image finish writing....
                Image image = new Image(Files.newInputStream(imageFile));
                imageCompare.setImg2(image);
                if (imageCompare.getImg1() != null) {
                    imageCompare.compare();
                    logger.info("Detected a difference: {}", !imageCompare.match());
                    //if(!imageCompare.match()) {
                    //    imageCompare.writeImg2ToFile(String.format("%05d.png", numberOfDifferences++));
                    //}
                }
                imageCompare.setImg1(image);
                Files.delete(imageFile);
            } catch (Exception e) {
                logger.error("Error while processing image.", e);
            }
            return null;
        }

        @Override
        protected void scheduled() {
            super.scheduled();    //To change body of overridden methods use File | Settings | File Templates.
            logger.info("Scheduled...");
        }

        @Override
        protected void running() {
            super.running();    //To change body of overridden methods use File | Settings | File Templates.
            logger.info("Running...");
        }

        @Override
        protected void succeeded() {
            super.succeeded();    //To change body of overridden methods use File | Settings | File Templates.
            logger.info("Succeeded...");
        }

        @Override
        protected void cancelled() {
            super.cancelled();    //To change body of overridden methods use File | Settings | File Templates.
            logger.info("Cancelled...");
        }

        @Override
        protected void failed() {
            super.failed();    //To change body of overridden methods use File | Settings | File Templates.
            logger.info("Failed...");
        }
    }
}
