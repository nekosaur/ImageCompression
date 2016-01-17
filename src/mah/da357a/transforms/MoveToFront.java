package mah.da357a.transforms;

/**
 * Move-to-Front transform, used to MTF-encode the byte sequence from RLE, specifically on the binary sequence. Turned out to be not effective. (Even counter productive)
 * Methods have no comments due to not being used at the moment.
 *
 * @author Filip Harald & Albert Kaaman
 */
public class MoveToFront {

	public static byte[] apply(byte[] bytes) {

		byte[] table = new byte[256];
		for (int i = 0; i < 256; i++)
			table[i] = (byte)(i);

		byte[] out = new byte[bytes.length];

		for (int i = 0; i < bytes.length; i++) {

			byte b = bytes[i];

			int rank = find(table, b);

			out[i] = (byte)rank;

			if (rank > 0) {
				table = update(table, rank);
			}
		}

		return out;
	}

	private static byte[] update(byte[] table, int rank) {
		byte b = table[rank];
		for (int i = rank; i > 0; i--) {
			table[i] = table[i - 1];
		}
		table[0] = b;

		return table;
	}

	private static int find(byte[] table, byte entry) {
		for (int i = 0; i < table.length; i++)
			if (table[i] == entry)
				return i;

		return -1;
	}

	public static byte[] revert(byte[] bytes) {
		byte[] table = new byte[256];
		for (int i = 0; i < 256; i++)
			table[i] = (byte)i;

		byte[] out = new byte[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			int rank = bytes[i] & 0xFF;

			out[i] = table[rank];

			table = update(table, rank);
		}

		return out;
	}

}