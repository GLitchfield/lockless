package com.glitchfield.lockless;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class LocklessCounter {
	
	private int SIZE = 100;
	
	private volatile int A = 0;
	
	private volatile AtomicIntegerArray B;
	private volatile AtomicIntegerArray result;
	
	private volatile AtomicIntegerArray AThreadRes;
	private volatile AtomicIntegerArray BThreadRes;
	
	private volatile AtomicIntegerArray AThreadAVal;
	private volatile AtomicIntegerArray AThreadBVal;
	private volatile AtomicIntegerArray BThreadAVal;
	private volatile AtomicIntegerArray BThreadBVal;
	
	public LocklessCounter() {
		
		B = new AtomicIntegerArray(SIZE*2);
		result = new AtomicIntegerArray(SIZE*2);
		for(int i = 0; i < SIZE*2; i++) {
			B.set(i, 0);
			result.set(i, 0);
		}
		
		AThreadRes = new AtomicIntegerArray(SIZE*2);
		BThreadRes = new AtomicIntegerArray(SIZE*2);
		
		AThreadAVal = new AtomicIntegerArray(SIZE*2);
		AThreadBVal = new AtomicIntegerArray(SIZE*2);
		BThreadAVal = new AtomicIntegerArray(SIZE*2);
		BThreadBVal = new AtomicIntegerArray(SIZE*2);
		
	}
	
	public LocklessCounter(final int size) {
		
		SIZE = size;
		B = new AtomicIntegerArray(SIZE*2);
		result = new AtomicIntegerArray(SIZE*2);
		for(int i = 0; i < SIZE*2; i++) {
			B.set(i, 0);
			result.set(i, 0);
		}
		
		AThreadRes = new AtomicIntegerArray(SIZE*2);
		BThreadRes = new AtomicIntegerArray(SIZE*2);
		
		AThreadAVal = new AtomicIntegerArray(SIZE*2);
		AThreadBVal = new AtomicIntegerArray(SIZE*2);
		BThreadAVal = new AtomicIntegerArray(SIZE*2);
		BThreadBVal = new AtomicIntegerArray(SIZE*2);
		
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
		
		int coord = a+b;
		
		int temp = result.addAndGet(coord, 2);
		result.set(coord, temp);
		int res = result.get(coord);
		
//		if(res != 2) {
//			if(a <= b) {
//				return coord;
//			} else {
//				return coord+1;
//			}
//		} else {
//			return coord;
//		}
		
		AThreadRes.set(coord, res);
		AThreadAVal.set(coord, a);
		AThreadBVal.set(coord, b);
		
		if(res == 2) {
			return coord;
		} else {
			return coord+1;
		}
		
	}
	
	public int BincrementAndGet(final int b) {
		
		int a = A;
		
		B.set(a, b);
		
		int coord = a+b;
		
		int temp = result.addAndGet(coord, 3);
		result.set(coord, temp);
		int res = result.get(coord);
		
//		if(res != 3) {
//			if(a <= b) {
//				return coord;
//			} else {
//				return coord+1;
//			}
//		} else {
//			return coord;
//		}
		
		BThreadRes.set(coord, res);
		BThreadAVal.set(coord, a);
		BThreadBVal.set(coord, b);
		
		if(res == 3) {
			return coord;
		} else {
			return coord+1;
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
			sb.append(result.get(i)+",");
		}
		return sb.toString();
	}
	
	public String printCoords() {
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < result.length(); i++) {
			sb.append(AThreadRes.get(i)+",");
		}
		sb.append("\n");
		for(int i = 0; i < result.length(); i++) {
			sb.append(AThreadAVal.get(i)+",");
		}
		sb.append("\n");
		for(int i = 0; i < result.length(); i++) {
			sb.append(AThreadBVal.get(i)+",");
		}
		sb.append("\n");
		
		for(int i = 0; i < result.length(); i++) {
			sb.append(BThreadRes.get(i)+",");
		}
		sb.append("\n");
		for(int i = 0; i < result.length(); i++) {
			sb.append(BThreadAVal.get(i)+",");
		}
		sb.append("\n");
		for(int i = 0; i < result.length(); i++) {
			sb.append(BThreadBVal.get(i)+",");
		}
		return sb.toString();
	}

}
