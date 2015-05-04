package com.tnmp.timemusic.database;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

	private SQLiteDatabase database;

	public DBHelper() {
		this.database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH
				+ "/" + DBManager.DB_NAME, null);
	}

	public ArrayList<MusicListBean> getAllList() {
		String sql = "select id,musicname,musicurl,musiclength,musicsize,favourite from musiclist order by id";
		Cursor cur = database.rawQuery(sql, null);
		if (cur != null) {
			int NUM_LIST = cur.getCount();
			ArrayList<MusicListBean> musicList = new ArrayList<MusicListBean>(
					NUM_LIST);
			if (cur.moveToFirst()) {
				do {
					MusicListBean mlb = new MusicListBean();
					mlb.setId(cur.getInt(cur.getColumnIndex("id")));
					mlb.setName(cur.getString(cur.getColumnIndex("musicname")));
					// mlb.setType(cur.getString(cur.getColumnIndex("musictype")));
					mlb.setUrl(cur.getString(cur.getColumnIndex("musicurl")));
					mlb.setSize(cur.getLong(cur.getColumnIndex("musicsize")));
					mlb.setLength(cur.getLong(cur
							.getColumnIndex("musiclength")));
					// mlb.setIfdownloaded(cur.getInt(cur
					// .getColumnIndex("ifdownloaded")));
					mlb.setFavourite(cur.getInt(cur.getColumnIndex("favourite")));
					musicList.add(mlb);
				} while (cur.moveToNext());
			}
			return musicList;
		} else {
			return null;
		}
	}

	public ArrayList<MusicListBean> getListByFav() {
		String sql = "select id,musicname,musicurl,musiclength,musicsize,favourite from musiclist where favourite=1";
		Cursor cur = database.rawQuery(sql, null);
		if (cur != null) {
			int NUM_LIST = cur.getCount();
			ArrayList<MusicListBean> musicList = new ArrayList<MusicListBean>(
					NUM_LIST);
			if (cur.moveToFirst()) {
				do {
					MusicListBean mlb = new MusicListBean();
					mlb.setId(cur.getInt(cur.getColumnIndex("id")));
					mlb.setName(cur.getString(cur.getColumnIndex("musicname")));
					// mlb.setType(cur.getString(cur.getColumnIndex("musictype")));
					mlb.setUrl(cur.getString(cur.getColumnIndex("musicurl")));
					mlb.setSize(cur.getLong(cur.getColumnIndex("musicsize")));
					mlb.setLength(cur.getLong(cur
							.getColumnIndex("musiclength")));
					// mlb.setIfdownloaded(cur.getInt(cur
					// .getColumnIndex("ifdownloaded")));
					mlb.setFavourite(cur.getInt(cur.getColumnIndex("favourite")));
					musicList.add(mlb);
				} while (cur.moveToNext());
			}
			return musicList;
		} else {
			return null;
		}
	}

	public ArrayList<MusicListBean> getListByQuery(String queryStr) {
		String sql = "select id,musicname,musicurl,musiclength,musicsize,favourite from musiclist ";
		if (queryStr != null && !queryStr.equals("")) {
			sql += "where musicname like '%" + queryStr + "%'";
		}else{
			return new ArrayList<MusicListBean>(0);
		}
		Cursor cur = database.rawQuery(sql, null);
		if (cur != null) {
			int NUM_LIST = cur.getCount();
			ArrayList<MusicListBean> musicList = new ArrayList<MusicListBean>(
					NUM_LIST);
			if (cur.moveToFirst()) {
				do {
					MusicListBean mlb = new MusicListBean();
					mlb.setId(cur.getInt(cur.getColumnIndex("id")));
					mlb.setName(cur.getString(cur.getColumnIndex("musicname")));
					// mlb.setType(cur.getString(cur.getColumnIndex("musictype")));
					mlb.setUrl(cur.getString(cur.getColumnIndex("musicurl")));
					mlb.setSize(cur.getLong(cur.getColumnIndex("musicsize")));
					mlb.setLength(cur.getLong(cur
							.getColumnIndex("musiclength")));
					// mlb.setIfdownloaded(cur.getInt(cur
					// .getColumnIndex("ifdownloaded")));
					mlb.setFavourite(cur.getInt(cur.getColumnIndex("favourite")));
					musicList.add(mlb);
				} while (cur.moveToNext());
			}
			return musicList;
		} else {
			return new ArrayList<MusicListBean>(0);
		}
	}

	public int modifyFavState(int state, int id) {
		int tarState = -1;
		if (state == 0) {
			tarState = 1;
		} else if (state == 1) {
			tarState = 0;
		}
		String sql = "update musiclist set favourite = " + tarState
				+ " where id=" + id;
		try {
			if (tarState != -1) {
				database.execSQL(sql);
			}
		} catch (Exception e) {
			tarState = -1;
		}
		return tarState;
	}

	public int getPlayMode() {
		int playMode = 0;
		String sql = "select playmode from db_info";
		try {
			Cursor cur = database.rawQuery(sql, null);
			if(cur.moveToFirst()){
				playMode = cur.getInt(cur.getColumnIndex("playmode"));
			}
		} catch (Exception e) {
			 playMode = 0;
		}
		return playMode;
	}
	
	public void setPlayMode(int playMode) {
		String sql = "update db_info set playmode = " + playMode ;
		try {
				database.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeDatabase() {
		this.database.close();
	}
}
