package mah.da357a.transforms;

/**
 * Move-to-Front transform
 *
 * @author Filip Harald & Albert Kaaman
 */
public class MoveToFront {

	public byte[] apply(byte[] bytes) {

		BitArray bits = new BitArray(bytes);

		/*
		for (int i = 0; i < bits.getLength(); i++)
			System.out.print(bits.getBit(i) == true ? 1 : 0);
		System.out.println();
		*/

		boolean zeroIsFront = true;

		for (int i = 0; i < bits.getLength(); i++) {

			boolean bit = bits.getBit(i);

			bits.setBit(i, zeroIsFront ? bit : !bit);

			zeroIsFront = zeroIsFront ? !bit : true;

		}

		/*
		for (int i = 0; i < bits.getLength(); i++)
			System.out.print(bits.getBit(i) == true ? 1 : 0);
		System.out.println();
		*/

		return bits.toByteArray();

	}

	public byte[] revert(byte[] bytes) {
		return apply(bytes);
	}

}
