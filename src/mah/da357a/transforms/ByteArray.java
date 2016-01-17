package mah.da357a.transforms;

/**
 * Array representing a number of bytes. Contains convenience functions for manipulating byte array.
 *
 * http://stackoverflow.com/questions/15736626/java-how-to-create-and-manipulate-a-bit-array-with-length-of-10-million-bits
 *
 * @author Albert Kaaman & Stackoverflow
 */
public class ByteArray {

    private byte bits[] = null;
    private int index = 0;
    private int bitIndex = -1;

    /**
     * Constructs a bit array with the specified size
     * 
     * @param size Size of array in bytes
     */
    public ByteArray(int size) {
        //bits = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
        bits = new byte[size];
    }

    /**
     * Constructs a bit array representing all the bits in the specified byte array.
     * 
     * @param array Array
     */
    public ByteArray(byte[] array) {
        bits = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bits[i] = array[i];
        }
    }
    
    /**
     * Sets the index position
     * 
     * @param index Index
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
     * http://www.herongyang.com/Java/Bit-String-Get-Bit-from-Byte-Array.html
     * 
     * @param pos Position
     * @return Bit at position
     */
    private int getBit(int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = bits[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }

    /**
     * Sets the bit to the specified value at the specified position.
     * http://www.herongyang.com/Java/Bit-String-Set-Bit-to-Byte-Array.html
     * 
     * @param pos Position
     * @param val Bit value
     */
    private void setBit(int pos, int val) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte oldByte = bits[posByte];
        oldByte = (byte) (((0xFF7F>>posBit) & oldByte) & 0x00FF);
        byte newByte = (byte) ((val<<(8-(posBit+1))) | oldByte);
        bits[posByte] = newByte;
    }
    
    /**
     * Writes the int value using just one byte.
     * 
     * @param value Value to write
     */
    public void writeByte(int value) {
        if (bitIndex > 0) {
            for (int i = 8; i > 0; i--) {
                int b = (value >>> (i - 1)) & 0x1;

                setBit((index*8) + bitIndex++ + (8 - i), b);

                if (bitIndex >= 7) {
                    index++;
                    bitIndex = 0;
                }
            }
        } else {
            byte b = (byte) value;
            bits[index++] = b;
        }

    }

    /**
     * Writes half a byte
     * @param value Value to write
     */
    public void writeHalfByte(int value) {
        bitIndex = bitIndex == -1  ? 0 : bitIndex;
        for (int i = 4; i > 0; i--) {
            int b = (value >> (i - 1)) & 0x1;

            setBit((index*8) + bitIndex++, b);
        }

        if (bitIndex >= 7) {
            bitIndex = -1;
            index++;
        }
    }
    
    /**
     * Reads the byte at the current index position.
     * 
     * @return Read byte
     */
    public byte readByte() {
    	byte b = bits[index++];
    	
    	return b;
    }
    
    /**
     * Uses readByte, but translates it to int before returning.
     * 
     * @return Read int
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
     * @param value Value to write
     */
    public void writeInt(int value) {
    	/*
    	for (int i = 32; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(index + (32 - i), b == 1 ? true : false);
        }*/
    	
    	for (int i = 4; i > 0; i--) {
    		byte b = (byte)((value >>> ((i - 1) * 8)) & 0xFF);
    		bits[index++] = b;
    	}    	
    	
    }

    /**
     * Writes the specified bit value to the specified pos
     * 
     * @param pos Position
     * @param value Write
     */
    public void write(int pos, byte value) {

        for (int i = 8; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(pos + (8 - i), b);
        }
    }

    /**
     * Writes the specified bit value numBits times, starting at pos.
     * 
     * @param pos Position
     * @param numBits Number of bits to write
     * @param value Value to write
     */
    public void write(int pos, int numBits, int value) {

        for (int i = numBits; i > 0; i--) {
            int b = (value >>> (i - 1)) & 0x1;

            setBit(pos + (numBits - i), b);
        }
    }

    /**
     * @return the length of the bit array
     */
    public int getLength() {
        return bits.length;
    }

    /**
     * @return the bit array represented in a byte array
     */
    public byte[] toByteArray() {
        return bits;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getLength(); i++)
            sb.append(this.getBit(i));
        return sb.toString();
    }

}
