package mah.da357a.transforms;

import mah.da357a.ImageUtils;

public class LimitedPalette {

	
	public static byte[] apply(byte[] bytes) {
		
		System.out.println("Bytes = " + bytes.length);
		
		byte[] out = new byte[(256*4) + (bytes.length / 3)];
		
		BitArray bits = new BitArray(out);
		
		int[] palette = Palette.getPalette();
		
		for (int c : palette)
			bits.writeInt(c);
		
		int index = -1;
		for (int i = 0; i < bytes.length; i += 3) {
			
			int c = ImageUtils.toInt(bytes[i] & 0xFF, bytes[i + 1] & 0xFF, bytes[i + 2] & 0xFF);

			double minDistance = Double.MAX_VALUE;
			
			/*
			for (int p = 0; p < palette.length; p++) {
				double d = ImageUtils.distance(palette[p], c);
				if (d < minDistance) {
					index = p;
					minDistance = d;
				}
			}*/
			
			/*
			if (i % 16 == 0) {
				System.out.println((bytes[i] & 0xFF) + ","+ (bytes[i + 1] & 0xFF) +","+ (bytes[i + 2] & 0xFF));
				System.out.println(Palette.getCorrespondingColorIndex(bytes[i] & 0xFF, bytes[i + 1] & 0xFF, bytes[i + 2] & 0xFF));
			}
			*/
			index = Palette.getCorrespondingColorIndex(bytes[i] & 0xFF, bytes[i + 1] & 0xFF, bytes[i + 2] & 0xFF);
			
			bits.writeByte(index);
		}
		
		
		
		return bits.toByteArray();
	}
	
	public static byte[] revert(byte[] bytes) {
		
		byte[] out = new byte[(bytes.length - (256*4)) * 3];
		
		int s = -1;
		for (int i = 256*4; i < bytes.length; i++) {
			
			int c = Palette.getColor(bytes[i] & 0xFF);
			
			out[++s] = (byte)((c >>> 16) & 0xFF);  
			out[++s] =  (byte)((c >>> 8) & 0xFF);
			out[++s] = (byte)(c & 0xFF);
			
		}
		
		return out;
	}
}
