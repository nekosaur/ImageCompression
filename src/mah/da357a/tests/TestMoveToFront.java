package mah.da357a.tests;

import mah.da357a.transforms.MoveToFront;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * NOTE TO SELF! Make sure -ea JVM option is enabled!
 *
 * @author Albert Kaaman
 */
public class TestMoveToFront {

    public static void main(String[] args) {

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("resources/cartoon.png"));

            byte[] bytes = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();

            byte[] mtf = MoveToFront.apply(bytes);
            mtf = MoveToFront.revert(mtf);

            TestUtils.assertArrays(bytes, mtf);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
