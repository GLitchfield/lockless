package com.glitchfield.lockless;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class RunTest {

	private volatile LocklessCounter counter;
	private volatile AtomicIntegerArray results;
	private volatile CountDownLatch latch = new CountDownLatch(2);
	
	final int ARRAY_SIZE;

	public RunTest(final int arraySize) {
		ARRAY_SIZE = arraySize;
		counter = new LocklessCounter(ARRAY_SIZE);
		results = new AtomicIntegerArray(ARRAY_SIZE);
		
		for(int i = 0; i < ARRAY_SIZE; i++) {
			results.set(i, 0);
		}
		
	}
	
	public int runTest() {
		
		final AtomicBoolean keepRunningA = new AtomicBoolean(true);
		final AtomicBoolean keepRunningB = new AtomicBoolean(true);
		
		final Runnable runA = new Runnable() {

			volatile int aCounter = 0;
			
			@Override
			public void run() {
				
				latch.countDown();
				try {
					latch.await();
				} catch (final InterruptedException e) {
					//e.printStackTrace();
				}
				
				while(keepRunningA.get()) {
					
					aCounter = aCounter + 1;
					
					final int c = counter.AincrementAndGet(aCounter);
					if(c < ARRAY_SIZE) {
						results.getAndIncrement(c);
					} else {
						keepRunningA.set(false);
					}
				}
				
			}
			
		};
		
		final Runnable runB = new Runnable() {

			volatile int bCounter = 0;
			
			@Override
			public void run() {
				
				latch.countDown();
				try {
					latch.await();
				} catch (final InterruptedException e) {
					//e.printStackTrace();
				}
				
				while(keepRunningB.get()) {
					
					bCounter = bCounter + 1;
					
					final int c = counter.BincrementAndGet(bCounter);
					if(c < ARRAY_SIZE) {
						results.getAndIncrement(c);
					} else {
						keepRunningB.set(false);
					}
				}
			}
			
		};
		
		final Thread threadA = new Thread(runA);
		final Thread threadB = new Thread(runB);
		
		threadA.setDaemon(true);
		threadB.setDaemon(true);
		
		threadA.start();
		threadB.start();
		
		while(keepRunningA.get() && keepRunningB.get()) {
			
		}
		
		try {
			threadA.join();
			threadB.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		int numTwos = 0;
		int numGTts = 0;
		
		for(int i = 0; i < ARRAY_SIZE; i++) {
			if(results.get(i) == 2) {
				numTwos++;
			} else if(results.get(i) > 2) {
				numGTts++;
			}
		}
	
//		if(numTwos + numGTts > 0 && !counter.isResEmpty()) {
//			System.out.println(printRes(results));
//			System.out.println(counter.printRes());
//		}
	
//		if(!counter.isResEmpty()) {
//			System.out.println(counter.printRes());
//		}
		
		return (numTwos + numGTts);
		
	}
	
	private String printRes(final AtomicIntegerArray res) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < res.length(); i++) {
			sb.append(res.get(i));
		}
		return sb.toString();
	}
	
}
