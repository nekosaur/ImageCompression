package mah.da357a.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * IO class for reading and writing compressed image files
 * 
 * @author Albert Kaaman & Filip Harald
 */
public class CompressIO {

    private final byte[] HEADER = "QWERTY".getBytes();

    @SuppressWarnings("serial")
    public final static class InvalidCompressFileException extends IOException { }

    /**
     * Writes image to disk.
     * 
     * @param img Image to write
     * @param outputFile File to write image to
     * @return True if success
     */
    public static boolean write(BufferedImage img, File outputFile) throws IOException {
        return false;
    }

    /**
     * Reads an image from file on disk.
     * 
     * @param inputFile File to read from
     * @return Image read from file
     */
    public static BufferedImage read(File inputFile) throws IOException {
        return null;
    }
}
