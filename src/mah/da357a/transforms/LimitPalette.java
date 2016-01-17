package mah.da357a.transforms;

import mah.da357a.palette.CustomPalette;
import mah.da357a.palette.Palette;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

/**
 * Compression transform that takes a source image and compresses the data by limiting the palette to 8-bits (256 colors)
 *
 * @author Albert Kaaman & Filip Harald
 */
public class LimitPalette {

	public static byte[] apply(BufferedImage img) {
		
		byte[] bytes = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();

		byte[] out = new byte[(256*4) + (bytes.length / 3)];
		
		ByteArray bits = new ByteArray(out);

		// Always use custom palette instead of default
		Palette cp = new CustomPalette(img);
		//Palette cp = new StandardPalette();

		// Write palette at start of compressed data
		int[] palette = cp.getPalette();
		for (int c : palette)
			bits.writeInt(c);

		// Write 1 byte (palette index) per pixel
		int index;
		for (int i = 0; i < bytes.length; i += 3) {
			index = cp.getIndex(bytes[i + 2] & 0xFF, bytes[i + 1] & 0xFF, bytes[i] & 0xFF);

			bits.writeByte(index);
		}

		return bits.toByteArray();
	}
	
	public static byte[] revert(byte[] bytes) {

		// Recreates custom palette from compressed file
		CustomPalette cp = new CustomPalette(Arrays.copyOfRange(bytes, 0, 1024));

		// Output array will contain decompressed byte data
		byte[] out = new byte[(bytes.length - (256*4)) * 3];
		
		int s = -1;
		for (int i = 256*4; i < bytes.length; i++) {

			// We read a byte (palette index for one pixel) and retrieves palette color
			int c = cp.getColor(bytes[i] & 0xFF);

			// Write palette color to byte array as three bytes
			out[++s] = (byte)((c >>> 16) & 0xFF);  
			out[++s] =  (byte)((c >>> 8) & 0xFF);
			out[++s] = (byte)(c & 0xFF);
			
		}
		
		return out;
	}
}
