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
		A = A;
		
		int b = B.get(a-1);
		int counter = a-2;
		
		while(counter >= 0) {
			int temp = B.get(counter);
			if(temp != 0) {
				if(b < temp) {
					b = temp;
				} 
				break;
			}
			counter--;
		}
		
		
		result.set(a+b, result.getAndAdd(a+b, 2));
		
		if(result.get(a+b) == 2) {
			return a+b;
		} else {
			return a+b+1;
		}
		
	}
	
	public int BincrementAndGet(final int b) {
		
		int a = A;
		
		B.set(a, b);
		
		result.set(a+b, result.getAndAdd(a+b, 3));
		
		if(result.get(a+b) == 3) {
			return a+b;
		} else {
			return a+b+1;
		}
		
	}
	
	public boolean isResEmpty() {
		for(int i = 0; i < SIZE; i++) {
			if(result.get(i) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public String printRes() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < result.length(); i++) {
			sb.append(result.get(i));
		}
		return sb.toString();
	}

}
