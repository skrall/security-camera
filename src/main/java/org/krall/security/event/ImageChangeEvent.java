package org.krall.security.event;

import javafx.scene.image.Image;
import org.krall.security.image.PixelLocation;

import java.util.ArrayList;
import java.util.List;

public class ImageChangeEvent {

    private Image image;
    private List<PixelLocation> pixelLocations = new ArrayList<>();
    private int comparex;
    private int comparey;

    public ImageChangeEvent(Image image, List<PixelLocation> pixelLocations, int comparex, int comparey) {
        this.image = image;
        this.pixelLocations = pixelLocations;
        this.comparex = comparex;
        this.comparey = comparey;
    }

    public Image getImage() {
        return image;
    }

    public List<PixelLocation> getPixelLocations() {
        return pixelLocations;
    }

    public int getComparex() {
        return comparex;
    }

    public int getComparey() {
        return comparey;
    }
}
