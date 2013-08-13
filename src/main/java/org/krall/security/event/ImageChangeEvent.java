package org.krall.security.event;

import javafx.scene.image.Image;
import org.krall.security.image.PixelLocation;

import java.util.ArrayList;
import java.util.List;

public class ImageChangeEvent {

    private Image image;
    private List<PixelLocation> pixelLocations = new ArrayList<>();

    public ImageChangeEvent(Image image, List<PixelLocation> pixelLocations) {
        this.image = image;
        this.pixelLocations = pixelLocations;
    }

    public Image getImage() {
        return image;
    }

    public List<PixelLocation> getPixelLocations() {
        return pixelLocations;
    }

}
