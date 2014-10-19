package com.team7.smartwatch.shared.tests;

import com.team7.smartwatch.shared.RadialFence;

import static org.junit.Assert.*;

import org.junit.Test;

public class RadialFenceTest {

	@Test
	public void containsLocation_LocationOutsideFence_False() {
		
		Double libraryLat = -27.477114;
		Double libraryLong = 153.028291;
		Double radius = 100.0;
		RadialFence fence = new RadialFence(libraryLat, libraryLong, radius);
		
		Double parliamentLat = -27.475065;
		Double parliamentLong = 153.027157;
		assertFalse(fence.containsLocation(parliamentLat, parliamentLong));
	}

	@Test
	public void containsLocation_LocationInsideFence_True() {
		
		Double sBlockLat = -27.477412;
		Double sBlockLong = 153.027221;
		Double radius = 100.0;
		RadialFence fence = new RadialFence(sBlockLat, sBlockLong, radius);
		
		Double zBlockLat = -27.477797;
		Double zBlockLong = 153.027618;
		assertTrue(fence.containsLocation(zBlockLat, zBlockLong));
	}
}
