package org.krall.security.command;

import javafx.scene.image.Image;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.krall.security.commandline.AppOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CaptureThumbnailImage implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CaptureThumbnailImage.class);

    private DefaultExecutor executor = new DefaultExecutor();

    private CommandLine command;

    private ByteArrayOutputStream outputStream;

    public CaptureThumbnailImage() {
        outputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStream, null));
        executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
        command = CommandLine.parse(String.format("raspistill -n -t 0 -w %d -h %d -o - ",
                                                  AppOptions.getInstance().getImageWidth(),
                                                  AppOptions.getInstance().getImageHeight()));
    }

    @Override
    public void run() {
        outputStream.reset();
        try {
            int returnValue = executor.execute(command);
            if (returnValue != 0) {
                throw new RuntimeException(String.format("Error while runnng command, returned (%d)", returnValue));
            }
        } catch (Exception e) {
            logger.error("Error while running command", e);
            throw new RuntimeException("Error while running command", e);
        }
    }

    public Image getImage() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return new Image(inputStream);
    }
}
