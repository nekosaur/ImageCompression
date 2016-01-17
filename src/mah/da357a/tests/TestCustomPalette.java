package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import mah.da357a.ImageUtils;
import mah.da357a.transforms.CustomPalette;

public class TestCustomPalette {

	public static void main(String[] args) {
		
		
		
		try {
			BufferedImage img = ImageIO.read(new File("resources/cartoon.png"));
			
			CustomPalette p = new CustomPalette(img);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*
		Map<Integer, Integer> map = new HashMap<>();
		
		int[][] img = new int[][] {
			{ 12352, 4212, -613429 },
			{ 354212, -88912, 354212 },
			{ 12352, 354212, -613429 }
		};
		
		int[] p = new int[3];
		int color;
		for (int x = 0; x < img[0].length; x++) {
			for (int y = 0; y < img.length; y++) {
							
				color = img[y][x];
				
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
		
		System.out.println(sorted);
		*/
	}
}
