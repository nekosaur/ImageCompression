package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mah.da357a.io.MegatronIO;

public class TestPNG2MTG {

	public static void main(String[] args) {
			
		try {
			BufferedImage img = ImageIO.read(new File("resources/gradient.png"));
			
			MegatronIO.write(img, new File("resources/gradient.mtg"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}