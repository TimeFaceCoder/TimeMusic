//package com.tnmp.timemusic;
//
//import java.util.ArrayList;
//
//import android.app.ListActivity;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.tnmp.timemusic.database.DBHelper;
//import com.tnmp.timemusic.database.MusicListBean;
//import com.tnmp.timemusic.service.MusicListAdapter;
//import com.tnmp.timemusic.service.MusicTag;
//
//public class MusicListActivity extends ListActivity implements
//		View.OnClickListener {
//
//	private ArrayList<MusicListBean> musicList;
//
//	private String tag;
//	private TextView tagView;
//	private MusicTag mt = new MusicTag();
//
//	private Button returnButton;
//	private Button popmusic;
//	private Button drama;
//	private Button hotest;
//	private Button favourite;
//
//	private DBHelper dbh;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.music_list);
//
//		returnButton = (Button) findViewById(R.id.return_button);
//		popmusic = (Button) findViewById(R.id.popmusic);
//		drama = (Button) findViewById(R.id.drama);
//		hotest = (Button) findViewById(R.id.hotest);
//		favourite = (Button) findViewById(R.id.favourite);
//		returnButton.setOnClickListener(this);
//		popmusic.setOnClickListener(this);
//		drama.setOnClickListener(this);
//		hotest.setOnClickListener(this);
//		favourite.setOnClickListener(this);
//
//		tag = getIntent().getExtras().getString("tag");
//		if (tag == null || tag.equals("")) {
//			tag = String.valueOf(R.id.popmusic);
//		}
//
//		tagView = (TextView) findViewById(R.id.tag);
//		tagView.setText(mt.getTag(tag));
//
//		dbh = new DBHelper();
//		if (mt.getTag(tag).equals("favourite")) {
//			musicList = dbh.getListByFav();
//		} else if (mt.getTag(tag).equals("hotest")) {
//			musicList = null;
//		} else {
//			musicList = dbh.getListByType(mt.getTag(tag));
//		}
//
//		dbh.closeDatabase();
//
//		if (musicList != null)
//			setListAdapter(new MusicListAdapter(this, musicList));
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		dbh = new DBHelper();
//		switch (v.getId()) {
//		case R.id.return_button:
//			finish();
//			break;
//
//		case R.id.popmusic:
//			musicList = dbh.getListByType(mt.getTag(String
//					.valueOf(R.id.popmusic)));
//			if (musicList != null)
//				setListAdapter(new MusicListAdapter(this, musicList));
//			break;
//
//		case R.id.drama:
//			musicList = dbh
//					.getListByType(mt.getTag(String.valueOf(R.id.drama)));
//			if (musicList != null)
//				setListAdapter(new MusicListAdapter(this, musicList));
//			break;
//
//		case R.id.hotest:
//			setListAdapter(null);
//			Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
//			break;
//
//		case R.id.favourite:
//			musicList = dbh.getListByFav();
//			if (musicList != null)
//				setListAdapter(new MusicListAdapter(this, musicList));
//			break;
//		}
//
//		dbh.closeDatabase();
//
//	}
//
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		// TODO Auto-generated method stub
//		String text = "歌曲名：" + musicList.get(position).getName() + "\n"
//				+ "分    类：" + musicList.get(position).getType() + "\n"
//				+ "时    长：" + musicList.get(position).getLength() + "\n"
//				+ "文件大小：" + musicList.get(position).getSize() + "\n" + "url："
//				+ musicList.get(position).getUrl();
//		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		return super.onKeyDown(keyCode, event);
//	}
//
//}
