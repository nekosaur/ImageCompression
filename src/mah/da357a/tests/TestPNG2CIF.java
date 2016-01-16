package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mah.da357a.io.CompressIO;

public class TestPNG2CIF {
	public static void main(String[] args) {
		
		try {
			BufferedImage img = ImageIO.read(new File("resources/cartoon.png"));

			System.out.println(img.getType());
			System.out.println();
			
			CompressIO.write(img, new File("c:/users/ae6662/documents/resources/cartoon.cif"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
