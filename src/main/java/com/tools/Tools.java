package com.tools;

import java.security.MessageDigest;

public class Tools {
    /**
     * 产生随机位数的字符串
     */
	public static String random( int length ) {
		String from   = "abcdefghijklmnopqrstuvwxyz1234567890";
		StringBuffer sb = new StringBuffer();
	    int len = from.length();
	    for (int i = 0; i < length; i++) {
	        sb.append(from.charAt(getRandom(len-1)));
	    }
	    return sb.toString();
	}
	
	private static int getRandom(int count) {
	    return (int) Math.round(Math.random() * (count));
	}
	
	//md5加密
	public static String getMD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};        
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
