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
    private int index = 0;

    public BitArray(int size) {
        //bitX8 = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
        bitX8 = new byte[size];
    }

    public BitArray(byte[] array) {
        bitX8 = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bitX8[i] = array[i];
        }
    }
    
    public void setPosition(int index) {
    	this.index = index;
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
    
    public void writeByte(int value) {
    	/*
    	for (int i = 8; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(index + (8 - i), b == 1 ? true : false);
        }*/
    	byte b = (byte)value;
    	bitX8[index++] = b;
    	
    }
    
    public void writeInt(int value) {
    	/*
    	for (int i = 32; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(index + (32 - i), b == 1 ? true : false);
        }*/
    	
    	for (int i = 4; i > 0; i--) {
    		byte b = (byte)((value >>> ((i - 1) * 8)) & 0xFF);
    		bitX8[index++] = b;
    	}    	
    	
    }

    public void write(int pos, byte value) {

        for (int i = 8; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(pos + (8 - i), b == 1 ? true : false);
        }
    }

    public void write(int pos, int numBits, int value) {

        for (int i = numBits; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(pos + (numBits - i), b == 1 ? true : false);
        }
    }

    public int getLength() {
        return bitX8.length * 8;
    }

    public byte[] toByteArray() {
        return bitX8;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getLength(); i++)
            sb.append(this.getBit(i) == true ? 1 : 0);
        return sb.toString();
    }

}
