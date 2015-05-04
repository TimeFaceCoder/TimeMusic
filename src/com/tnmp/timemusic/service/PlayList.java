package com.tnmp.timemusic.service;

import java.util.ArrayList;
import java.util.Random;

import com.tnmp.timemusic.MainActivity;
import com.tnmp.timemusic.database.MusicListBean;

public class PlayList {

	private ArrayList<MusicListBean> playList;// ≤•∑≈¡–±Ì
	private int position;
	private int current_focus;

	public PlayList(ArrayList<MusicListBean> playList, int position, int current_focus) {
		this.playList = playList;
		this.position = position;
		this.current_focus = current_focus;
	}

	public MusicListBean getPreMusic() {
		if(playList.size() == 0){
			setPlayList(MainActivity.getAllList());
			setPosition(0);
		}
		MusicListBean rtn = new MusicListBean();
		switch (MainActivity.CURRENT_MODE) {
		case MainActivity.MODE_ORDER:
			if (position == 0) {
				position = playList.size() - 1;
			} else {
				position--;
			}
			rtn = playList.get(position);
			break;
		case MainActivity.MODE_REPEAT:
			if (position == 0) {
				position = playList.size() - 1;
			} else {
				position--;
			}
			rtn = playList.get(position);
			break;
		case MainActivity.MODE_RANDON:
			rtn = getRandomMusic();
			break;
		}
		return rtn;
	}

	public MusicListBean getNextMusic() {
		if(playList.size() == 0){
			setPlayList(MainActivity.getAllList());
			setPosition(0);
		}
		MusicListBean rtn = new MusicListBean();
		switch (MainActivity.CURRENT_MODE) {
		case MainActivity.MODE_ORDER:
			if (position >= (playList.size() - 1)) {
				position = 0;
			} else {
				position++;
			}
			rtn = playList.get(position);
			break;
		case MainActivity.MODE_REPEAT:
			if (position == (playList.size() - 1)) {
				position = 0;
			} else {
				position++;
			}
			rtn = playList.get(position);
			break;
		case MainActivity.MODE_RANDON:
			rtn = getRandomMusic();
			break;
		}
		return rtn;
	}

	public MusicListBean getRandomMusic() {
		Random random = new Random();
		position = random.nextInt(playList.size());
		return playList.get(position);
	}

	public MusicListBean getCurrentMusic() {
		return playList.get(position);
	}

	public ArrayList<MusicListBean> getPlayList() {
		return playList;
	}

	public void setPlayList(ArrayList<MusicListBean> playList) {
		this.playList = null;
		this.playList = playList;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getCurrent_focus() {
		return current_focus;
	}

	public void setCurrent_focus(int current_focus) {
		this.current_focus = current_focus;
	}

}
