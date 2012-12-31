package com.glitchfield.lockless;

import java.util.ArrayList;
import java.util.List;

import com.glitchfield.lockless.simple.SimpleLocklessCounter;

public class TestMain {

	final static int NUM_TESTS = 1 * 10000;
	final static int ARRAY_SIZE = 1 * 10000;
	
	public static void main(final String[] args) {
		
		final List<Integer> results = new ArrayList<Integer>();
		
		for(int i = 0; i < NUM_TESTS; i++) {
			final TestRunner t = new TestRunner(new SimpleLocklessCounter(), ARRAY_SIZE);
			int result = t.runTest();
			results.add(result);
			System.out.println(result);
		}

		int sum = 0;
		for(int r : results) {
			sum = sum + r;
		}
		
		final double result = (NUM_TESTS * ARRAY_SIZE) / (double) sum;
		
		System.out.println();
		System.out.println("Counts per error: " + result);
		
	}
	
}
