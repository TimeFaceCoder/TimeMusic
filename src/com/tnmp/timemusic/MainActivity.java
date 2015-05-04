package com.tnmp.timemusic;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tnmp.timemusic.database.DBHelper;
import com.tnmp.timemusic.database.DBManager;
import com.tnmp.timemusic.database.MusicListBean;
import com.tnmp.timemusic.service.MusicFavAdapter;
import com.tnmp.timemusic.service.MusicListAdapter;
import com.tnmp.timemusic.service.MusicUtil;
import com.tnmp.timemusic.service.PlayList;
import com.tnmp.timemusic.service.StreamingMediaPlayer;

public class MainActivity extends ListActivity {

	// ȫ�ֱ���
	private static long exitTime = 0;
	private static int cur_pos = -1;// ��ǰ�����ַ

	public static final int MUSIC_TAG = 0;// �����ǩ����
	public static final int SEARCH_TAG = 1;// ������ǩ����
	public static final int FAVOURITE_TAG = 2;// �ղر�ǩ����
	public static int CURRENT_FOCUS = 0;// ��ǰ����

	public static final int MODE_ORDER = 0;// ˳�򲥷Ŵ���
	public static final int MODE_RANDON = 1;// ���Ŵ���
	public static final int MODE_REPEAT = 2;// �ظ����Ŵ���
	public static int CURRENT_MODE = 0;// ��ǰ����ģʽ��Ĭ��ֵȡ����ݿ�

	private static Context mContext;

	// �ؼ�����
	private RelativeLayout wifiTip;
	private Button closeTip;
	private RelativeLayout searchBar;
	private ImageButton musicList;
	private ImageButton search;
	private ImageButton favourite;
	private EditText searchTxt;
	private Button searchBtn;
	private static ImageButton pauseBtn;
	private ImageButton previous;
	private ImageButton next;
	private ImageButton playMode;
	public static TextView currentMusciname;
	private static SeekBar seekbar;
	private static TextView currentPosition;
	private static TextView musicLength;
	public static StreamingMediaPlayer audioStreamer;

	// ��ݿ�
	private DBManager dbm;

	// �����б�
	private static ArrayList<MusicListBean> allList;// ȫ������
	private static ArrayList<MusicListBean> searchList;// �������
	private static ArrayList<MusicListBean> favouriteList;// �ղ��б�
	public static PlayList playList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		setContentView(R.layout.activity_main);

		// ��ʼ����ݿ�
		dbm = new DBManager(this);
		dbm.openDatabase();
		dbm.closeDatabase();

		initView();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(mContext, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {

		// �ؼ���ֵ
		wifiTip = (RelativeLayout) findViewById(R.id.wifi_layout);
		closeTip = (Button) findViewById(R.id.close_tip);
		closeTip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// tip�رհ�ťʵ��
				wifiTip.setVisibility(View.GONE);
			}
		});
		musicList = (ImageButton) findViewById(R.id.musiclist);
		musicList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����ǩ��ťʵ��
				searchBar.setVisibility(View.GONE);
				focusTag(MUSIC_TAG);

				// DB��ȡ�б����
				DBHelper dbh = new DBHelper();
				allList = dbh.getAllList();
				dbh.closeDatabase();

				if (allList != null) {
					setListAdapter(new MusicListAdapter(mContext, allList));
				}

			}
		});
		search = (ImageButton) findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ������ǩ��ťʵ��
				searchBar.setVisibility(View.VISIBLE);
				focusTag(SEARCH_TAG);

				// ������ݣ�������
				String searchStr = searchTxt.getText().toString();
				DBHelper dbh = new DBHelper();
				searchList = dbh.getListByQuery(searchStr);
				dbh.closeDatabase();

				if (searchList != null) {
					setListAdapter(new MusicListAdapter(mContext, searchList));
				}
			}
		});
		favourite = (ImageButton) findViewById(R.id.favourite);
		favourite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// �ղر�ǩ��ťʵ��
				searchBar.setVisibility(View.GONE);
				focusTag(FAVOURITE_TAG);
				// DB��ȡ�б����
				DBHelper dbh = new DBHelper();
				favouriteList = dbh.getListByFav();
				dbh.closeDatabase();
				if (favouriteList != null) {
					setListAdapter(new MusicFavAdapter(mContext, favouriteList));
				}
			}
		});
		searchBar = (RelativeLayout) findViewById(R.id.search_bar_layout);
		searchTxt = (EditText) findViewById(R.id.search_text);
		searchBtn = (Button) findViewById(R.id.search_button);
		searchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ʵ��������ť
				String searchStr = searchTxt.getText().toString();
				DBHelper dbh = new DBHelper();
				searchList = dbh.getListByQuery(searchStr);
				dbh.closeDatabase();

				if (searchList != null) {
					if (searchStr == null || searchStr.equals("")) {
						searchList.clear();
						Toast.makeText(mContext, "������ؼ������", Toast.LENGTH_SHORT)
								.show();
					}
					setListAdapter(new MusicListAdapter(mContext, searchList));
				}

				// �ر����뷨
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				// ��ʾ�����������뷨
				if (imm.isActive()) {
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});
		pauseBtn = (ImageButton) findViewById(R.id.pause);
		pauseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (audioStreamer == null
						|| audioStreamer.getMediaPlayer() == null) {
					String url = allList.get(0).getUrl();
					String name = allList.get(0).getName();
					long size = allList.get(0).getSize();
					long length = allList.get(0).getLength();
					switch (CURRENT_FOCUS) {
					case SEARCH_TAG:
						if (searchList != null && searchList.size() > 0) {
							url = searchList.get(0).getUrl();
							name = searchList.get(0).getName();
							size = searchList.get(0).getSize();
							length = searchList.get(0).getLength();
						} else {
							searchBar.setVisibility(View.GONE);
							focusTag(MUSIC_TAG);
							setListAdapter(new MusicListAdapter(mContext,
									allList));
						}
						break;
					case FAVOURITE_TAG:
						if (favouriteList != null && favouriteList.size() > 0) {
							url = favouriteList.get(0).getUrl();
							name = favouriteList.get(0).getName();
							size = favouriteList.get(0).getSize();
							length = favouriteList.get(0).getLength();
						} else {
							focusTag(MUSIC_TAG);
							setListAdapter(new MusicListAdapter(mContext,
									allList));
						}
						break;
					}
					startStreamingAudio(url, name, size, length);
					musicLength.setText(MusicUtil.convertLength(length));

				} else {
					// ��ͣ��ťʵ��
					if (audioStreamer.getMediaPlayer().isPlaying()) {
						audioStreamer.getMediaPlayer().pause();
						pauseBtn.setBackgroundResource(R.drawable.playbuttonselector);
					} else {
						audioStreamer.getMediaPlayer().start();
						audioStreamer.startPlayProgressUpdater();
						pauseBtn.setBackgroundResource(R.drawable.pausebuttonselector);
					}
				}
			}
		});
		previous = (ImageButton) findViewById(R.id.previous);
		previous.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��һ�װ�ťʵ��
				preMp3();
			}
		});
		next = (ImageButton) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��һ�װ�ťʵ��
				nextMp3();
			}
		});
		playMode = (ImageButton) findViewById(R.id.play_mode);
		playMode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����ģʽ��ťʵ��
				CURRENT_MODE++;
				if (CURRENT_MODE > MODE_REPEAT) {
					CURRENT_MODE = MODE_ORDER;
				}
				playMode.setBackgroundResource(getPlayModeDrawable(CURRENT_MODE));

				// ���ò���ģʽ
				// if (CURRENT_MODE == MODE_REPEAT) {
				// audioStreamer.getMediaPlayer().setLooping(true);
				// }

				// ���CURRENT_MODEֵ����ݿ�
				DBHelper dbh = new DBHelper();
				dbh.setPlayMode(CURRENT_MODE);
				dbh.closeDatabase();

			}
		});
		currentMusciname = (TextView) findViewById(R.id.current_musicname);
		seekbar = (SeekBar) findViewById(R.id.seek_bar);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int progress;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					this.progress = progress
							* audioStreamer.getMediaPlayer().getDuration()
							/ seekbar.getMax();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				// audioStreamer.getMediaPlayer().pause();

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				audioStreamer.getMediaPlayer().seekTo(progress);
			}

		});
		currentPosition = (TextView) findViewById(R.id.current_position);
		musicLength = (TextView) findViewById(R.id.music_length);

		// �ж��Ƿ���wifi��������ʾtip
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null) {
			int wifiState = wifiManager.getWifiState();
			if (wifiState != WifiManager.WIFI_STATE_ENABLED) {
				wifiTip.setVisibility(View.VISIBLE);
			}
		}

		// Ĭ�Ͻ�������ǩ
		musicList.setBackgroundResource(R.drawable.music_f);

		// ��ȥȫ�������б�
		DBHelper dbh = new DBHelper();
		allList = dbh.getAllList();

		if (allList != null && allList.size() > 0) {
			setListAdapter(new MusicListAdapter(this, allList));
		}

		// ���ò���ģʽ
		// �ȴ���ݿ��ȡ����ģʽ
		CURRENT_MODE = dbh.getPlayMode();
		playMode.setBackgroundResource(getPlayModeDrawable(CURRENT_MODE));

		// �ر���ݿ�����
		dbh.closeDatabase();

		// ���������б�
		playList = new PlayList(allList, 0, CURRENT_FOCUS);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		if (audioStreamer == null
				|| audioStreamer.getPlayingTag() != CURRENT_FOCUS
				|| audioStreamer.getPlayingPosition() != position) {
			long mp3Length = 0;
			cur_pos = position;
			switch (CURRENT_FOCUS) {
			case MUSIC_TAG:
				// Toast.makeText(this, allList.get(position).getUrl(),
				// Toast.LENGTH_SHORT).show();
				startStreamingAudio(allList.get(position).getUrl(), allList
						.get(position).getName(), allList.get(position)
						.getSize(), allList.get(position).getLength());
				mp3Length = allList.get(position).getLength();
				playList.setPlayList(allList);
				playList.setPosition(position);
				playList.setCurrent_focus(CURRENT_FOCUS);
				break;
			case SEARCH_TAG:
				// Toast.makeText(this, searchList.get(position).getName(),
				// Toast.LENGTH_SHORT).show();
				startStreamingAudio(searchList.get(position).getUrl(),
						searchList.get(position).getName(),
						searchList.get(position).getSize(),
						searchList.get(position).getLength());
				mp3Length = searchList.get(position).getLength();
				playList.setPlayList(searchList);
				playList.setPosition(position);
				playList.setCurrent_focus(CURRENT_FOCUS);
				break;
			case FAVOURITE_TAG:
				// Toast.makeText(this, favouriteList.get(position).getName(),
				// Toast.LENGTH_SHORT).show();
				startStreamingAudio(favouriteList.get(position).getUrl(),
						favouriteList.get(position).getName(), favouriteList
								.get(position).getSize(),
						favouriteList.get(position).getLength());
				mp3Length = favouriteList.get(position).getLength();
				playList.setPlayList(favouriteList);
				playList.setPosition(position);
				playList.setCurrent_focus(CURRENT_FOCUS);
				break;
			default:
				cur_pos = -1;
				break;
			}

			musicLength.setText(MusicUtil.convertLength(mp3Length));
		}
	}

	// ���ñ�ǩ����
	private void focusTag(int tag) {
		if (tag != CURRENT_FOCUS) {
			switch (CURRENT_FOCUS) {
			case MUSIC_TAG:
				musicList.setBackgroundResource(R.drawable.music);
				break;
			case SEARCH_TAG:
				search.setBackgroundResource(R.drawable.search);
				break;
			case FAVOURITE_TAG:
				favourite.setBackgroundResource(R.drawable.favourite);
				break;
			}

			switch (tag) {
			case MUSIC_TAG:
				musicList.setBackgroundResource(R.drawable.music_f);
				break;
			case SEARCH_TAG:
				search.setBackgroundResource(R.drawable.search_f);
				break;
			case FAVOURITE_TAG:
				favourite.setBackgroundResource(R.drawable.favourite_f);
				break;
			}

			CURRENT_FOCUS = tag;
		}
	}

	// ��ȡ����ģʽͼ��ID
	private int getPlayModeDrawable(int mode) {
		int rtn;
		switch (mode) {
		case MODE_ORDER:
			rtn = R.drawable.order;
			break;
		case MODE_RANDON:
			rtn = R.drawable.random;
			break;
		case MODE_REPEAT:
			rtn = R.drawable.repeat;
			break;

		default:
			rtn = R.drawable.order;
			break;
		}
		return rtn;
	}

	// ��������
	public static void startStreamingAudio(String mp3Url, String mp3name,
			long mp3Size, long mp3Length) {
		try {
			if (audioStreamer != null) {
				audioStreamer.interrupt();
			}
			// System.out.println("��ǰ��ǩ" + CURRENT_FOCUS + ";��ǰ����ID��" +
			// cur_pos);
			audioStreamer = new StreamingMediaPlayer(mContext, CURRENT_FOCUS,
					cur_pos, seekbar, currentPosition, pauseBtn);
			audioStreamer.startStreaming(mp3Url, mp3Size, mp3Length);
			// if (CURRENT_MODE == MODE_REPEAT) {
			// audioStreamer.getMediaPlayer().setLooping(true);
			// }
			currentMusciname.setText(mp3name);
			musicLength.setText(MusicUtil.convertLength(mp3Length));

			pauseBtn.setBackgroundResource(R.drawable.pausebuttonselector);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ��һ��
	public void nextMp3() {
		MusicListBean mlb = playList.getNextMusic();
		startStreamingAudio(mlb.getUrl(), mlb.getName(), mlb.getSize(),
				mlb.getLength());
	}

	// ��һ��
	public void preMp3() {
		MusicListBean mlb = playList.getPreMusic();
		startStreamingAudio(mlb.getUrl(), mlb.getName(), mlb.getSize(),
				mlb.getLength());
	}

	public static int getCur_pos() {
		return cur_pos;
	}

	public static void setCur_pos(int cur_pos) {
		MainActivity.cur_pos = cur_pos;
	}

	public static ArrayList<MusicListBean> getAllList() {
		return allList;
	}

	public static void setAllList(ArrayList<MusicListBean> allList) {
		MainActivity.allList = allList;
	}

}
