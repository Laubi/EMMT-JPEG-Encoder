package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Component;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.SubSamplerI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;

public class SubSampler implements SubSamplerI {

    @Override
    public YUVImageI downSample(YUVImageI yuvImg, int samplingRatio) {
        
    	Component Y= (Component) yuvImg.getComponent(YUVImage.Y_COMP);
     	Component U= (Component) yuvImg.getComponent(YUVImage.CB_COMP);
     	Component V= (Component) yuvImg.getComponent(YUVImage.CR_COMP);
     	
     	int [][] u = U.getData(); 
     	int [][] v = V.getData(); 
     	
     	if(samplingRatio==SubSampler.YUV_422)
     	{
     		u=downSample422(u); 
     		v=downSample422(v); 
     	}else if(samplingRatio==SubSampler.YUV_420)
     	{
     		u=downSample420(u); 
     		v=downSample420(v); 
     	}
     	
     	U= new Component(u,YUVImage.CB_COMP);
     	V= new Component(v,YUVImage.CR_COMP);
     	return new YUVImage(Y, U, V, samplingRatio);
    }

    protected int [][] downSample422(int [][]data){
    	int [][] result = new int[data.length][data[0].length/2]; 
    	for(int x=0,j=0; x<data[0].length; x=x+2, j++)
    	{
    		for(int y=0; y<data.length; y++)
    		{
    			result[y][j]=(data[y][x]+data[y][x+1])/2; 
    		}
    	}
    	return result; 
    }
    
    protected int [][] downSample420(int [][]data){
    	int [][] result = new int[data.length/2][data[0].length/2]; 
    	for(int x=0,x2=0; x<data[0].length; x=x+2, x2++)
    	{
    		for(int y=0,y2=0; y<data.length; y=y+2,y2++)
    		{
    			result[y2][x2]=(data[y][x]+data[y][x+1]+ data[y+1][x]+data[y+1][x+1])/4; 
    		}
    	}
    	return result; 
    }
}
