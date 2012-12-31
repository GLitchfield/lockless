package com.glitchfield.lockless;

public interface LocklessCounter {

	public int AIncrementAndGet(final int a);
	public int BIncrementAndGet(final int b);
	
}
