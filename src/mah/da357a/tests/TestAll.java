package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import mah.da357a.io.CompressIO;
import mah.da357a.io.MegatronIO;

public class TestAll {

	public static void main(String[] args) {
		
		String[] files = new String[] {"cartoon", "green_boat", "yellow_flower"};
		
		for (String file : files) {
			long time = 0;
			try {
				System.out.println("reading " + file);
				BufferedImage img = MegatronIO.read(new File("resources/" + file + ".mtg"));
				
				System.out.println("compressing " + file);
				time = System.nanoTime();
				CompressIO.write(img, new File("C:/Users/ae8556/Documents/resources/" + file + ".cif"));
				System.out.println(TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - time));
				
				System.out.println("decompressing " + file);
				time = System.nanoTime();
				img = CompressIO.read(new File("C:/Users/ae8556/Documents/resources/" + file + ".cif"));
				ImageIO.write(img, "PNG", new File("C:/Users/ae8556/Documents/resources/" + file + ".png"));
				System.out.println(TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - time));
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
	
}
