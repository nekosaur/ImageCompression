package mah.da357a.transforms;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Compression transform that takes a byte stream and performs run length encoding on a byte level (alphabet = 0-255)
 *
 * Not used right now since compression savings are slim to none on photographic images. Cartoon was halved in size though.
 *
 * @author Filip Harald & Albert Kaaman
 */
public class RunLengthEncode {

	public static byte[] apply(byte[] bytes) {
		List<BytePair> pairs = new LinkedList<>();

		int maxLength = 0;
		BytePair currentPair = new BytePair(bytes[0]);

		/**
		 * Saves byte data as 1 byte value, variable byte length. Uses first 7 bits of every byte. If 8th bit is set
		 * it means another length byte will follow. Allows for 1 byte length data for lengths (1-128).
		 */
		int counter = 1;
		do {

			byte b = bytes[counter];

			if (b == currentPair.b)
				currentPair.increment();
			else {
				maxLength = Math.max(maxLength, currentPair.length);
				pairs.add(currentPair);
				currentPair = new BytePair(b);
			}

			counter++;

		} while (counter < bytes.length);

		double average = 0;
		int[] values = new int[pairs.size()];
		for (BytePair pair : pairs) {
			average += pair.length;
		}
		average = average / pairs.size();
		System.out.println("average = " + average);

		System.out.println("median = " + pairs.stream().sorted((p1, p2) -> p1.length - p2.length).skip(pairs.size()/2).findFirst().get().length);

		maxLength = Math.max(maxLength, currentPair.length);
		pairs.add(currentPair);

		System.out.println("Pairs = " + pairs.size());

		System.out.println("Max pair length = " + maxLength);

		int requiredBits = calculateRequiredBits(maxLength);

		System.out.println("Required bits = " + requiredBits);

		int totalBits = 8 + (pairs.size() * 8) + (pairs.size() * requiredBits);

		System.out.println("Total bits = " + totalBits);

		//BitArray out = new BitArray((totalBits / 8) + (totalBits % 8 == 0 ? 0 : 1));

		byte[] out = null;
		try {
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                for (BytePair pair : pairs) {
					//out.write(bitPosition, pair.b);
					bos.write(pair.b);

					//bitPosition += 8;

					// Calculate how many bytes are needed for length.
					int length = pair.length;
					double requiredBytes;
					while (length > 0) {
						requiredBytes = calculateRequiredBits(length) / (double) 7;

						if (requiredBytes <= 1) {
							bos.write(length);
							break;
						} else {
							int v = pair.length & 0x7F;
							bos.write(length | 0x80);

							length = length >>> 7;
						}

					}

                    //out.write(bitPosition, requiredBits, pair.length);

                    //bitPosition += requiredBits;
                }

				out = bos.toByteArray();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out;
	}

	public static byte[] revert(byte[] bytes) {
		ByteArray bits = new ByteArray(bytes);

		System.out.println("Total bit length = " + bits.getLength());

		byte[] out = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length * 2)) {
			while (bits.getPosition() < bits.getLength()) {
				byte b = 0;
				try {
					b = bits.readByte();
				} catch (ArrayIndexOutOfBoundsException ex) {
					ex.printStackTrace();
				}

				/**
				 * Variable byte length. Uses first 7 bits of every byte. If 8th bit is set
				 * it means another length byte will follow. Allows for 1 byte length data for lengths (1-128).
				 */
				int length = 0, shift = 0;
				byte tmp;
				do {
					tmp = bits.readByte();

					length |= (tmp & 0x7F) << (7*shift);
					//length = length << 7;

					shift++;
				} while (((tmp >>> 7) & 0x1) == 1);

				for (int i = 0; i < length; i++) {
					bos.write(b);
				}

			}

			out = bos.toByteArray();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out;
	}

	private static int calculateRequiredBits(int maxLength) {
		int count = 0;
		while (maxLength > 0) {
			count++;
			maxLength = maxLength >>> 1;
		}
		return count;
	}

	private static class BytePair {
		byte b;
		int length;

		public BytePair(byte b) {
			this.b = b;
			this.length = 1;
		}

		public void increment() {
			length += 1;
		}
	}

}