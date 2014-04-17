/*
 * 
 */
package me.iamcxa.remindme;

import java.util.Locale;

import me.iamcxa.remindme.cardfragment.ListCursorCardFragmentLocal;
import me.iamcxa.remindme.cardfragment.ListCursorCardFragmentTime;
import me.iamcxa.remindme.cardfragment.ListCursorCardFragment;
import me.iamcxa.remindme.cardfragment.ListviewFragment;
import me.iamcxa.remindme.fragment.CardFragmentLoader0;
import me.iamcxa.remindme.fragment.CardFragmentLoader1;
import me.iamcxa.remindme.fragment.CardFragmentLoader2;
import me.iamcxa.remindme.service.TaskSortingService;
import android.R.integer;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.google.android.gms.drive.internal.r;

/**
 * @author cxa Main Activity
 */
public class RemindmeMainActivity extends FragmentActivity {
	/**********************/
	/** Variables LOCALE **/
	/**********************/
	// Used in savedInstanceState
	private static String BUNDLE_SELECTEDFRAGMENT = null;
	private static PagerSlidingTabStrip tabs;
	private static ViewPager pager;
	public static MyPagerAdapter adapter;
	private static ShareActionProvider mShareActionProvider;
	// private Intent mShareIntent;
	// private static SearchView mSearchView;
	private static ProgressBar pBar;
	private static ProgressDialog psDialog;
	private int threadID;
	private static android.app.FragmentManager fragmentManager;
	public static int layoutID = 0;

	/**********************/
	/** onCreate LOCALE **/
	/**********************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLoding("ON");
		CommonUtils.mPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		CommonUtils.debugMsg = CommonUtils.mPreferences.getBoolean(
				CommonUtils.debugMsgTag, true);

		// �]�w�Glayout����
		setContentView(R.layout.activity_main);
		// pBar = (ProgressBar) findViewById(R.id.progressBar1);
		CommonUtils.debugMsg(0, "=========================");
		threadID = android.os.Process.getThreadPriority(android.os.Process
				.myTid());
		CommonUtils.debugMsg(1, threadID + " onCreate");

		new Thread(new Runnable() {
			@Override
			public void run() {
				threadID = android.os.Process
						.getThreadPriority(android.os.Process.myTid());
				CommonUtils.debugMsg(1, threadID + " pre-setViewComponent");
				// �]�w�G��������
				setViewComponent();
			}
		}).start();

		Intent intent = new Intent(this, TaskSortingService.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent);

		if (savedInstanceState != null) {
			setFragment(3, 0);
			setLoding("OFF");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(BUNDLE_SELECTEDFRAGMENT, pager.getCurrentItem());
	}

	/*************************/
	/** onCreateOptionsMenu **/
	/*************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ��main_activity_actionbar.xmlŪ�����s��T
		getMenuInflater().inflate(R.menu.main_activity_actionbar, menu);

		// �w�q�G�j�M���s
		MenuItem actionSearch = menu.findItem(R.id.action_search);
		actionSearch.setVisible(false);
		// �]�w�G�I��������
		// mSearchView = (SearchView) actionSearch.getActionView();
		// setSearchView(actionSearch);

		// �w�q�G�s�W���s
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		// �]�w�G�I��������
		actionAdd.setOnMenuItemClickListener(btnActionAddClick);

		// �w�q�G������s
		MenuItem actionRefresh = menu.findItem(R.id.action_refresh);
		// �]�w�G�I��������
		actionRefresh.setOnMenuItemClickListener(btnRefreshClick);

		/*
		 * // �w�q�G���ɫ��s MenuItem actionShare = menu.findItem(R.id.action_share);
		 * �]�w�Gplay�W��app�s��/���ɵ��B�ͪ���ܦr�� String playStoreLink =
		 * " https://play.google.com/store/apps/details?id=" + getPackageName();
		 * String yourShareText = getString(R.string.share_this) +
		 * playStoreLink; // �]�w�G����intent Intent shareIntent =
		 * ShareCompat.IntentBuilder.from(this)
		 * .setType("text/plain").setText(yourShareText).getIntent(); //
		 * �]�w�G�I�����Ѫ� mShareActionProvider = (ShareActionProvider) actionShare
		 * .getActionProvider();
		 * mShareActionProvider.setShareIntent(shareIntent);
		 */

		// �w�q�G�]�m���s
		MenuItem actionPref = menu.findItem(R.id.action_settings);
		// �]�w�G�I��������
		actionPref.setOnMenuItemClickListener(btnPrefClick);

		return super.onCreateOptionsMenu(menu);
	}

	/**********************/
	/** setViewComponent **/
	/**********************/
	private void setViewComponent() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadID = android.os.Process
						.getThreadPriority(android.os.Process.myTid());
				CommonUtils.debugMsg(1, threadID + " setViewComponent");
				// �w�q�Gtabs
				tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
				pager = (ViewPager) findViewById(R.id.pager);
				adapter = new MyPagerAdapter(getSupportFragmentManager());
				CommonUtils.debugMsg(0,
						"adapter.getCount=" + adapter.getCount());
				// �]�w�G����margin
				final int pageMargin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
								.getDisplayMetrics());
				pager.setPageMargin(pageMargin);
				// �T�O���|�������|�Q�N�~�M��
				pager.setOffscreenPageLimit(3);

				// �]�w�Gtab�Ppage��adapter
				pager.setAdapter(adapter);
				tabs.setViewPager(pager);
			}
		}).start();
	}

	/**********************/
	/** setFragment **/
	/**********************/
	// To ensure the target frame will show the right fragment.
	public void setFragment(final int MODE, final int FragmentPosition) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadID = android.os.Process.getThreadPriority(android.os.Process
						.myTid());
				CommonUtils.debugMsg(1, threadID + " setFragment " + FragmentPosition);
				
				fragmentManager = getFragmentManager();
				FragmentTransaction	 fragmentTransaction = fragmentManager
						.beginTransaction();

				// �]�w�d��
				ListCursorCardFragment cardview = new ListCursorCardFragment();
				ListCursorCardFragmentTime cardview1 = new ListCursorCardFragmentTime();
				ListCursorCardFragmentLocal cardview2 = new ListCursorCardFragmentLocal();
				ListCursorCardFragment.sortOrder = CommonUtils.RemindmeTaskCursor.KeyColumns.PriorityWeight;
				ListCursorCardFragmentTime.sortOrder = CommonUtils.RemindmeTaskCursor.KeyColumns.EndDate;
				ListCursorCardFragmentLocal.sortOrder = CommonUtils.RemindmeTaskCursor.KeyColumns.Distance;
				ListCursorCardFragment.selection = null;
				ListCursorCardFragmentTime.selection = "TaskLocationName = \"\"";
				ListCursorCardFragmentLocal.selection = "TaskLocationName <> \"\"";

				// �Ҧ� - �����ηs�WFragment
				switch (MODE) {
				case 0:
					// Remove Fragment
					switch (FragmentPosition) {
					case 0:
						fragmentTransaction.remove(cardview);
						break;
					case 1:
						fragmentTransaction.remove(cardview1);
						break;
					case 2:
						fragmentTransaction.remove(cardview2);
						break;
					}
					fragmentTransaction.commit();
					break;

				case 1:
					// Replace Fragment
					switch (FragmentPosition) {
					case 0:
						fragmentTransaction.replace(R.id.fragment_local, cardview2,"cardview2");
						break;
					case 1:
						fragmentTransaction.replace(R.id.fragment_time, cardview1,"cardview1");
						break;
					case 2:
						fragmentTransaction.replace(R.id.fragment_main, cardview,"cardview");
						break;
					}
					fragmentTransaction.commit();
					break;
				case 3:
					int i;
					for (i = 0; i < (adapter.getCount())-1; i++) {
						setFragment(0, i);
						setFragment(1, i);
					}
					break;
				}

				setLoding("OFF");
			}
		}).start();

	}

	/**********************/
	/** setViewComponent **/
	/**********************/
	private void setDataToEditor() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadID = android.os.Process
						.getThreadPriority(android.os.Process.myTid());
				CommonUtils.debugMsg(1, +threadID + " setDatatoEditor");

			}
		}).start();
	}

	/************************/
	/** set Lode dialog On **/
	/************************/
	private void setLoding(String MODE) {
		if (MODE == "ON") {
			psDialog = ProgressDialog.show(this, "", "......");
			/*
			 * if (!(pBar == null)) { pBar.setVisibility(View.VISIBLE); }
			 */
		} else if (MODE == "OFF") {
			psDialog.dismiss();
			/*
			 * if (!(pBar == null)) { pBar.setVisibility(View.GONE); }
			 */
		}

	}

	/**********************/
	/** btnActionAddClick **/
	/**********************/
	private MenuItem.OnMenuItemClickListener btnActionAddClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {

			Toast.makeText(getApplication(), item.getTitle(),
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(getApplication(), RemindmeTaskEditor.class);
			startActivity(intent);

			return false;
		}
	};

	/************************/
	/** btnRefreshAddClick **/
	/************************/
	private MenuItem.OnMenuItemClickListener btnRefreshClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Toast.makeText(getApplication(), item.getTitle(),
					Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent();
			// intent.setClass(getApplication(), testcard.class);
			// startActivity(intent);
			setLoding("ON");
			setFragment(3, 0);

			return false;
		}
	};

	/**********************/
	/** menuActionPrefClick **/
	/**********************/
	private MenuItem.OnMenuItemClickListener btnPrefClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// Display the fragment as the main content.

			Toast.makeText(getApplication(), item.getTitle(),
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), RemindmePreference.class);
			startActivity(intent);

			return false;
		}
	};

	/**************************/
	/** Class MyPagerAdapter **/
	/**************************/
	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		// �w�q�Utab�W��
		private final int[] TITLES = { R.string.tab1, R.string.tab2,
				R.string.tab3 };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		// This displays tab names and how to switch, if you remove a tab,
		// remove its case below but make sure to keep the order (0,1,2)
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.tab1).toUpperCase(l);
			case 1:
				return getString(R.string.tab2).toUpperCase(l);
			case 2:
				return getString(R.string.tab3).toUpperCase(l);
			}
			return null;
		}

		// These are the tabs in the main display, if you remove a tab
		// (fragment) you must remove it from below. also, ensure you keep the
		// cases in order or it will not know what to do
		@Override
		public Fragment getItem(int position) {
			Fragment f = new Fragment();
			switch (position) {
			case 0:
				f = new CardFragmentLoader0();
				setFragment(1, 0);
				break;
			case 1:
				f = new CardFragmentLoader1();
				setFragment(1, 1);
				break;
			case 2:
				f = new CardFragmentLoader2();
				setFragment(1, 2);
				break;
			}
			return f;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}
	};
}