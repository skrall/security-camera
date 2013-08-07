package org.krall.security;

import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kohsuke.args4j.CmdLineParser;
import org.krall.security.command.CaptureThumbnailImage;
import org.krall.security.commandline.AppOptions;
import org.krall.security.compare.CaptureAndCompareImages;
import org.krall.security.compare.SimpleCaptureAndCompareImages;
import org.krall.security.compare.WatcherCaptureAndCompareImages;
import org.krall.security.event.EventBusSingleton;
import org.krall.security.event.ImageChangeDifferenceListener;
import org.krall.security.image.FXImageCompare;
import org.krall.security.image.ImageCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Watching directory...
 */
public class App extends Application {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage stage) throws Exception {
        CaptureAndCompareImages captureAndCompareImages;
        if(AppOptions.getInstance().isUseTimeLapse()) {
            captureAndCompareImages = new WatcherCaptureAndCompareImages();
        } else {
            captureAndCompareImages = new SimpleCaptureAndCompareImages();
        }

        EventBus eventBus = EventBusSingleton.getInstance().getEventBus();
        eventBus.register(new ImageChangeDifferenceListener());

        captureAndCompareImages.captureAndCompareImages();
    }

    public static void main(String[] args) {
        logger.info("Starting application...");
        App app = new App();
        CmdLineParser parser = new CmdLineParser(AppOptions.getInstance());
        try {
            parser.parseArgument(args);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar myprogram.jar [options...] arguments...");
            parser.printUsage(System.err);
            return;
        }

        app.setLoggingLevel();

        launch();

        logger.info("Application finished.");
    }

    private void setLoggingLevel() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(AppOptions.getInstance().getLogLevel());
    }
}
