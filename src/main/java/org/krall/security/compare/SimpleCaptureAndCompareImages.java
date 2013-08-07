package org.krall.security.compare;

import javafx.scene.image.Image;
import org.krall.security.command.CaptureThumbnailImage;
import org.krall.security.commandline.AppOptions;
import org.krall.security.image.FXImageCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCaptureAndCompareImages implements CaptureAndCompareImages {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCaptureAndCompareImages.class);

    @Override
    public void captureAndCompareImages() {
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
                //if(!imageCompare.match()) {
                //    imageCompare.writeImg2ToFile(String.format("%05d.png", numberOfDifferences++));
                //}
            }
            oldImage = newImage;
            imageCompare.setImg1(oldImage);
        }
    }
}
