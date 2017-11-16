package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Block;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.QuantizationI;

public class Quantizer implements QuantizationI {

    protected int qualityFactor;

    public Quantizer() {
        this(DEFAULT_QUALITY_FACTOR);
    }

    public Quantizer(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

    @Override
    public int[] getQuantumLuminance() {
    	/** The normative quantization matrix for luminance blocks. **/
    	int[] quant_lum=QuantizationI.QUANTUM_LUMINANCE;
    	int qualityFactorScaled=0;
    	if(qualityFactor<50) {
    		qualityFactorScaled=5000/qualityFactor;
    	}
    	else {
    		qualityFactorScaled=200-2*qualityFactor;
    	}
    	for(int i=0;i<quant_lum.length;i++) {
    		quant_lum[i]=Math.min(255,Math.max(1,(quant_lum[i]*qualityFactorScaled+50)/100 ));
    	}
        return quant_lum;
    }

    @Override
    public int[] getQuantumChrominance() {
    	/** The normative quantization matrix for chrominace blocks. **/
    	int[] quant_chrom=QuantizationI.QUANTUM_CHROMINANCE;
    	int qualityFactorScaled=0;
    	if(qualityFactor<50) {
    		qualityFactorScaled=5000/qualityFactor;
    	}
    	else {
    		qualityFactorScaled=200-2*qualityFactor;
    	}
    	for(int i=0;i<quant_chrom.length;i++) {
    		quant_chrom[i]=Math.min(255,Math.max(1,(quant_chrom[i]*qualityFactorScaled+50)/100 ));
    	}
        return quant_chrom;
    }

    @Override
    public BlockI quantizeBlock(DCTBlockI dctBlock, int compType) {
    	int[] quant;
    	int[][] dataMatrix=new int[8][8];
    	double[][] block=dctBlock.getData();
    	if(compType==YUVImage.Y_COMP) {
    		quant=this.getQuantumLuminance();
    	}
    	else {
    		quant=this.getQuantumChrominance();
    	}
    	for(int i=0;i<block.length;i++) {
    		for(int j=0;j<block.length;j++) {
    			dataMatrix[i][j]=(int) Math.round(block[i][j]/quant[i*8+j]);
    		}
    	}
        return new Block(dataMatrix);
    }

    @Override
    public void setQualityFactor(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

}
