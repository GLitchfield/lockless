package com.glitchfield.lockless;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Test {

	final static int NUM_TESTS = 1 * 1000;
	final static int ARRAY_SIZE = 1 * 1000;
	
	public static void main(final String[] args) {
		
		final List<Integer> results = new ArrayList<Integer>();
		
		
		for(int i = 0; i < NUM_TESTS; i++) {
			final RunTest t = new RunTest(ARRAY_SIZE);
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
	
	public static String printRes(final AtomicIntegerArray res) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < res.length(); i++) {
			sb.append(res.get(i)+",");
		}
		return sb.toString();
	}
	
}
