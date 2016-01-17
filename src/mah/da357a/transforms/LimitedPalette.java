package mah.da357a.transforms;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import mah.da357a.ImageUtils;

public class LimitedPalette {

	public static byte[] apply(BufferedImage img) {
		
		byte[] bytes = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		
		System.out.println("Bytes = " + bytes.length);
		
		byte[] out = new byte[(256*4) + (bytes.length / 3)];
		
		BitArray bits = new BitArray(out);
		
		//int[] palette = Palette.getPalette();
		CustomPalette cp = new CustomPalette(img);
		//Palette cp = new Palette();
		
		int[] palette = cp.getPalette();
		
		for (int c : palette)
			bits.writeInt(c);
		
		int index = -1;
		for (int i = 0; i < bytes.length; i += 3) {
			index = cp.getCorrespondingColorIndex(bytes[i + 2] & 0xFF, bytes[i + 1] & 0xFF, bytes[i] & 0xFF);
			/*
			if(i % 50000 == 0){
				System.out.println("Jag jobbar " + i);
			}*/
			bits.writeByte(index);
		}
		
		
		
		return bits.toByteArray();
	}
	
	public static byte[] revert(byte[] bytes) {
		
		CustomPalette cp = new CustomPalette(Arrays.copyOfRange(bytes, 0, 1024));
		
		byte[] out = new byte[(bytes.length - (256*4)) * 3];
		
		int s = -1;
		for (int i = 256*4; i < bytes.length; i++) {
			
			int c = cp.getColor(bytes[i] & 0xFF);
			
			out[++s] = (byte)((c >>> 16) & 0xFF);  
			out[++s] =  (byte)((c >>> 8) & 0xFF);
			out[++s] = (byte)(c & 0xFF);
			
		}
		
		return out;
	}
}
