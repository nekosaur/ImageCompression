package mah.da357a.io;

import mah.da357a.transforms.MoveToFront;
import mah.da357a.transforms.RunLengthEncode;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

            // Write width and length
            write4bytes(img.getWidth(), out);
            write4bytes(img.getHeight(), out);

            byte[] array = compress(img);

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
    
    /**
     * @param image
     * @return
     */
    private static byte[] compress(BufferedImage image){
        byte[] array = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

        array = new MoveToFront().apply(array);


        return array;
    }
    
    /**
     * @param bytes
     * @return
     */
    private static BufferedImage decompress(byte[] bytes){
    	return null;
    }

    /**
     * Writes an int as 4 bytes, big endian.
     *
     * @param v Int to write
     * @param out Stream to write int to
     * @throws IOException
     */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write((v >>> 3*8));
        out.write((v >>> 2*8) & 0xFF);
        out.write((v >>> 1*8) & 0xFF);
        out.write((v >>> 0*8) & 0xFF);
    }
}
