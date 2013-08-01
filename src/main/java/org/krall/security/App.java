package org.krall.security;

import javafx.scene.image.Image;
import org.kohsuke.args4j.CmdLineParser;
import org.krall.security.command.CaptureThumbnailImage;
import org.krall.security.commandline.AppOptions;
import org.krall.security.image.FXImageCompare;
import org.krall.security.image.ImageCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Watching directory...
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

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
        app.doImageCaptureAndCompare();
        logger.info("Application finished.");
    }

    private void doImageCaptureAndCompare() {
        int numberOfDifferences = 0;
        Image oldImage = null;
        Image newImage;
        CaptureThumbnailImage captureThumbnailImage = new CaptureThumbnailImage();
        FXImageCompare imageCompare = new FXImageCompare();
        imageCompare.setDebugMode(2);
        AppOptions options = AppOptions.getInstance();
        imageCompare.setParameters(options.getVerticalRegions(), options.getHorizontalRegions(),
                                   options.getSensitivity(), options.getStabilizer());
        for(;;) {
            captureThumbnailImage.run();
            newImage = captureThumbnailImage.getImage();
            imageCompare.setImg2(newImage);
            if(oldImage != null) {
                imageCompare.compare();
                logger.info("Detected a difference: {}", !imageCompare.match());
                if(!imageCompare.match()) {
                    imageCompare.writeImg2ToFile(String.format("%05d.png", numberOfDifferences++));
                }
            }
            oldImage = newImage;
            imageCompare.setImg1(oldImage);
        }
    }

    private void setLoggingLevel() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(AppOptions.getInstance().getLogLevel());
    }

    private void compareImages(String image1, String image2) {
        long start = System.currentTimeMillis();

        // Create a compare object specifying the 2 images for comparison.
        ImageCompare ic = new ImageCompare(image1, image2);
        // Set the comparison parameters.
        //   (num vertical regions, num horizontal regions, sensitivity, stabilizer)
        ic.setParameters(8, 6, 5, 10);
        // Display some indication of the differences in the image.
        ic.setDebugMode(2);
        // Compare.
        ic.compare();
        // Display if these images are considered a match according to our parameters.
        logger.info("Match: " + ic.match());
        // If its not a match then write a file to show changed regions.
        if (!ic.match()) {
            //ImageCompare.saveJPG(ic.getChangeIndicator(), "changes.jpg");
        }

        System.out.println("ImageCompare Total time in ms: " + (System.currentTimeMillis() - start));
    }

    private void compareImagesFX(String image1, String image2) {
        long start = System.currentTimeMillis();

        // Create a compare object specifying the 2 images for comparison.
        FXImageCompare ic = new FXImageCompare(image1, image2);
        // Set the comparison parameters.
        //   (num vertical regions, num horizontal regions, sensitivity, stabilizer)
        ic.setParameters(8, 6, 1000, 10);
        // Display some indication of the differences in the image.
        ic.setDebugMode(2);
        // Compare.
        ic.compare();
        // Display if these images are considered a match according to our parameters.
        logger.info("Match: " + ic.match());
        // If its not a match then write a file to show changed regions.
        if (!ic.match()) {
            //saveJPG(ic.getChangeIndicator(), "changes.jpg");
        }

        logger.info("FXImageCompare Total time in ms: " + (System.currentTimeMillis() - start));

    }

}
