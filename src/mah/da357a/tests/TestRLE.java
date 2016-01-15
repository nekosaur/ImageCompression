package mah.da357a.tests;

import mah.da357a.transforms.BitArray;
import mah.da357a.transforms.MoveToFront;
import mah.da357a.transforms.RunLengthEncode;

import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by nekosaur on 2016-01-15.
 */
public class TestRLE {

    public static void main(String[] args) {

        byte[] bytes = new byte[] { Byte.parseByte("0") };

        System.out.println(new BitArray(bytes).toString());

        RunLengthEncode rle = new RunLengthEncode();

        bytes = rle.apply(bytes);

        System.out.println(new BitArray(bytes).toString());

        //bytes = rle.revert(bytes);

        //System.out.println(String.format("%8s", Integer.toBinaryString(bytes[0])).replace(' ', '0'));



    }
}
