package com.tnmp.timemusic.database;

public class MusicListBean {

	private int id;

	private String name;

	private String type;

	private String url;

	private long size;

	private long length;

	private int ifdownloaded;

	private int favourite;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public int getIfdownloaded() {
		return ifdownloaded;
	}

	public void setIfdownloaded(int ifdownloaded) {
		this.ifdownloaded = ifdownloaded;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFavourite() {
		return favourite;
	}

	public void setFavourite(int favourite) {
		this.favourite = favourite;
	}

}
