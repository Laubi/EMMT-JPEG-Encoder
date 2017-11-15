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
    	int height=rgbImg.getHeight(null);
    	int width=rgbImg.getWidth(null);
    	int[] pixels = new int[width*height];
    	int[][] yComp= new int[width][height];
    	int[][] uComp= new int[width][height];
    	int[][] vComp= new int[width][height];
    	PixelGrabber pg = new PixelGrabber(rgbImg, 0, 0, width, height, pixels, 0, width);
    	
    	
    	try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
        }
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int red=(pixels[j * width + i]>>16) & 0xff;
                int green=(pixels[j * width + i]>>8) & 0xff;
                int blue=(pixels[j * width + i]) & 0xff;
                int y=(int) (0.299*red+0.587*green+0.114*blue);
                int u=(int) (128-0.1687*red-0.3313*green+0.5*blue);
                int v=(int) (128+0.5*red-0.4187*green-0.0813*blue);
                yComp[j][i]=y;
                uComp[j][i]=u;
                vComp[j][i]=v;
            }
        }
        Component Y= new Component(yComp,YUVImage.Y_COMP);
    	Component U= new Component(uComp,YUVImage.CB_COMP);
    	Component V= new Component(vComp,YUVImage.CR_COMP);
    	
        return new YUVImage(Y,U,V,SubSamplerI.YUV_444);
    }
    /*public void handlesinglepixel(int x, int y, int pixel) {
        int red   = (pixel >> 16) & 0xff;
        int green = (pixel >>  8) & 0xff;
        int blue  = (pixel      ) & 0xff;
        // Deal with the pixel as necessary...
   }*/

}
