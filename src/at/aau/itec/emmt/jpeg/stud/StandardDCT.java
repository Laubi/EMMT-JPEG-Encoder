package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.Block;
import at.aau.itec.emmt.jpeg.impl.DCTBlock;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.DCTI;

public class StandardDCT implements DCTI {

    @Override
    public DCTBlockI forward(BlockI b) {
        int N = BlockI.N;
        double[] c = new double[BlockI.N];
        int[][] f;
        b = levelShift(b);
        f = b.getData();
        //initialize Coefficients
        for (int i = 1; i < c.length; i++) {
            c[i] = 1;
        }
        c[0] = (1 / Math.sqrt(2.0));

        double[][] F = new double[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += f[i][j] * Math.cos(((2 * i + 1) * u * Math.PI) / 16.0) * Math.cos(((2 * j + 1) * v * Math.PI) / 16.0);
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        return new DCTBlock(F);
    }


    private BlockI levelShift(BlockI b) {
        int[][] data = b.getData();
        for (int i = 0; i < BlockI.N; i++) {
            for (int j = 0; j < BlockI.N; j++) {
                data[i][j] = data[i][j] - 128;
            }
        }
        return new Block(data);
    }


}
