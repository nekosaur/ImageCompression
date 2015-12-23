package mah.da357a;

import mah.da357a.io.CompressIO;
import mah.da357a.io.MegatronIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nekosaur
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            showHelp();
            System.exit(1);
        }

        BufferedImage img;
        File input = null, output = null;

        try {
            input = new File(args[1]);
            output = new File(args[2]);
        } catch (NullPointerException ex) {
            showHelp();
            System.exit(1);
        }

        switch (args[0]) {
            case "mtg2png":
                img = ImageIO.read(input);
                MegatronIO.write(img, output);
                break;
            case "mtg2cif":
                img = MegatronIO.read(input);
                CompressIO.write(img, output);
                break;
            case "cif2png":
                img = CompressIO.read(input);
                ImageIO.write(img, "PNG", output);
                break;
            case "cif2mtg":
                img = CompressIO.read(input);
                MegatronIO.write(img, output);
                break;
            case "png2mtg":
                img = ImageIO.read(input);
                MegatronIO.write(img, output);
                break;
            case "png2cif":
                img = ImageIO.read(input);
                CompressIO.write(img, output);
                break;
            default:
                showHelp();
        }

    }

    public static void showHelp() {

        String help = "Expected arguments: <command> <input> <output>\n"
                    + "\n"
                    + "Available commands:\n"
                    + "mtg2png : Converts MTG file to PNG\n"
                    + "mtg2cif : Converts MTG file to CIF\n"
                    + "cif2png : Converts CIF file to PNG\n"
                    + "cif2mtg : Converts CIF file to MTG\n"
                    + "png2cif : Converts PNG file to CIF\n"
                    + "png2mtg : Converts PNG file to MTG\n"
                    + "\n"
                    + "Both input and output are paths relative to current directory"
                    + "\n"
                    + "Have fun!";


        System.err.println(help);

    }

}
