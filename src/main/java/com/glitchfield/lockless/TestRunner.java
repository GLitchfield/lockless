package com.glitchfield.lockless;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class TestRunner {

	private final LocklessCounter counter;
	
	private final AtomicIntegerArray results;
	private final int COUNT_TO;
	
	private final CountDownLatch latch = new CountDownLatch(2);
	
	public TestRunner(LocklessCounter counter, final int countTo) {
		this.counter = counter;
		
		results = new AtomicIntegerArray(countTo);
		COUNT_TO = countTo;
		
		for(int i = 0; i < COUNT_TO; i++) {
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
					
					final int c = counter.AIncrementAndGet(aCounter);
					if(c < COUNT_TO) {
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
					
					final int c = counter.BIncrementAndGet(bCounter);
					if(c < COUNT_TO) {
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
		
		try {
			threadA.join();
			threadB.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int numTwos = 0;
		int numGTts = 0;
		
		for(int i = 0; i < COUNT_TO; i++) {
			if(results.get(i) == 2) {
				numTwos++;
			} else if(results.get(i) > 2) {
				numGTts++;
			}
		}
		
		return (numTwos + numGTts);
	}
	
	
}
