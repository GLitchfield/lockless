package com.glitchfield.lockless;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitTesting {

	public static void main(final String[] args) {
		
		long test = 0;
		System.out.println(test + " = " + Arrays.toString(ByteBuffer.allocate(8).putLong(test).array()));
		test = test | 2;
		System.out.println(test + " = " + Arrays.toString(ByteBuffer.allocate(8).putLong(test).array()));
		test = test | 4;
		System.out.println(test + " = " + Arrays.toString(ByteBuffer.allocate(8).putLong(test).array()));
		
	}
	
}
