package com.team7.smartwatch.shared;

import java.util.Arrays;

public class Utility {

    public static boolean arrayContainsNull(Object... objects) {
    	return Arrays.asList(objects).contains(null);
    }
}
