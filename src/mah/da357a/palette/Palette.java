package mah.da357a.palette;

/**
 * Palette interface.
 *
 * @author Albert Kaaman
 */
public interface Palette {
    int[] getPalette();
    int getColor(int index);
    int getIndex(int r, int g, int b);
}
