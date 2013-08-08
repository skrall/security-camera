package org.krall.security.compare;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.krall.security.command.CaptureTimeLapseImage;
import org.krall.security.command.INotifyWait;
import org.krall.security.commandline.AppOptions;
import org.krall.security.image.FXImageCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WatcherCaptureAndCompareImages implements CaptureAndCompareImages {

    private static final Logger logger = LoggerFactory.getLogger(WatcherCaptureAndCompareImages.class);

    private FXImageCompare imageCompare = new FXImageCompare();

    private ThreadPoolExecutor executor;

    public WatcherCaptureAndCompareImages() {
        executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                                          new ArrayBlockingQueue<Runnable>(1));
        AppOptions options = AppOptions.getInstance();
        imageCompare
                .setParameters(options.getVerticalRegions(), options.getHorizontalRegions(), options.getSensitivity(),
                               options.getStabilizer());
        imageCompare.setDebugMode(2);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void captureAndCompareImages() {
        logger.info("Starting to run capture...");
        CaptureTimeLapseImage captureTimeLapseImage = new CaptureTimeLapseImage();
        captureTimeLapseImage.run();
        INotifyWait iNotifyWait = new INotifyWait(this);
        iNotifyWait.run();
        logger.info("Finished running capture.");
    }

    public void processFile(String file) {
        Path image = null;
        try {
            image = Paths.get(file);
            executor.submit(new ImageProcessor(image, imageCompare));
        } catch (Exception ree) {
            logger.warn("The raspistill is capturing images too fast.  Need to slow down the speed of " +
                        "capturing the images by increasing the -m flag.");
            try { Files.delete(image); } catch (Exception ignore) {}
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
                logger.info("Beginning work in thread");
                Image image = new Image(Files.newInputStream(imageFile));
                imageCompare.setImg2(image);
                if (imageCompare.getImg1() != null) {
                    imageCompare.compare();
                    logger.info("Detected a difference: {}", !imageCompare.match());
                }
                imageCompare.setImg1(image);
                Files.delete(imageFile);
            } catch (Exception e) {
                logger.error("Error while processing image.", e);
            }
            return null;
        }
    }
}
