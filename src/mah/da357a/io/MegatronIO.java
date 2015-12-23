package mah.da357a.io;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.imageio.*;

public class MegatronIO {
    /** Magic start string to signify Megatron file format. */
    final static byte[] magic = "mEgaMADNZ!".getBytes(StandardCharsets.US_ASCII);

    public final static class InvalidMegatronFileException extends IOException { }

    /**
     *
     * @param img
     * @param outputFile
     * @throws IOException
     */
    public static void write(BufferedImage img, File outputFile) throws IOException {
        int width  = img.getWidth();
        int height = img.getHeight();
        int[] pxl = new int[3];
        Raster imgRaster  = img.getRaster();
        OutputStream out = new FileOutputStream(outputFile);

        // Write megatron header
        out.write(magic);

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
        out.close();
    }

    /**
     *
     * @param inputFile
     * @return
     * @throws IOException
     */
    public static BufferedImage read(File inputFile) throws IOException {
        InputStream in = new FileInputStream(inputFile);

        // Check magic value.
        for (int i = 0; i < magic.length; i++) {
            if (in.read() != magic[i]) { throw new InvalidMegatronFileException(); }
        }

        // Read width and height
        int width  = read4bytes(in);
        int height = read4bytes(in);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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
        in.close();

        return img;
    }
      
    /**
     * Writes an int as 4 bytes, big endian.
     * @param v
     * @param out
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
     * @param in
     * @return
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


