package mah.da357a.tests;

import mah.da357a.transforms.ByteArray;

/**
 * Created by nekosaur on 2016-01-15.
 */
public class TestBitArray {

    public static void main(String[] args) {

        ByteArray bits = new ByteArray(32);

        bits.write(0, (byte)32);

        System.out.println(bits.toString());
    }
}
