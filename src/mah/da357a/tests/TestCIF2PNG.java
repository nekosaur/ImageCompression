package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mah.da357a.io.CompressIO;

public class TestCIF2PNG {

	public static void main(String[] args) {
			
		try {
			BufferedImage img = CompressIO.read(new File("resources/test/tmp.cif"));
			
			ImageIO.write(img, "PNG", new File("resources/output/tmp.png"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
