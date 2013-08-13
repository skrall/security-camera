package org.krall.security.event;

import com.google.common.eventbus.Subscribe;
import javafx.scene.image.Image;
import org.krall.security.commandline.AppOptions;
import org.krall.security.image.PixelLocation;
import org.krall.security.util.SwingFXUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageChangeDifferenceListener {

    private static final Logger logger = LoggerFactory.getLogger(ImageChangeDifferenceListener.class);

    private int numberOfDifferences = 0;

    @Subscribe
    public void listenForChangeEvent(ImageChangeEvent changeEvent) {
        logger.info("Got ChangeEvent...");
        try {
            Image image = changeEvent.getImage();

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            if (!AppOptions.getInstance().isNoDrawDifferenceMarks()) {
                Graphics2D gc = bufferedImage.createGraphics();
                gc.setColor(java.awt.Color.RED);

                AppOptions options = AppOptions.getInstance();

                int width = (int) image.getWidth();
                int heigth = (int) image.getHeight();
                int blocksx = width / options.getVerticalRegions();
                int blocksy = heigth / options.getHorizontalRegions();

                for (PixelLocation pixelLocation : changeEvent.getPixelLocations()) {
                    gc.drawRect(pixelLocation.getX() * blocksx, pixelLocation.getY() * blocksy, blocksx, blocksy);
                }
            }

            Path outputImage = Paths.get(AppOptions.getInstance().getOutputDirectory().getAbsolutePath(),
                                         String.format("%010d.png", numberOfDifferences++));

            ImageIO.write(bufferedImage, "png", outputImage.toFile());
        } catch (Exception e) {
            logger.error("Error while creating change indicator image.", e);
            throw new RuntimeException("Error while creating change indicator image.", e);
        }
        logger.info("Finished processing change event.");
    }

}
