package com.A1w0n.androidcommonutils;

import android.os.Build;

public class APILevelUtils {
	
/*	SDK_INT value        Build.VERSION_CODES        Human Version Name       
    1                  BASE                      Android 1.0 (no codename)
    2                  BASE_1_1                  Android 1.1 Petit Four
    3                  CUPCAKE                   Android 1.5 Cupcake
    4                  DONUT                     Android 1.6 Donut
    5                  ECLAIR                    Android 2.0 Eclair
    6                  ECLAIR_0_1                Android 2.0.1 Eclair                  
    7                  ECLAIR_MR1                Android 2.1 Eclair
    8                  FROYO                     Android 2.2 Froyo
    9                  GINGERBREAD               Android 2.3 Gingerbread
   10                  GINGERBREAD_MR1           Android 2.3.3 Gingerbread
   11                  HONEYCOMB                 Android 3.0 Honeycomb
   12                  HONEYCOMB_MR1             Android 3.1 Honeycomb
   13                  HONEYCOMB_MR2             Android 3.2 Honeycomb
   14                  ICE_CREAM_SANDWICH        Android 4.0 Ice Cream Sandwich
   15                  ICE_CREAM_SANDWICH_MR1    Android 4.0.3 Ice Cream Sandwich
   16                  JELLY_BEAN                Android 4.1 Jellybean
   17                  JELLY_BEAN_MR1            Android 4.2 Jellybean
   18                  JELLY_BEAN_MR2            Android 4.3 Jellybean
   19                  KITKAT                    Android 4.4 KitKat
  10000                CUR_DEVELOPMENT           Current Development Build*/

	private APILevelUtils() {
	}
	
	/**
	 * Level 18 是Android 4.3 
	 * JELLY_BEAN_MR2 指的也是Android 4.3
	 */
	public static boolean isLevel18AndAbove() {
		return Integer.valueOf(Build.VERSION.SDK_INT).intValue() >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}

}
