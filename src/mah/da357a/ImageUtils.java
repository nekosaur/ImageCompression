package mah.da357a;
import java.awt.image.BufferedImage;

/**This class includes some utilities used for modifying or calculating image and pixel-values.
 * @author nekosaur
 */
public class ImageUtils {

    private ImageUtils() {}

    /** returns the Image type of the int
     * @param type
     * @return
     */
    public static String typeToString(int type) {
        switch (type) {
            case BufferedImage.TYPE_INT_ARGB:
                return "INT_ARGB";
            case BufferedImage.TYPE_INT_ARGB_PRE:
                return "INT_ARGB_PRE";
            case BufferedImage.TYPE_INT_RGB:
                return "INT_RGB";
            case BufferedImage.TYPE_4BYTE_ABGR:
                return "4BYTE_ABGR";
            case BufferedImage.TYPE_BYTE_GRAY:
                return "BYTE_GRAY";
            case BufferedImage.TYPE_CUSTOM:
                return "CUSTOM";
            case BufferedImage.TYPE_BYTE_BINARY:
                return "BYTE_BINARY";
            case BufferedImage.TYPE_INT_BGR:
                return "BYTE_BGR";
            case BufferedImage.TYPE_3BYTE_BGR:
                return "3BYTE_BGR";
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                return "4BYTE_ABGR_PRE";
            case BufferedImage.TYPE_BYTE_INDEXED:
                return "BYTE_INDEXED";
            default:
                return "UNKNOWN";
        }
    }
    
    /**
     * Clamps the value so that values larger than max are set to max. And values smaller than min are set to min.
     *
     * @param value Value to clamp
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return The clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Returns the Euclidean distance between two RGB colors. The two colors are mapped to a three-dimensional
     * space (with the dimensions R, G and B) and the Euclidean distance is calculated.
     *
     * @param c1 First color
     * @param c2 Second color
     * @return Distance between colors
     */
    public static double distance(int c1, int c2) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        // Normal Euclidean distance
        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));

        // Algorithm taken from http://www.compuphase.com/cmetric.htm, presents a possibly more 'human' perception of color difference
        // and is cheaper to calculate than converting to another color space
        //double rMean = (r1 + r2) / (double)2;
        //return Math.sqrt(((int)((512 + rMean) * Math.pow(r1 - r2, 2)) >> 8) + 4 * Math.pow(g1 - g2, 2) + ((int)((767 - rMean) * Math.pow(b1 - b2, 2)) >> 8));
    }

    /**
     * Calculates the distance (delta E) between two RGB colors by first converting them into LAB color space, then calculates
     * then Euclidean distance. https://en.wikipedia.org/wiki/Color_difference
     *
     * @param c1 First color
     * @param c2 Second color
     * @return Value describing the distance between the two colors
     */
    public static double distanceInLAB(int c1, int c2) {

        double[] lab1 = XYZtoLAB(RGBtoXYZ(c1));
        double[] lab2 = XYZtoLAB(RGBtoXYZ(c2));

        return Math.sqrt(Math.pow(lab1[0] - lab2[0], 2) + Math.pow(lab1[1] - lab2[1], 2) + Math.pow(lab1[2] - lab2[2], 2));
    }

    /**
     * Converts RGB color to XYZ color space. Code from http://www.easyrgb.com/index.php?X=MATH
     *
     * @param c RGB color
     * @return XYZ color
     */
    private static double[] RGBtoXYZ(int c) {
        double r = ((c >> 16) & 0xFF) / (double)255;
        double g = ((c >> 8) & 0xFF) / (double)255;
        double b = (c & 0xFF) / (double)255;

        r = r > 0.04045 ? Math.pow(((r + 0.055) / 1.055), 2.4) : r / 12.92;
        g = g > 0.04045 ? Math.pow(((g + 0.055) / 1.055), 2.4) : g / 12.92;
        b = b > 0.04045 ? Math.pow(((b + 0.055) / 1.055), 2.4) : b / 12.92;

        r *= 100;
        g *= 100;
        b *= 100;

        double[] xyz = new double[3];

        xyz[0] = r * 0.4124 + g * 0.3576 + b * 0.1805;
        xyz[1] = r * 0.2126 + g * 0.7152 + b * 0.0722;
        xyz[2] = r * 0.0193 + g * 0.1192 + b * 0.9505;

        return xyz;
    }

    private static final double refX = 95.047;
    private static final double refY = 100.00;
    private static final double refZ = 108.883;

    /**
     * Converts XYZ color to LAB color space. Code from http://www.easyrgb.com/index.php?X=MATH
     * @param xyz XYZ color
     * @return LAB color
     */
    private static double[] XYZtoLAB(double[] xyz) {
        double x = xyz[0]  / refX;
        double y = xyz[1]  / refY;
        double z = xyz[2]  / refZ;

        x = x > 0.00856 ? Math.pow(x, (double)1/3) : (7.787 * x) + ((double)16 / 116);
        y = y > 0.00856 ? Math.pow(y, (double)1/3) : (7.787 * y) + ((double)16 / 116);
        z = z > 0.00856 ? Math.pow(z, (double)1/3) : (7.787 * z) + ((double)16 / 116);

        double[] lab = new double[3];

        lab[0] = (116 * y) - 16;
        lab[1] = 500 * (x - y);
        lab[2] = 200 * (y - z);

        return lab;

    }

    /**
     * Returns the average ARGB-value between two specified ARGB-values
     * 
     * @param c1 first ARGB-value
     * @param c2 second ARGB-value
     * @return the average ARGB-value
     */
    public static int average(int c1, int c2) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        return ImageUtils.toInt((r1+r2)/2, (g1+g2)/2, (b1+b2)/2);
    }

    /**
     * Returns an ARGB-value of the given values. A is set to 255.
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return ARGB color value stored in a single integer
     */
    public static int toInt(int r, int g, int b) {
        int argb = 0;

        argb += 0xFF << 24;
        argb += (r & 0xFF) << 16;
        argb += (g & 0xFF) << 8;
        argb += b & 0xFF;

        return argb;
    }

    public static String toString(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        return String.format("Color[%d, %d, %d, %d]", a, r, g, b);
    }
}
