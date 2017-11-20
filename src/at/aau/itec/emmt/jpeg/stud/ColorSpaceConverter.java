package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.ColorSpaceConverterI;
import at.aau.itec.emmt.jpeg.spec.SubSamplerI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;
import at.aau.itec.emmt.jpeg.impl.Component;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

public class ColorSpaceConverter implements ColorSpaceConverterI {

    @Override
    public YUVImageI convertRGBToYUV(Image rgbImg) {
        int height = rgbImg.getHeight(null);
        int width = rgbImg.getWidth(null);

        YUVImage yuvImage = null;
        int[] pixels = new int[width * height];

        PixelGrabber pixelGrabber = new PixelGrabber(rgbImg, 0, 0, width, height, pixels, 0, width);


        try {
            pixelGrabber.grabPixels();
            yuvImage = convert(pixels, width, height);
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
        }

        return yuvImage;
    }

    private YUVImage convert(int[] pixels, int width, int height) {
        int[][] yComp = new int[width][height];
        int[][] uComp = new int[width][height];
        int[][] vComp = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                Color c = new Color(pixel);

                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                yComp[y][x] = (int) (0.299 * red + 0.587 * green + 0.114 * blue);       // Y
                uComp[y][x] = (int) (128 - 0.1687 * red - 0.3313 * green + 0.5 * blue); // U
                vComp[y][x] = (int) (128 + 0.5 * red - 0.4187 * green - 0.0813 * blue); // V
            }
        }

        Component Y = new Component(yComp, YUVImage.Y_COMP);
        Component U = new Component(uComp, YUVImage.CB_COMP);
        Component V = new Component(vComp, YUVImage.CR_COMP);

        return new YUVImage(Y, U, V, SubSamplerI.YUV_444);
    }

}
