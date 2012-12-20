package com.glitchfield.lockless;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class LocklessCounter {
	
	private int SIZE = 100;
	
	private volatile int A = 0;
	
	private volatile AtomicIntegerArray B;
	private volatile AtomicIntegerArray result;
	
	public LocklessCounter() {
		
		B = new AtomicIntegerArray(SIZE*2);
		result = new AtomicIntegerArray(SIZE*2);
		for(int i = 0; i < SIZE*2; i++) {
			B.set(i, 0);
			result.set(i, 0);
		}
		
	}
	
	public LocklessCounter(final int size) {
		
		SIZE = size;
		B = new AtomicIntegerArray(SIZE*2);
		result = new AtomicIntegerArray(SIZE*2);
		for(int i = 0; i < SIZE*2; i++) {
			B.set(i, 0);
			result.set(i, 0);
		}
		
	}
	
	public synchronized void reset() {
		
		A = 0;
		for(int i = 0; i < SIZE*2; i++) {
			B.set(i, 0);
			result.set(i, 0);
		}
		
	}
	
	public int AincrementAndGet(final int a) {
		
		A = a;
		
		int b = B.get(A);
		int counter = A-1;
		while(counter >= 0) {
			int temp = B.get(counter);
			if(temp != 0) {
				if(b < temp) {
//					b = BB.get(counter);
					b = temp;
					
				} 
				break;
			}
			counter--;
		}
		
		result.set(b, a+b);
		
		return a+b;
		
	}
	
	public int BincrementAndGet(final int b) {
		
		int a = A;
		
		B.set(a, b);
		
		int resTemp = result.get(b-1);
		
		if(resTemp >= a+b) {
			return resTemp + 1;
		} else {
			return a+b;
		}
	}

}
