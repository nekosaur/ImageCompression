package mah.da357a.transforms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Filip Harald & Albert Kaaman
 *
 */
public class RunLengthEncode {

	/**
	 * Applies RLE to a byte sequence. Specifically on the binary sequence. Turned out to be not effective. (Even counter productive)
	 * Methods have no comments due too they are not being used.
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] apply(byte[] bytes) {
		List<BytePair> pairs = new LinkedList<>();

		int maxLength = 0;
		BytePair currentPair = new BytePair(bytes[0]);

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

		BitArray out = new BitArray((totalBits / 8) + (totalBits % 8 == 0 ? 0 : 1));

		/**
		 * This is not necessary since we have dynamic length
		 */
		// Write number of bits required for length (in 1 byte)
		//out.write(0, (byte)requiredBits);

		int bitPosition = 8;
		for (BytePair pair : pairs) {
			out.write(bitPosition, pair.b);

			bitPosition += 8;
			
			// Calculate how many bytes are needed for length.
			

			out.write(bitPosition, requiredBits, pair.length);

			bitPosition += requiredBits;
		}

		return out.toByteArray();
	}

	/*
	public static byte[] apply(byte[] bytes) {
		BitArray bits = new BitArray(bytes);
		List<Pair> pairs = new LinkedList<>();
		int maxLength = 0;
		Pair currentPair = new Pair(bits.getBit(0));
		int counter = 1;
		do {
			boolean bit = bits.getBit(counter);
			if (bit == currentPair.bit)
				currentPair.increment();
			else {
				maxLength = Math.max(maxLength, currentPair.length);
				pairs.add(currentPair);
				currentPair = new Pair(bit);
			}
			counter++;
		} while (counter < bits.getLength());
		double average = 0;
		int[] values = new int[pairs.size()];
		for (Pair pair : pairs) {
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
		int totalBits = 8 + pairs.size() + (pairs.size() * requiredBits);
		System.out.println("Total bits = " + totalBits);
		BitArray out = new BitArray(new byte[(totalBits / 8) + (totalBits % 8 == 0 ? 0 : 1)]);
		// Write number of bits required for length
		out.write(0, (byte)requiredBits);
		int bitPosition = 8;
		for (Pair pair : pairs) {
			out.setBit(bitPosition++, pair.bit);
			out.write(bitPosition, requiredBits, pair.length);
			bitPosition += requiredBits;
		}
		return out.toByteArray();
	}
	*/

	public static byte[] revert(byte[] bytes) {
		BitArray bits = new BitArray(bytes);

		int requiredBits = bits.readByte();

		System.out.println("Required bits = " + requiredBits);

		System.out.println("Total bit length = " + bits.getLength());

		byte[] out = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

			while (bits.getPosition() < bits.getLength()) {
				byte b = 0;
				try {
					b = bits.readByte();
				} catch (ArrayIndexOutOfBoundsException ex) {
					ex.printStackTrace();
				}

				/**
				 * TODO: Write this to allow dynamic length. Read a byte. if last bit is 1, continue 
				 * reading bytes, combine data and get length of pair.
				 */
				
				byte length = bits.readByte();


				
				
				for (int i = 0; i < length; i++) {
					bos.write(b);
				}

			}

			out = bos.toByteArray();
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

	private static class Pair {
		boolean bit;
		int length;

		public Pair(boolean bit) {
			this.bit = bit;
			this.length = 1;
		}

		public void increment() {
			length += 1;
		}
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