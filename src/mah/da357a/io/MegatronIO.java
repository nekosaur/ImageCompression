package mah.da357a.io;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * IO class for reading and writing Megatron files
 * 
 * @author Jesper Larsson & Albert Kaaman
 *
 */
public class MegatronIO {
    /** Magic start string to signify Megatron file format. */
    private final static byte[] MAGIC = "mEgaMADNZ!".getBytes(StandardCharsets.US_ASCII);

    @SuppressWarnings("serial")
	public final static class InvalidMegatronFileException extends IOException { }

    /**
     * Writes an image to disk
     * @param img Image to write
     * @param outputFile File to write image to
     * @throws IOException
     * @return True if success
     */
    public static boolean write(BufferedImage img, File outputFile) throws IOException {
        int width  = img.getWidth();
        int height = img.getHeight();
        int[] pxl = new int[3];
        Raster imgRaster  = img.getRaster();
        
        try (OutputStream out = new FileOutputStream(outputFile)) {
        
	        // Write megatron header
	        out.write(MAGIC);
	
	        // Write width and length
	        write4bytes(width, out);
	        write4bytes(height, out);
	
	        // Write pixel data
	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < height; j++) {
	                imgRaster.getPixel(i, j, pxl);
	                out.write(pxl[0]);
	                out.write(pxl[1]);
	                out.write(pxl[2]);
	            }
	        }
	        
        }
        
        return true;
    }

    /**
     * Reads an image from file on disk
     * 
     * @param inputFile File to read image from
     * @return Image read from file
     * @throws IOException
     */
    public static BufferedImage read(File inputFile) throws IOException {
        BufferedImage img = null;
        
        try (InputStream in = new FileInputStream(inputFile)) {
	        // Check magic value.
	        for (int i = 0; i < MAGIC.length; i++) {
	            if (in.read() != MAGIC[i]) { throw new InvalidMegatronFileException(); }
	        }
	
	        // Read width and height
	        int width  = read4bytes(in);
	        int height = read4bytes(in);
	
	        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        byte[] pxlBytes = new byte[3];
	        int[] pxl = new int[3];
	        WritableRaster imgRaster = img.getRaster();
	
	        // Read pixel data
	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < height; j++) {
	                if (in.read(pxlBytes) != 3) { throw new EOFException(); }
	                pxl[0] = pxlBytes[0];
	                pxl[1] = pxlBytes[1];
	                pxl[2] = pxlBytes[2];
	                imgRaster.setPixel(i, j, pxl);
	            }
	        }
        
        } catch (Exception ex) {
        	ex.printStackTrace();
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


