package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Block;
import at.aau.itec.emmt.jpeg.impl.YUVImage;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.QuantizationI;
import at.aau.itec.emmt.jpeg.spec.YUVImageI;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Quantizer implements QuantizationI {

    @SuppressWarnings("WeakerAccess")
    protected int qualityFactor;

    @SuppressWarnings("unused")
    public Quantizer() {
        this(DEFAULT_QUALITY_FACTOR);
    }

    public Quantizer(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

    @Override
    public int[] getQuantumLuminance() {
        return calculateWithQualityFactor(QuantizationI.QUANTUM_LUMINANCE);
    }

    @Override
    public int[] getQuantumChrominance() {
        return calculateWithQualityFactor(QuantizationI.QUANTUM_CHROMINANCE);
    }

    private int[] calculateWithQualityFactor(final int[] input){
        final int qualityFactorScaled = qualityFactor < 50 ? 5000 / qualityFactor : 200 - 2 * qualityFactor;

        return Arrays.stream(input).map(i -> Math.min(255, Math.max(1, (i * qualityFactorScaled + 50) / 100))).toArray();
    }

    @Override
    public BlockI quantizeBlock(DCTBlockI dctBlock, int compType) {
        final double[][] block = dctBlock.getData();
        final int[] quant = compType == YUVImageI.Y_COMP ? this.getQuantumLuminance() : this.getQuantumChrominance();

        int[][] dataMatrix = new int[8][8];

        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block.length; j++) {
                dataMatrix[i][j] = (int) Math.round(block[i][j] / quant[i * 8 + j]);
            }
        }
        return new Block(dataMatrix);
    }

    @Override
    public void setQualityFactor(int qualityFactor) {
        this.qualityFactor = qualityFactor;
    }

}
