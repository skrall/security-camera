package org.krall.security.image;

import com.google.common.eventbus.EventBus;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import org.krall.security.commandline.AppOptions;
import org.krall.security.event.EventBusSingleton;
import org.krall.security.event.ImageChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class FXImageCompare {

    private static final Logger logger = LoggerFactory.getLogger(FXImageCompare.class);

    private Image img1 = null;

    private Image img2 = null;

    protected int comparex = 0;

    protected int comparey = 0;

    protected int factorA = 0;

    protected int factorD = 10;

    protected boolean match = false;

    protected int debugMode = 0; // 1: textual indication of change, 2: difference of factors

    private int width;

    private int heigth;

    private List<PixelLocation> pixelLocations = new ArrayList<>();

    public FXImageCompare() {
    }

    public FXImageCompare(String image1, String image2) {
        try {
            img1 = new Image(new FileInputStream(new File(image1)));
            img2 = new Image(new FileInputStream(new File(image2)));
        } catch (Exception e) {
            logger.error("Error while creating FXImageCompare instance.", e);
            throw new RuntimeException("Error while creating FXImageCompare instance.", e);
        }
    }

    // set the parameters for use during change detection.
    public void setParameters(int x, int y, int factorA, int factorD) {
        this.comparex = x;
        this.comparey = y;
        this.factorA = factorA;
        this.factorD = factorD;
    }

    // want to see some stuff in the console as the comparison is happening?
    public void setDebugMode(int m) {
        this.debugMode = m;
    }

    public void setImg1(Image img1) {
        this.img1 = img1;
    }

    public Image getImg1() {
        return img1;
    }

    public void setImg2(Image img2) {
        this.img2 = img2;
    }

    public Image getImg2() {
        return img2;
    }

    // returns true if image pair is considered a match
    public boolean match() {
        return this.match;
    }

    public void compare() {
        pixelLocations.clear();
        StringBuilder sb = new StringBuilder();
        logger.info("Starting compare...");
        width = (int) img1.getWidth();
        heigth = (int) img1.getHeight();
        int blocksx = width / comparex;
        int blocksy = heigth / comparey;
        PixelReader reader1 = img1.getPixelReader();
        PixelReader reader2 = img2.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        this.match = true;
        int[] buf = new int[blocksx * blocksy];
        for (int y = 0; y < comparey; y++) {
            if (debugMode > 0) sb.append("|");
            for (int x = 0; x < comparex; x++) {

                logger.trace("X: {} BlocksX: {} Y: {} BlocksY: {}", x, blocksx, y, blocksy);

                reader1.getPixels(x * blocksx, y * blocksy, blocksx, blocksy, pixelFormat, buf, 0, blocksy);
                int b1 = getAveragePixelValues(buf);

                reader2.getPixels(x * blocksx, y * blocksy, blocksx, blocksy, pixelFormat, buf, 0, blocksy);
                int b2 = getAveragePixelValues(buf);

                int diff = Math.abs(b1 - b2);
                if (diff > factorA) { // the difference in a certain region has passed the threshold value of factorA
                    // draw an indicator on the change image to show where change was detected.
                    //gc.drawRect(x*blocksx, y*blocksy, blocksx - 1, blocksy - 1);
                    this.match = false;
                    pixelLocations.add(new PixelLocation(x, y));
                    //return;
                }
                if (debugMode == 1) sb.append((diff > factorA ? "X" : " "));
                if (debugMode == 2) sb.append(diff + (x < comparex - 1 ? "," : ""));

            }
            if (debugMode > 0) sb.append("|\n");
        }
        if(!match && !AppOptions.getInstance().isNoWriteDifferences()) {
            EventBus eventBus = EventBusSingleton.getInstance().getEventBus();
            eventBus.post(new ImageChangeEvent(img2, pixelLocations));
        }
        logger.info("Image:\n{}", sb);
        logger.info("Finished compare.");
    }

    private int getAverageBrightness(int[] pixels) {
        int alpha = 0, red, green, blue;
        int total = 0;
        for (int color : pixels) {
            logger.trace("Color: {}", color);
            //alpha = (color >>> 24);
            red = (color >>> 16) & 0xFF;
            green = (color >>> 8) & 0xFF;
            blue = (color & 0xFF);

            int brightness = (int) ((0.2126 * red) + (0.7152 * green) + (0.0722 * blue));
            logger.trace("\tRed: {} Green: {} Blue: {} Alpha: {} Brightness: {}", red, green, blue, alpha, brightness);
            total += brightness;
        }
        return (total / ((width / factorD) * (heigth / factorD)));
    }

    private int getAveragePixelValues(int[] pixels) {
        int alpha = 0, red, green, blue;
        int total = 0;
        for (int color : pixels) {
            logger.trace("Color: %d", color);
            //alpha = (color >>> 24);
            red = (color >>> 16) & 0xFF;
            green = (color >>> 8) & 0xFF;
            blue = (color & 0xFF);

            int pixelValues = (red + green + blue) / 3;
            logger.trace("\tRed: {} Green: {} Blue: {} Alpha: {} PixelValues: {}", red, green, blue, alpha,
                         pixelValues);
            total += pixelValues;
        }
        return (total / ((width / factorD) * (heigth / factorD)));
    }

}
