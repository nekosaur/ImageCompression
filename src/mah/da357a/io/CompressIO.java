package mah.da357a.io;

import javafx.scene.shape.MoveTo;
import mah.da357a.transforms.LimitPalette;
import mah.da357a.transforms.MoveToFront;
import mah.da357a.transforms.RunLengthEncode;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
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

        System.out.println(img.getWidth());
        System.out.println(img.getHeight());
        
        try (OutputStream out = new FileOutputStream(outputFile)) {

	        // Write compress header
	        out.write(HEADER);

            // Write width and length
            write4bytes(img.getWidth(), out);
            write4bytes(img.getHeight(), out);

            byte[] array = compress(img);

            for (int i = 0; i < array.length; i++) {
                out.write(array[i]);
            }

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
	        
	        // Read width and height
	        int width  = read4bytes(in);
	        int height = read4bytes(in);
	        
	        byte[] buffer = new byte[4096];
	        
	        byte[] data = null;
	        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
	        	
	        	int length = -1;
	        	while ((length = in.read(buffer)) != -1) {
	        		bos.write(buffer, 0, length);
	        	}
	        	
	        	data = bos.toByteArray();
	        	
	        }
	        
	        img = decompress(width, height, data);
	        
        }
        
        return img;
    }
    
    /**
     * @param image
     * @return
     */
    private static byte[] compress(BufferedImage image){

        byte[] bytes = LimitPalette.apply(image);

        /*
        bytes = MoveToFront.apply(bytes);
        bytes = RunLengthEncode.apply(bytes);
        */

        return bytes;
    }
    
    /**
     * @param bytes
     * @return
     */
    private static BufferedImage decompress(int width, int height, byte[] bytes){
    	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	WritableRaster raster = img.getRaster();
    	
    	System.out.println("Bytes (compressed) = " + bytes.length);

        /*
        bytes = RunLengthEncode.revert(bytes);
        bytes = MoveToFront.revert(bytes);
        */

        bytes = LimitPalette.revert(bytes);

    	System.out.println("Bytes (decompressed) = " + bytes.length);
    	System.out.println("Pixels (3bytes/p) = " + (bytes.length / 3));
    	
    	int i = 0;

    	for (int y = 0; y < height; y++) {
    		for (int x = 0; x < width; x++) {
    			raster.setPixel(x, y, new int[] {bytes[i] & 0xFF, bytes[i + 1] & 0xFF, bytes[i + 2] & 0xFF});
    			
    			i += 3;
    		}
    	}
    	
    	return img;
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
    
    /**
     * Reads an int as 4 bytes, big endian.
     * 
     * @param in Stream to read from
     * @return Int read from stream
     * @throws IOException
     */
    private static int read4bytes(InputStream in) throws IOException {
        int b, v = 0;
        /*
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v = b << 3*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b << 2*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b << 1*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b;
        */

        for (int i = 3; i >= 0; i--) {
            b = in.read();
            if (b < 0)  { throw new EOFException(); }
            v |= b << i*8;
        }

        return v;
    }
}
