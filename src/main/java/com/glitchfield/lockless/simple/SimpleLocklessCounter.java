package com.glitchfield.lockless.simple;

import com.glitchfield.lockless.LocklessCounter;

public class SimpleLocklessCounter implements LocklessCounter {

	private volatile int A = 0;
	private volatile int B = 0;
	
	@Override
	public int AIncrementAndGet(final int a) {
		
		A = a;
		A = A;
		
		
		return a + B;
		
	}
	
	@Override
	public int BIncrementAndGet(final int b) {
		
		B = b;
		B = B;
		
		return A + b;
	}
	
}
