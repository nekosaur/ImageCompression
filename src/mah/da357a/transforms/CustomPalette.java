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
		
		int[] p = new int[3];
		int color;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				
				p = raster.getPixel(x, y, p);
				
				color = ImageUtils.toInt(p[0], p[1], p[2]);
				
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
		
		palette[0] = sorted.get(0);
		int counter = 1;
		boolean add = false;
		for(int i = 0; i < 16; i++){
			palette[i] = 0;
		}
		for (int i = 16; i < sorted.size() && counter < 232; i++) {
			//System.out.println(counter);
			add = false;
			
			for (int j = 0; j < counter; j++) {
				if (ImageUtils.distanceInLAB(palette[j], sorted.get(i)) < 3) {
				//if (ImageUtils.distance(palette[j], sorted.get(i)) < 4) {
					add = false;
					break;
				}else{					
					add = true;
				}
			}
			
			if (add)
				palette[counter++] = sorted.get(i);
			
			
		}
		
		palette[232] = 0x000000;
		palette[233] = 0x080808;
		palette[255] = 0xFFFFFF;
		int intensity = 8;
		for (int i = 234; i < 255; i++) {
			intensity += 10;
			palette[i] = ImageUtils.toInt(intensity, intensity, intensity);
		}
		
		sort();
		writePalette();
		
	}
	
	/**
	 * This changes the acutual palatte
	 */
	private void sort() {
		int[] sortedRed = sortPaletteRed();
		int[] sortedGreen = sortPaletteGreen(sortedRed);
		int[] sortedBlue = sortPaletteBlue(sortedGreen);
		for(int i = 16; i < paletteColorSize; i++){
			palette[i] = sortedBlue[i-16];
		}
	}


	private int[] sortPaletteRed() {
		int[] sorted = new int[paletteColorSize];
		for(int i = 0; i < paletteColorSize; i++){
			sorted[i] = palette[i+16]; 
		}
		sorted = Arrays.stream(sorted)
					.boxed()
					.sorted((a, b) -> {
						return ((a >> 16) & 0xFF) - ((b >> 16) & 0xFF);
					})
					.mapToInt(i -> i)
					.toArray();
		return sorted;
	}
	
	private int[] sortPaletteGreen(int[] sortedRed) {
		int[] sorted = sortedRed.clone();
		for(int j = 0; j < sorted.length; j += 36){
			int[] tempSort = Arrays.copyOfRange(sorted, j, j+35);
			tempSort = Arrays.stream(tempSort)
					.boxed()
					.sorted((a, b) -> {
						return ((a >> 8) & 0xFF) - ((b >> 8) & 0xFF);
					})
					.mapToInt(i -> i)
					.toArray();
			for(int k = 0; k < tempSort.length; k++){
				sorted[j+k] = tempSort[k];
			}
		}
		
		return sorted;
	}

	private int[] sortPaletteBlue(int[] sortedGreen) {
		int[] sorted = sortedGreen.clone();
		for(int j = 0; j < sorted.length; j += 6){
			int[] tempSort = Arrays.copyOfRange(sorted, j, j+5);
			tempSort = Arrays.stream(tempSort)
					.boxed()
					.sorted((a, b) -> {
						return ((a) & 0xFF) - ((b) & 0xFF);
					})
					.mapToInt(i -> i)
					.toArray();
			for(int k = 0; k < tempSort.length; k++){
				sorted[j+k] = tempSort[k];
			}
		}
		return sorted;
	}
	
	public int getColor(int index){
		return palette[index];
	}
	
	public int[] getPalette() {
		return palette;
	}
	
	public int getCorrespondingColorIndex(int r, int g, int b) {
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
		
		return index;
	}
	
	public void writePalette() {
		
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				img.setRGB(x, y, palette[y * 16 + x]);
			}
		}
		
		try {
			ImageIO.write(img, "PNG", new File("resources/palette.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
