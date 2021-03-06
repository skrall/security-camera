package org.krall.security.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import sun.awt.image.IntegerComponentRaster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class SwingFXUtils {

    private SwingFXUtils() {
    }

    public static BufferedImage fromFXImage(Image img, BufferedImage bimg) {
        PixelReader pr = img.getPixelReader();
        if (pr == null) {
            return null;
        }
        int iw = (int) img.getWidth();
        int ih = (int) img.getHeight();
        if (bimg != null) {
            int type = bimg.getType();
            int bw = bimg.getWidth();
            int bh = bimg.getHeight();
            if (bw < iw || bh < ih ||
                (type != BufferedImage.TYPE_INT_ARGB && type != BufferedImage.TYPE_INT_ARGB_PRE)) {
                bimg = null;
            } else if (iw < bw || ih < bh) {
                Graphics2D g2d = bimg.createGraphics();
                g2d.setComposite(AlphaComposite.Clear);
                g2d.fillRect(0, 0, bw, bh);
                g2d.dispose();
            }
        }
        if (bimg == null) {
            bimg = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB_PRE);
        }
        IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
        int offset = icr.getDataOffset(0);
        int scan = icr.getScanlineStride();
        int data[] = icr.getDataStorage();
        WritablePixelFormat<IntBuffer> pf = (bimg.isAlphaPremultiplied() ? PixelFormat.getIntArgbPreInstance() :
                                             PixelFormat.getIntArgbInstance());
        pr.getPixels(0, 0, iw, ih, pf, data, offset, scan);

        return bimg;
    }

}
