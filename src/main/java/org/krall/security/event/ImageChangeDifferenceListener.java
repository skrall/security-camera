package org.krall.security.event;

import com.google.common.eventbus.Subscribe;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.krall.security.commandline.AppOptions;
import org.krall.security.image.PixelLocation;
import org.krall.security.util.SwingFXUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
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
            Canvas canvas = new Canvas(image.getWidth(), image.getHeight());

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(image, 0, 0);

            int width = (int) image.getWidth();
            int heigth = (int) image.getHeight();
            int blocksx = width / changeEvent.getComparex();
            int blocksy = heigth / changeEvent.getComparey();

            gc.setStroke(Color.RED);
            for(PixelLocation pixelLocation : changeEvent.getPixelLocations()) {
                gc.strokeRect(pixelLocation.getX() * blocksx, pixelLocation.getY() * blocksy, blocksx, blocksy);
            }

            WritableImage wim = new WritableImage(width, heigth);
            canvas.snapshot(null, wim);

            Path outputImage = Paths.get(AppOptions.getInstance().getOutputDirectory().getAbsolutePath(),
                             String.format("%010d.png", numberOfDifferences++));

            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", outputImage.toFile());
        } catch (Exception e) {
            logger.error("Error while creating change indicator image.", e);
            throw new RuntimeException("Error while creating change indicator image.", e);
        }
        logger.info("Finished processing change event.");
    }

}
