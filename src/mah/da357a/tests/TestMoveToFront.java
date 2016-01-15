package mah.da357a.tests;

import mah.da357a.transforms.MoveToFront;

import java.util.BitSet;

/**
 * Created by nekosaur on 2016-01-15.
 */
public class TestMoveToFront {

    public static void main(String[] args) {

        byte[] bytes = new byte[] { Byte.parseByte("20"), Byte.parseByte("83") };

        System.out.println(bytes.length);
        System.out.println(BitSet.valueOf(bytes).length());
        BitSet set = BitSet.valueOf(bytes);

        //System.out.println(String.format("%8s", Integer.toBinaryString(bytes[0])).replace(' ', '0'));


        MoveToFront mtf = new MoveToFront();

        bytes = mtf.apply(bytes);

        bytes = mtf.revert(bytes);

        //System.out.println(String.format("%8s", Integer.toBinaryString(bytes[0])).replace(' ', '0'));


    }
}
