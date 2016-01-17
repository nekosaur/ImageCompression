package mah.da357a.transforms;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import mah.da357a.ImageUtils;

public class CustomPalette {

	public int[] palette = new int[256];
	private final static int LOOKUP_SIZE = 15;
	public int[] lookup = new int[(int)Math.pow(2, LOOKUP_SIZE)];
	public int paletteColorSize = 215;//indexes 16-231 Excluding grayscale and truecolours
	
	public CustomPalette(byte[] bytes) {
		
		BitArray bits = new BitArray(bytes);
		
		int counter = 0;
		for (int i = 0; i < bytes.length; i += 4) {
			
			palette[counter++] = bits.readInt();
			
		}
		
		palette[232] = 0x000000;
		palette[233] = 0x080808;
		palette[255] = 0xFFFFFF;
		int intensity = 8;
		for (int i = 234; i < 255; i++) {
			intensity += 10;
			palette[i] = ImageUtils.toInt(intensity, intensity, intensity);
		}
		
		writePalette();
		
	}
	
	public CustomPalette(BufferedImage img) {
		
		Map<Integer, Integer> map = new HashMap<>();
				
		Raster raster = img.getRaster();
		
		int[] pixel = new int[3];
		int color;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				
				pixel = raster.getPixel(x, y, pixel);
				
				color = ImageUtils.toInt(pixel[0], pixel[1], pixel[2]);
				
				if (map.containsKey(color))
					map.put(color, map.get(color) + 1);
				else
					map.put(color, 1);
				
			}
		}
		
		List<Integer> sorted = new LinkedList<>();
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet())
			sorted.add(entry.getKey());
		
		sorted.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer c1, Integer c2) {
				return map.get(c2) - map.get(c1);
			}
			
		});
		
		palette[16] = sorted.get(0);
		int counter = 17;
		boolean add = false;
		for(int i = 0; i < 16; i++){
			palette[i] = 0;
		}
		double delta = 3;
		while (counter < 232) {
			for (int i = 16; i < sorted.size() && counter < 232; i++) {
				System.out.println(counter);
				add = false;
				
				for (int j = 0; j < counter; j++) {
					if (ImageUtils.distanceInLAB(palette[j], sorted.get(i)) < delta) {
					//if (ImageUtils.distance(palette[j], sorted.get(i)) < 4) {
						add = false;
						break;
					}else{					
						add = true;
					}
				}
				
				if (add)
					palette[counter++] = sorted.get(i) & 0xFFFFFF;
				
				
			}
			if (counter < 232)
				delta -= 0.5;
		}
		
		palette[232] = 0x000000;
		palette[233] = 0x080808;
		palette[255] = 0xFFFFFF;
		int intensity = 8;
		for (int i = 234; i < 255; i++) {
			intensity += 10;
			palette[i] = ImageUtils.toInt(intensity, intensity, intensity) & 0xFFFFFF;
		}
			
		writePalette();
		
		int pindex = -1;
		double maxDistance, d;
		
		/*
		for (int r = 0; r < 255; r += 256/32) {
			for (int g = 0; g < 255; g += 256/32) {
				for (int b = 0; b < 255; b += 256/32) {
				*/
		int r, g, b, c;
		for (int i = 0; i < lookup.length; i++) {
			r = i / (32 * 32); 
			g = (i / 32) % 32;
			b = i % 32;
			
			c = ImageUtils.toInt(r*8, g*8, b*8) & 0xFFFFFF;
			
			maxDistance = Double.MAX_VALUE;
			pindex = -1;
			for (int p = 0; p < palette.length; p++) {
				d = ImageUtils.distance(c, palette[p]);
				//System.out.println("d="+d);
				if (d < maxDistance) {
					maxDistance = d;
					pindex = p;
				}
			}
			
			lookup[i] = palette[pindex];
						
			//System.out.println(r + ", " + g + ", " + b);
		}
		
		/*
		for (int r = 0; r < 256/8; r += 1) {
		
			for (int g = 0; g < 256/8; g += 1) {
				for (int b = 0; b < 256/8; b += 1) {
					
					
					//i = r + 256 * (g + 256 * b);
					i = b + (g*32) + (r * 32 * 32);
					//System.out.println(r + ", " + g + ", " + b);
					//System.out.println(i);
					
					c = ImageUtils.toInt(r*8, g*8, b*8) & 0xFFFFFF;

					maxDistance = Double.MAX_VALUE;
					pindex = -1;
					for (int p = 0; p < palette.length; p++) {
						d = ImageUtils.distance(c, palette[p]);
						//System.out.println("d="+d);
						if (d < maxDistance) {
							maxDistance = d;
							pindex = p;
						}
					}
					
					//lookup[r/LOOKUP_SIZE + g/LOOKUP_SIZE + b/LOOKUP_SIZE] = index;
					
					
					
					int ri = ((r*8) >>> 3) & 0x1F;
					int gi = ((g*8) >>> 3) & 0x1F;
					int bi = ((b*8) >>> 3) & 0x1F;
					
					System.out.println((bi << 10) | (gi << 5) | ri);
					
					lookup[(ri << 10) | (gi << 5) | bi] = pindex;
					
					
					//lookup[i] = pindex;
					System.out.println("i="+i);
				}
			}
		}*/	
		
		//System.out.println("sorting");
		//sort();
		//writePalette();
		
	}
	
	public int getIndex(int r, int g, int b) {
		r = (r >>> 3);
		g = (g >>> 3);
		b = (b >>> 3);
		
		//int tindex = (r << 10) | (g << 5) | b;
		
		int tindex = r + (g*32) + (b * 32 * 32);
		int pindex = lookup[tindex];
		
		return pindex;
		//return palette[pindex];
	}
	
	public int getColor(int index){
		return palette[index];
	}
	
	public int[] getPalette() {
		return palette;
	}
	
	public int getCorrespondingColorIndex(int r, int g, int b) {
		/*
		int index = -1;
		int c = ImageUtils.toInt(r, g, b);

		double minDistance = Double.MAX_VALUE;
		for (int p = 0; p < palette.length; p++) {
			double d = ImageUtils.distance(palette[p], c);
			if (d < minDistance) {
				index = p;
				minDistance = d;
			}
		}
		
		return index;*/
		int index = -1;
		if(r == 0 && g == 0 && b == 0){
			index = 0;
		}else if(r == g && r == b){//All colors have same value, this means it's grayscale. (indexes 232-255, 24 different colors)			
			index = 232 + (int)(((double)(r + g + b)/765)*23); 
		}else{ //not grayscale. (indexes 16-231, 216 different colors)
			
			int rIndex = Math.round(((r / 255f) * 5));
			int gIndex = Math.round(((g / 255f) * 5));
			int bIndex = Math.round(((b / 255f) * 5));
			
			index = (rIndex * 36) + (gIndex * 6) + bIndex + 16;
			
		}
		return index;
	}
	
	public void writePalette() {
		
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		BufferedImage img2 = new BufferedImage(6, 36, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				img.setRGB(x, y, palette[y * 16 + x]);
			}
		}
		
		for (int y = 0; y < 36; y++) {
			for (int x = 0; x < 6; x++) {
				img2.setRGB(x, y, palette[16 + (y * 6 + x)]);
			}
		}
		
		try {
			ImageIO.write(img, "PNG", new File("resources/palette.png"));
			ImageIO.write(img2, "PNG", new File("resources/palette_sorted.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
