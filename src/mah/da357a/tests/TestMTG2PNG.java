package mah.da357a.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mah.da357a.io.MegatronIO;

public class TestMTG2PNG {

	public static void main(String[] args) {
		
		try {
			BufferedImage img = MegatronIO.read(new File("resources/cartoon.mtg"));
			
			ImageIO.write(img, "PNG", new File("resources/cartoon.png"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
