package mah.da357a.transforms;

/**
 * Array representing a number of bits
 *
 * http://stackoverflow.com/questions/15736626/java-how-to-create-and-manipulate-a-bit-array-with-length-of-10-million-bits
 *
 * @author ???
 */
public class BitArray {

    private byte bitX8[] = null;

    public BitArray(int size) {
        bitX8 = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
    }

    public BitArray(byte[] array) {
        bitX8 = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bitX8[i] = array[i];
        }
    }

    public boolean getBit(int pos) {
        return (bitX8[pos / 8] & (1 << (pos % 8))) != 0;
    }

    public void setBit(int pos, boolean b) {
        byte b8 = bitX8[pos / 8];
        byte posBit = (byte) (1 << (pos % 8));
        if (b) {
            b8 |= posBit;
        } else {
            b8 &= (255 - posBit);
        }
        bitX8[pos / 8] = b8;
    }

    public int getLength() {
        return bitX8.length * 8;
    }

    public byte[] toByteArray() {
        return bitX8;
    }

}
