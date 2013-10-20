package org.wzj.memcached;

import java.util.Arrays;


/**
 * 
 * @author Wen
 *
 */
public class CachedData {

	public static final int MAX_SIZE = 2 * 1024 * 1024;

	private final int flags;
	private final byte[] data;

	public CachedData(int f, byte[] d ) {
		if (d.length > MAX_SIZE) {
			throw new IllegalArgumentException("Cannot cache data larger than "
					+ MAX_SIZE + " bytes (you tried to cache a " + d.length
					+ " byte object)");
		}
		flags = f;
		data = d;
	}

	public byte[] getData() {
		return data;
	}

	public int getFlags() {
		return flags;
	}

	@Override
	public String toString() {
		return "{CachedData flags=" + flags + " data=" + Arrays.toString(data)
				+ "}";
	}

}
