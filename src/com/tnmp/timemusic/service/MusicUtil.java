package com.tnmp.timemusic.service;

public class MusicUtil {

	public static String convertLength(long mp3Length) {
		int min = 0;
		int sec = 0;
		min = (int) (mp3Length / 60);
		sec = (int) (mp3Length % 60);
		String secStr = "";
		String minStr = "";
		if (sec < 10)
			secStr = "0" + sec;
		else
			secStr = "" + sec;
		if (min < 10)
			minStr = "0" + min;
		else
			minStr = "" + min;
		return minStr + ":" + secStr;
	}

	public static String convertLength(int mp3Length) {
		int min = 0;
		int sec = 0;
		min = (int) (mp3Length / 60);
		sec = (int) (mp3Length % 60);
		String secStr = "";
		String minStr = "";
		if (sec < 10)
			secStr = "0" + sec;
		else
			secStr = "" + sec;
		if (min < 10)
			minStr = "0" + min;
		else
			minStr = "" + min;
		return minStr + ":" + secStr;
	}

}
