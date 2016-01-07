package mah.da357a.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO class for reading and writing compressed image files
 * 
 * @author Albert Kaaman & Filip Harald
 */
public class CompressIO {

    private final static byte[] HEADER = "QWERTY".getBytes();

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
    	/*
    	int width  = img.getWidth();
        int height = img.getHeight();
        int[] pxl = new int[3];
        Raster imgRaster  = img.getRaster();
        */
        
        try (OutputStream out = new FileOutputStream(outputFile)) {

	        // Write compress header
	        out.write(HEADER);

        }
        
        return true;
    }

    /**
     * Reads an image from file on disk.
     * 
     * @param inputFile File to read from
     * @return Image read from file
     */
    public static BufferedImage read(File inputFile) throws IOException {
    	BufferedImage img = null;
        
        try (InputStream in = new FileInputStream(inputFile)) {
	        // Check magic value.
	        for (int i = 0; i < HEADER.length; i++) {
	            if (in.read() != HEADER[i]) { throw new InvalidCompressFileException(); }
	        }
	        
        }
        
        return img;
    }
}
