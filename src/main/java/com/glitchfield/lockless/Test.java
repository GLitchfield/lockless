package com.glitchfield.lockless;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Test {

	final static int NUM_TESTS = 100 * 1000;
	final static int ARRAY_SIZE = 1 * 1000;
	
	public static void main(final String[] args) {
		
		final List<Integer> results = new ArrayList<Integer>();
		
		
		for(int i = 0; i < NUM_TESTS; i++) {
			int result = runTest(ARRAY_SIZE);
			System.out.println(result);
			results.add(result);
		}
		
		int sum = 0;
		for(int r : results) {
			sum = sum + r;
		}
		
		System.out.println();
		System.out.println("Average: " + sum / (double)results.size());
		
	}
	
	
	public static int runTest(final int size) {
		
		final LocklessCounter counter = new LocklessCounter(size);
		
		final AtomicIntegerArray results = new AtomicIntegerArray(ARRAY_SIZE);
		
		for(int i = 0; i < ARRAY_SIZE; i++) {
			results.set(i, 0);
		}
		
		final AtomicBoolean keepRunning = new AtomicBoolean(true);
		
		final Runnable runA = new Runnable() {

			volatile int aCounter = 0;
			
			@Override
			public void run() {
				
				while(keepRunning.get()) {
					
					aCounter = aCounter + 1;
					
					final int c = counter.AincrementAndGet(aCounter);
					if(c < ARRAY_SIZE) {
						results.getAndIncrement(c);
					} else {
						keepRunning.set(false);
					}
				}
				
			}
			
		};
		
		final Runnable runB = new Runnable() {

			volatile int bCounter = 0;
			
			@Override
			public void run() {
				
				while(keepRunning.get()) {
					
					bCounter = bCounter + 1;
					
					final int c = counter.BincrementAndGet(bCounter);
					if(c < ARRAY_SIZE) {
						results.getAndIncrement(c);
					} else {
						keepRunning.set(false);
					}
				}
			}
			
		};
		
		final Thread threadA = new Thread(runA);
		final Thread threadB = new Thread(runB);
		
		threadA.start();
		threadB.start();
		
		while(keepRunning.get())  {
			
		}
		
		int numTwos = 0;
		int numGTts = 0;
		
//		System.out.println();
		for(int i = 0; i < ARRAY_SIZE; i++) {
			if(results.get(i) == 2) {
				numTwos++;
			} else if(results.get(i) > 2) {
				numGTts++;
			}
		}
		
//		System.out.println();
//		System.out.println("Num twos = " + numTwos + " NumGTTwo = " + numGTts);

		return numTwos + numGTts;
	}
	
}
