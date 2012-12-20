package com.glitchfield.lockless;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class LocklessCounter {
	
private int SIZE = 100;
	
	private volatile int A = 0;
	private volatile boolean Bworking = false;
	
	volatile int spinCounter = 0;
	
	volatile AtomicIntegerArray BB;
	volatile AtomicIntegerArray result;
	
	public LocklessCounter() {
		BB = new AtomicIntegerArray(SIZE*2);
		result = new AtomicIntegerArray(SIZE*2);
		for(int i = 0; i < SIZE*2; i++) {
			BB.set(i, 0);
			result.set(i, 0);
		}
	}
	
	public LocklessCounter(final int size) {
		SIZE = size;
		BB = new AtomicIntegerArray(SIZE*2);
		result = new AtomicIntegerArray(SIZE*2);
		for(int i = 0; i < SIZE*2; i++) {
			BB.set(i, 0);
			result.set(i, 0);
		}
	}
	
	public synchronized void reset() {
		
		A = 0;
		for(int i = 0; i < SIZE*2; i++) {
			BB.set(i, 0);
			result.set(i, 0);
		}
		
	}
	
	public int AincrementAndGet(final int a) {
		
		A = a;
		
		while(Bworking) {
			spinCounter++;
		}
		
		int b = BB.get(A);
		int counter = a-1;
		while(counter >= 0) {
			if(BB.get(counter) != 0) {
				if(b < BB.get(counter)) {
					b = BB.get(counter);
					break;
				} 
			}
			counter--;
		}
		
		result.set(b, a+b);
		
		return a+b;
		
	}
	
	public int BincrementAndGet(final int b) {
		
		int a = A;
		
		Bworking = true;
		BB.set(a, b);
		Bworking = false;
		
		int resTemp = result.get(b-1);
		
		if(resTemp >= a+b) {
			//return a+b+1;
			return resTemp + 1;
		} else {
			return a+b;
		}
	}

}
