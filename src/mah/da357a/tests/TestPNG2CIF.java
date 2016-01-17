package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mah.da357a.io.CompressIO;

public class TestPNG2CIF {
	public static void main(String[] args) {
		String file = "cartoon";
		try {
			BufferedImage img = ImageIO.read(new File("resources/" + file + ".png"));

			CompressIO.write(img, new File("e:/tmp/resources/" + file + ".cif"));
			
			img = CompressIO.read(new File("e:/tmp/resources/" + file + ".cif"));
			
			ImageIO.write(img, "PNG", new File("e:/tmp/resources/" + file + ".png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
