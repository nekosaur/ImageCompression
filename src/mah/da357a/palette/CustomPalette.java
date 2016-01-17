package mah.da357a.palette;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import mah.da357a.ImageUtils;
import mah.da357a.transforms.ByteArray;

/**
 * Implements Palette interface as a custom palette, generated from pixel information in source image
 *
 * @author Albert Kaaman & Filip Harald
 */
public class CustomPalette implements Palette {

	private final static int PALETTE_SIZE = 256;
	private final static int LOOKUP_SIZE = 32;

	public int[] palette = new int[PALETTE_SIZE];
	public int[] lookup = new int[LOOKUP_SIZE*LOOKUP_SIZE*LOOKUP_SIZE];

	/**
	 * Constructs an instance of CustomPalette using byte data from compressed image
	 * @param bytes Palette byte data
     */
	public CustomPalette(byte[] bytes) {
		
		ByteArray bits = new ByteArray(bytes);
		
		int counter = 0;
		for (int i = 0; i < bytes.length; i += 4) {
			
			palette[counter++] = bits.readInt();
			
		}
		
		writePalette();
		
	}

	/**
	 * Constructs an instance of CustomPalette using pixel data from source image
	 * @param img Source image
     */
	public CustomPalette(BufferedImage img) {
		
		Map<Integer, Integer> map = new HashMap<>();
		Raster raster = img.getRaster();


		// First we count all unique pixels and save the amount to hashmap
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

		// Then we sort the unique pixels, most common comes first
		List<Integer> sorted = new LinkedList<>();
		for (Map.Entry<Integer, Integer> entry : map.entrySet())
			sorted.add(entry.getKey());

		sorted.sort((c1, c2) -> map.get(c2) - map.get(c1));

		/**
		 * Then we create a custom palette by adding a color value to the palette
		 * as long as the color is not too close to all other previously added colors.
		 */
		palette[1] = sorted.get(0);
		int counter = 1;
		boolean add = false;
		double delta = 3;
		while (counter < PALETTE_SIZE) {
			for (int i = counter; i < sorted.size() && counter < PALETTE_SIZE; i++) {
				add = false;
				
				for (int j = 0; j < counter; j++) {
					if (ImageUtils.distanceInLAB(palette[j], sorted.get(i)) < delta) {
					//if (ImageUtils.distance(palette[j], sorted.get(i)) < 4) {
						add = false;
						break;
					} else {
						add = true;
					}
				}
				
				if (add)
					palette[counter++] = sorted.get(i) & 0xFFFFFF;

			}
			if (counter < 256)
				delta -= 0.5;
		}

		// We're no longer dedicating last 24 palette colors to grey values
		/*
		palette[232] = 0x000000;
		palette[233] = 0x080808;
		palette[255] = 0xFFFFFF;
		int intensity = 8;
		for (int i = 234; i < 255; i++) {
			intensity += 10;
			palette[i] = ImageUtils.toInt(intensity, intensity, intensity) & 0xFFFFFF;
		}*/

		// Debug, writes palette to resource folders
		//writePalette();

		/**
		 * Here we create a lookup table (5-bit/color RGB cube space) to use
		 * when assigning colors from source image to palette indices
		 */
		double maxDistance, d;
		int r, g, b, c, pindex;
		for (int i = 0; i < lookup.length; i++) {
			r = i / (LOOKUP_SIZE * LOOKUP_SIZE);
			g = (i / LOOKUP_SIZE) % LOOKUP_SIZE;
			b = i % LOOKUP_SIZE;
			
			c = ImageUtils.toInt(r*8, g*8, b*8) & 0xFFFFFF;
			
			maxDistance = Double.MAX_VALUE;
			pindex = -1;
			for (int p = 0; p < palette.length; p++) {
				d = ImageUtils.distance(c, palette[p]);
				if (d < maxDistance) {
					maxDistance = d;
					pindex = p;
				}
			}
			
			lookup[i] = pindex;
		}

	}

	/**
	 * Returns palette index for supplied color values
	 * @param r Red value
	 * @param g Green value
	 * @param b Blue value
     * @return Palette index
     */
	public int getIndex(int r, int g, int b) {
		// Truncate 8-bit color value to 5-bit
		r = (r >>> 3);
		g = (g >>> 3);
		b = (b >>> 3);

		// Combine values to 15-bit color value used to find palette index in lookup table
		int tindex = (r << 10) | (g << 5) | b;
		//int tindex = b + (g*32) + (r * 32 * 32);

		return lookup[tindex];
	}

	/**
	 * Returns palette color
	 * @param index Index of palette color
	 * @return Palette color
     */
	public int getColor(int index){
		return palette[index];
	}

	/**
	 * @return Palette
     */
	public int[] getPalette() {
		return palette;
	}

	/**
	 * Writes the palette as a 16x16 image to resource folder.
	 */
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
