package mah.da357a.transforms;

public interface Transform {
	
	/**
	 * Transforms the bytes using the transform algorithm
	 * 
	 * @param bytes
	 * @return Returns a copy of the transformed bytes. 
	 */
	byte[] apply(byte[] bytes);
	
	/**
	 * Reverts the given bytes already transformed by this transform
	 * 
	 * @param bytes The transformed bytes
	 * @return Returns a copy of the reverted transform bytes.
	 */
	byte[] revert(byte[] bytes);
}
