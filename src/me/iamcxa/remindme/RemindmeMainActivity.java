/*
 * 
 */
package me.iamcxa.remindme;

import java.security.PublicKey;
import java.util.Locale;

import me.iamcxa.remindme.cardset.BaseFragment;
import me.iamcxa.remindme.cardset.ListCursorCardFragment;
import me.iamcxa.remindme.fragment.CardViewFragment;
import me.iamcxa.remindme.fragment.ListviewFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

/**
 * @author cxa Main Activity
 */
public class RemindmeMainActivity extends FragmentActivity {

	/*
	 * ********************************************************************************************** *
	 * *
	 * Variables * *
	 * ************************************************************
	 * **********************************
	 */
    //Used in savedInstanceState
    private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	public MyPagerAdapter adapter;
	private ShareActionProvider mShareActionProvider;
	// private Intent mShareIntent;
	private SearchView mSearchView;

	/*
	 * ********************************************************************************************** *
	 * *
	 * onCreate �ҰʰϬq * *
	 * ********************************************************
	 * **************************************
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �]�w�G��������
		setViewComponent();

		// -----------------------------------------------------------------
		// BaseFragment baseFragment = null;
		if (savedInstanceState != null) {
			replaceFragment();
		}

		// -----------------------------------------------------------------

	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT,pager.getCurrentItem());
		Toast.makeText(getApplication(), BUNDLE_SELECTEDFRAGMENT+","+pager.getCurrentItem()+adapter.toString(), 200).show();
    }
	/*
	 * ********************************************************************************************** *
	 * *
	 * setViewComponent �Ϭq, �]�w�G�������� * *
	 * *****************************************
	 * *****************************************************
	 */
	private void setViewComponent() {
		// �]�w�Glayout����
		setContentView(R.layout.activity_main);

		// �w�q�Gtabs
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		// �]�w�G����margin
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		// �]�w�Gtab�Ppage��adapter
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
	}

	/*
	 * ********************************************************************************************** *
	 * *
	 * onCreateOptionsMenu �Ϭq, �]�w�GAction Bar�W�����s * *
	 * ****************************
	 * ******************************************************************
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ��main_activity_actionbar.xmlŪ�����s��T
		getMenuInflater().inflate(R.menu.main_activity_actionbar, menu);

		// �w�q�G�j�M���s
		MenuItem actionSearch = menu.findItem(R.id.action_search);
		// �]�w�G�I��������
		mSearchView = (SearchView) actionSearch.getActionView();
		setupSearchView(actionSearch);

		// �w�q�G�s�W���s
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		// �]�w�G�I��������
		actionAdd.setOnMenuItemClickListener(btnActionAddClick);

		// �w�q�G������s
		MenuItem actionRefresh = menu.findItem(R.id.action_refresh);
		// �]�w�G�I��������
		actionRefresh.setOnMenuItemClickListener(btnRefreshAddClick);

		// �w�q�G���ɫ��s
		MenuItem actionShare = menu.findItem(R.id.action_share);
		// �]�w�Gplay�W��app�s��/���ɵ��B�ͪ���ܦr��
		String playStoreLink = " https://play.google.com/store/apps/details?id="
				+ getPackageName();
		String yourShareText = getString(R.string.share_this) + playStoreLink;
		// �]�w�G����intent
		Intent shareIntent = ShareCompat.IntentBuilder.from(this)
				.setType("text/plain").setText(yourShareText).getIntent();
		// �]�w�G�I�����Ѫ�
		mShareActionProvider = (ShareActionProvider) actionShare
				.getActionProvider();
		mShareActionProvider.setShareIntent(shareIntent);

		// �w�q�G�]�m���s
		MenuItem actionPref = menu.findItem(R.id.action_settings);
		// �]�w�G�I��������
		actionPref.setOnMenuItemClickListener(menuActionPrefClick);

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * ********************************************************************************************** *
	 * *
	 * OnMenuItemClickListener:btnActionAddClick �Ϭq, �]�wActionBar�W�s�W���s�ʧ@ * *
	 * *****
	 * *********************************************************************
	 * ********************
	 */
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

	private MenuItem.OnMenuItemClickListener btnRefreshAddClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Toast.makeText(getApplication(), item.getTitle(),
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(getApplication(), testcard.class);
			startActivity(intent);
			return false;
		}
	};

	/*
	 * ********************************************************************************************** *
	 * *
	 * OnMenuItemClickListener:menuActionPrefClick�Ϭq, �]�wActionBar�W�]�w���s�ʧ@ * *
	 * ****
	 * **********************************************************************
	 * ********************
	 */
	private MenuItem.OnMenuItemClickListener menuActionPrefClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// Display the fragment as the main content.

			Toast.makeText(getApplication(), item.getTitle(),
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent();
			intent.setClass(getApplication(), RemindmePreference.class);
			startActivity(intent);

			return false;
		}
	};

	/*
	 * ********************************************************************************************** *
	 * *
	 * OsetupSearchView�Ϭq, �]�wActionBar�W�j�M���s�Ұʫᤧ�ʧ@ * *
	 * ***************************
	 * *******************************************************************
	 */
	private void setupSearchView(MenuItem searchItem) {
		// TODO Auto-generated method stub

	}

	private void replaceFragment() {
		android.app.FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		ListCursorCardFragment fragment11 = new ListCursorCardFragment();
		fragmentTransaction.replace(R.id.fragment_main, fragment11);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	/*
	 * ********************************************************************************************** *
	 * *
	 * MyPagerAdapter���O, �w�qactiviry�W��tab/pager * *
	 * ******************************
	 * ****************************************************************
	 */
	public class MyPagerAdapter extends FragmentPagerAdapter {
		// �w�q�Utab�W��
		private final int[] TITLES = { R.string.tab1, R.string.tab2 };

		// private final int[] TITLES = { R.string.tab1, R.string.tab2,
		// R.string.tab3, R.string.tab4 };
		// private final int[] TITLES = { R.string.tab1, R.string.tab2,
		// R.string.tab3 ,655};

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
				/*
				 * case 2: return getString(R.string.tab3).toUpperCase(l); case
				 * 3: return getString(R.string.tab4).toUpperCase(l);
				 */
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
				f = new CardViewFragment();
				replaceFragment();
				break;

			case 1:
				f = new ListviewFragment();
				break;

			/*
			 * case 2: f= new DistanceFragment(); break; case 3: f= new
			 * OverallFragment(); break;
			 */
			}
			return f;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}
		

		public int getPosion(Object object) {
			return getItemPosition(object);
			
		}


	}

}