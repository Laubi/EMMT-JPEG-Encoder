package at.aau.itec.emmt.jpeg.stud;

import at.aau.itec.emmt.jpeg.impl.AbstractHuffmanCoder;
import at.aau.itec.emmt.jpeg.impl.RunLevel;
import at.aau.itec.emmt.jpeg.spec.BlockI;
import at.aau.itec.emmt.jpeg.spec.EntropyCoderI;
import at.aau.itec.emmt.jpeg.spec.RunLevelI;

import java.util.LinkedList;
import java.util.List;

public class HuffmanCoder extends AbstractHuffmanCoder {
    final int[] ZIGZAG = EntropyCoderI.ZIGZAG_ORDER;

    @Override
    public RunLevelI[] runLengthEncode(BlockI quantBlock) {
        final int [][] data = quantBlock.getData();
        final List<RunLevelI> results = new LinkedList<>();
        int curCountZeros = 0;

        for(int i = 1; i < ZIGZAG.length; i++){ // We do not encode the first position, so we start at position 1
            int offset = ZIGZAG[i];
            int x = xPos(offset);
            int y = yPos(offset);

            int curData = data[x][y];

            if(curData == 0){
                curCountZeros++;

                if(i == ZIGZAG.length - 1){
                    results.add(new RunLevel(0,0));
                }
            }else{
                results.add(new RunLevel(curCountZeros, curData));
                curCountZeros = 0;
            }

        }

        return results.toArray(new RunLevelI[results.size()]);
    }



    private int xPos(int offset){
        return offset / 8;
    }
    private int yPos(int offset){
        return offset % 8;
    }
}
