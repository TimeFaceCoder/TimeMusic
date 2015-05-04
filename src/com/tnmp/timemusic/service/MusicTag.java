package com.tnmp.timemusic.service;

import java.util.HashMap;
import java.util.Map;

import com.tnmp.timemusic.R;

public class MusicTag {

	private Map<String, String> tag = new HashMap<String, String>();

	public MusicTag() {
//		tag.put(String.valueOf(R.id.popmusic), "popmusic");
//		tag.put(String.valueOf(R.id.drama), "drama");
//		tag.put(String.valueOf(R.id.hotest), "hotest");
//		tag.put(String.valueOf(R.id.favourite), "favourite");
	}
	
	public String getTag(String tagStr) {
		String rtn = "";
		if (tagStr == null || tagStr.equals(""))
			return rtn;
		rtn = tag.get(tagStr);
		return rtn;
	}
}
