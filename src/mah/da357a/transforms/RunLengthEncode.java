package mah.da357a.transforms;

import java.util.LinkedList;
import java.util.List;

public class RunLengthEncode {

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

		maxLength = Math.max(maxLength, currentPair.length);
		pairs.add(currentPair);

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

	public static byte[] revert(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
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

}
