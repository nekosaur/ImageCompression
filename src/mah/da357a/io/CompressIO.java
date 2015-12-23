package mah.da357a.io;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author nekosaur
 */
public class CompressIO {

    private final byte[] header = "QWERTY".getBytes();

    public final static class InvalidCompressFileException extends IOException { }

    /**
     *
     * @param img
     * @param outputFile
     * @return
     */
    public static boolean write(BufferedImage img, File outputFile) throws IOException {
        return false;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    public static BufferedImage read(File inputFile) throws IOException {
        return null;
    }
}
