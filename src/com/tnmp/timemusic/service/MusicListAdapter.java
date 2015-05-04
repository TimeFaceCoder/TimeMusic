package com.tnmp.timemusic.service;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tnmp.timemusic.MainActivity;
import com.tnmp.timemusic.R;
import com.tnmp.timemusic.database.DBHelper;
import com.tnmp.timemusic.database.MusicListBean;

public class MusicListAdapter extends BaseAdapter {

	private static final int COLLECTED = 1;// 已收藏值
	private static final int NOT_COLLECTED = 0;// 未收藏值
	private Context context;

	private ArrayList<MusicListBean> list;

	public MusicListAdapter(Context context, ArrayList<MusicListBean> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	// 用以生成在ListView中展示的一个个元素View
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 优化ListView
		convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
		final ItemViewCache viewCache = new ItemViewCache();
		viewCache.musicName = (TextView) convertView
				.findViewById(R.id.musicname);
		viewCache.imageBtn = (ImageButton) convertView
				.findViewById(R.id.favourite_btn);
		convertView.setTag(viewCache);
		// 设置文本和图片，然后返回这个View，用于ListView的Item的展示
		viewCache.musicName.setText(list.get(position).getName());
		if (list.get(position).getFavourite() == NOT_COLLECTED) {
			viewCache.imageBtn.setImageResource(R.drawable.collect);
		}
		if (list.get(position).getFavourite() == COLLECTED) {
			viewCache.imageBtn.setImageResource(R.drawable.collected);
		}

		viewCache.imageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DBHelper dbh = new DBHelper();
				int updateResult = dbh.modifyFavState(list.get(position)
						.getFavourite(), list.get(position).getId());
				
				if(updateResult != -1){
					list.get(position).setFavourite(updateResult);
				}
				//更新播放列表
				if (MainActivity.playList.getCurrent_focus() == MainActivity.FAVOURITE_TAG) {
					ArrayList<MusicListBean> favouriteList = dbh.getListByFav();
					MainActivity.playList.setPlayList(favouriteList);
				}
				dbh.closeDatabase();
				MusicListAdapter.this.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	// 元素的缓冲类,用于优化ListView
	private static final class ItemViewCache {
		public TextView musicName;
		public ImageButton imageBtn;
		// public TextView musicType;
	}

}
