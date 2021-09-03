package com.quests.utils;

import java.util.Optional;

public class StringMinipulation {
	
	public static String removeLastCharOptional(String s) {
	    return Optional.ofNullable(s)
	      .filter(str -> str.length() != 0)
	      .map(str -> str.substring(0, str.length() - 1))
	      .orElse(s);
	    }
	public static String removeLastCharOptional(String s, int amount) {
	    return Optional.ofNullable(s)
	      .filter(str -> str.length() != 0)
	      .map(str -> str.substring(0, str.length() - amount))
	      .orElse(s);
	    }

}
