package com.tools;

public class Tools {
    /**
     * 产生随机位数的字符串
     */
	public String random( int length ) {
		String from   = "abcdefghijklmnopqrstuvwxyz1234567890";
		StringBuffer sb = new StringBuffer();
	    int len = from.length();
	    for (int i = 0; i < length; i++) {
	        sb.append(from.charAt(getRandom(len-1)));
	    }
	    return sb.toString();
	}
	
	private int getRandom(int count) {
	    return (int) Math.round(Math.random() * (count));
	}
}
