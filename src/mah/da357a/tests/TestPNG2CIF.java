package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mah.da357a.io.CompressIO;

public class TestPNG2CIF {
	public static void main(String[] args) {
		
		try {
			BufferedImage img = ImageIO.read(new File("resources/test/tmp.png"));
			
			CompressIO.write(img, new File("resources/output/tmp.cif"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
