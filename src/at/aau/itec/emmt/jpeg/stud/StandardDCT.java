package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.DCTBlock;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.DCTBlockI;
import at.aau.itec.emmt.jpeg.spec.DCTI;

import static java.lang.Math.PI;
import static at.aau.itec.emmt.jpeg.spec.BlockI.N;

public class StandardDCT implements DCTI {

    private static final double oneRootTwo = 1f / Math.sqrt(2);

    private static double C(int pos){
        return pos == 0 ? oneRootTwo : 1;
    }

    @Override
    public DCTBlockI forward(BlockI b) {
        final int[][] f = levelShift(b);

        final double[][] F = new double[N][N];

        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double c4 = ( C(u) * C(v) ) / 4f;

                double sum = 0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        double cos_1 = Math.cos(((2f * i + 1f) * u * PI) / 16f);
                        double cos_2 = Math.cos(((2f * j + 1f) * v * PI) / 16f);

                        sum += f[i][j] * cos_1 * cos_2;
                    }
                }

                sum *= c4;

                F[u][v] = sum;
            }
        }
        return new DCTBlock(F);
    }


    private int[][] levelShift(BlockI b) {
        int[][] data = b.getData();

        for (int i = 0; i < BlockI.N; i++) {
            for (int j = 0; j < BlockI.N; j++) {
                data[i][j] -= 128;
            }
        }

        return data;
    }


}
