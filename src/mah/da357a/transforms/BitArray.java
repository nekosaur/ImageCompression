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

    /**
     * Constructs a bit array with the specified index
     * 
     * @param size
     */
    public BitArray(int size) {
        //bitX8 = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
        bitX8 = new byte[size];
    }

    /**
     * Constructs a bit array representing all the bits in the specified byte array.
     * 
     * @param array
     */
    public BitArray(byte[] array) {
        bitX8 = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bitX8[i] = array[i];
        }
    }
    
    /**
     * Sets the index position
     * 
     * @param index
     */
    public void setPosition(int index) {
    	this.index = index;
    }
    
    /**
     * @return The Index position
     */
    public int getPosition() {
    	return index;
    }

    /**
     * Returns the bit at the specified position
     * 
     * @param pos
     * @return
     */
    public boolean getBit(int pos) {
        return (bitX8[pos / 8] & (1 << (pos % 8))) != 0;
    }

    /**
     * Sets the bit to the specified value at the specified position.
     * 
     * @param pos
     * @param b
     */
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
    
    /**
     * Writes the int value using just one byte.
     * 
     * @param value
     */
    public void writeByte(int value) {
    	/*
    	for (int i = 8; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(index + (8 - i), b == 1 ? true : false);
        }*/
    	byte b = (byte)value;
    	bitX8[index++] = b;
    	
    }
    
    /**
     * Reads the byte at the current index position.
     * 
     * @return
     */
    public byte readByte() {
    	byte b = bitX8[index++];
    	
    	return b;
    }
    
    /**
     * Uses readByte, but translates it to int before returning.
     * 
     * @return
     */
    public int readInt() {
    	
    	int v = 0;
    	
    	v |= (readByte() & 0xFF) << 24;
    	v |= (readByte() & 0xFF) << 16;
    	v |= (readByte() & 0xFF) << 8;
    	v |= (readByte() & 0xFF);
    	
    	return v;
    }
    
    /**
     * Writes the specified int
     * 
     * @param value
     */
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

    /**
     * Writes the specified bit value to the specified pos
     * 
     * @param pos
     * @param value
     */
    public void write(int pos, byte value) {

        for (int i = 8; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(pos + (8 - i), b == 1 ? true : false);
        }
    }

    /**
     * writes the specified bit value numBits times, starting at pos.
     * 
     * @param pos
     * @param numBits
     * @param value
     */
    public void write(int pos, int numBits, int value) {

        for (int i = numBits; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(pos + (numBits - i), b == 1 ? true : false);
        }
    }

    /**
     * @return the length of the bit array
     */
    public int getLength() {
        return bitX8.length * 8;
    }

    /**
     * @return the bit array represented in a byte array
     */
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
