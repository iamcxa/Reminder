/**
 * 
 */
package me.iamcxa.remindme;

import java.util.Calendar;
import java.util.Date;

import me.iamcxa.remindme.CommonUtils.RemindmeTaskCursor;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @author cxa
 * 
 */

public class RemindmeTaskEditor extends Activity {

	private static EditText tittlEditText;

	// �Ƨѿ��T���C��
	private ListView listView;
	// �������
	private static int mYear;
	private static int mMonth;
	private static int mDay;
	// �����ɶ�
	private static int mHour;
	private static int mMinute;
	// ������TextView
	private static TextView dateTittle;

	private static TextView dateDesc, locationDesc;
	// �ɶ����TextView
	private static TextView timeTittle, timeDesc;
	// �������eTextView
	private static TextView contentTittle;

	private static TextView contentDesc;

	private static TextView locationTittle;
	// �O�_�}�Ҵ���
	private int on_off = 0;
	// �O�_�n��ĵ�i
	private int alarm = 0;

	private static int target;

	private static EditText datePicker, timePicker, contentBox, locationBox;

	// ��ܤ���B�ɶ���ܤ���`��
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	// �O�s���e�B����P�ɶ��r��
	private static String content = null;

	private String switcher;

	private String date1;

	private String time1;

	private static String locationName;
	// �Ƨѿ�ID
	private int id1;
	// �h���
	private CheckedTextView ctv1, ctv2;
	// �s���G�����
	private static LayoutInflater li;

	// ��l�Ƥ�k
	private void init(Intent intent) {
		Bundle b = intent.getBundleExtra("b");
		if (b != null) {
			id1 = b.getInt("id");
			content = b.getString("content");
			date1 = b.getString("date1");
			time1 = b.getString("time1");
			on_off = b.getInt("on_off");
			alarm = b.getInt("alarm");

			if (date1 != null && date1.length() > 0) {
				String[] strs = date1.split("/");
				mYear = Integer.parseInt(strs[0]);
				mMonth = Integer.parseInt(strs[1]);
				mDay = Integer.parseInt(strs[2]);
			}

			if (time1 != null && time1.length() > 0) {
				String[] strs = time1.split(":");
				mHour = Integer.parseInt(strs[0]);
				mMinute = Integer.parseInt(strs[1]);
			}
		}

	}

	public View vv;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_editor);

		// ���oIntent
		final Intent intent = getIntent();
		// �]�wUri
		if (intent.getData() == null) {
			intent.setData(CommonUtils.CONTENT_URI);
		}

		/*
		 * //String[] ops={"�Ȧ�","���","�ʪ�","����","�S��"};
		 * 
		 * // spinner spinnerTag=(Spinner)findViewById(R.id.spinnerTag);
		 * ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this,
		 * android.R.layout.simple_spinner_item,ops);
		 * spinnerTag.setAdapter(spinnerAdapter); spinnerTag.setPrompt("�������");
		 */

		// ���D��J���
		tittlEditText = (EditText) findViewById(R.id.editTextTittle);
		tittlEditText.setHint("���D");
		tittlEditText.setHintTextColor(R.color.background_window);

		// ���oCalendar���
		final Calendar c = Calendar.getInstance();

		// ���o�ثe����B�ɶ�
		mYear = c.get(Calendar.YEAR);
		mMonth = (c.get(Calendar.MONTH)) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		// ���oListView
		listView = (ListView) (findViewById(R.id.listView1));
		// ��Ҥ�LayoutInflater
		li = getLayoutInflater();
		// �]�wListView Adapter
		try {
			listView.setAdapter(new ViewAdapter());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// �i�h��
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		// �^���C������ƥ�
		listView.setOnItemClickListener(ViewAdapterClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��l�ƦC��
		init(getIntent());
	}

	// ListView Adatper�A�����O��@�C���C�@���z�L�۩w���Ϲ�{
	static class ViewAdapter extends BaseAdapter {
		// �C���e
		String[] strs = { "�I���", "�����ɶ�", "�Ƶ�", "�a�I" };

		// ���o�C��ƶq
		// @Override
		@Override
		public int getCount() {
			return strs.length;
		}

		// ���o�C����
		// @Override
		@Override
		public Object getItem(int position) {
			return position;
		}

		// ��^�C��ID
		// @Override
		@Override
		public long getItemId(int position) {
			return position;
		}

		// ���o�ثe�C���ص���
		// @Override
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// �۩w�ܹ�layout
			View textView = li.inflate(
					R.layout.activity_event_editor_parts_textview, null);
			// View editView =
			// li.inflate(R.layout.activity_event_editor_parts_editview, null);
			switch (position) {
			// �O�_�}�Ҹӵ��Ƨѿ�
			/*
			 * case 0: ctv1 = (CheckedTextView) li .inflate(
			 * android.R.layout.simple_list_item_multiple_choice, null);
			 * ctv1.setText(strs[position]); if (on_off == 0) {
			 * ctv1.setChecked(false); } else { ctv1.setChecked(true); } return
			 * ctv1;
			 */
			// �������
			case 0:
				// datePicker=(EditText)v.findViewById(R.id.editTextbox);
				// datePicker.setHint("��Ĳ�H��ܤ��");
				// datePicker.setHintTextColor(R.color.background_window);
				// datePicker.setText(mYear + "/" + mMonth + "/" + mDay);

				dateTittle = (TextView) textView.findViewById(R.id.name);
				dateDesc = (TextView) textView.findViewById(R.id.desc);
				dateTittle.setText(strs[position]);
				dateDesc.setText(mYear + "/" + mMonth + "/" + mDay);
				return textView;
				// �����ɶ�
			case 1:
				// timePicker=(EditText)v.findViewById(R.id.editTextbox);
				// timePicker.setHint("��Ĳ�H��ܮɶ�");
				// timePicker.setHintTextColor(R.color.background_window);
				// timePicker.setText(mHour + ":" + mMinute);
				timeTittle = (TextView) textView.findViewById(R.id.name);
				timeDesc = (TextView) textView.findViewById(R.id.desc);
				timeTittle.setText(strs[position]);
				timeDesc.setText(mHour + ":" + mMinute);
				return textView;
				// �������e
			case 2:
				// contentBox=(EditText)
				// editView.findViewById(R.id.editTextbox);
				// contentBox.setHint("��Ĳ�H��J���e");
				// contentBox.setHintTextColor(R.color.background_window);
				// contentBox.setText(content);

				contentTittle = (TextView) textView.findViewById(R.id.name);
				contentDesc = (TextView) textView.findViewById(R.id.desc);
				contentTittle.setText(strs[position]);
				contentDesc.setText(content);

				contentDesc.setTextColor(Color.GRAY);
				return textView;
				// �a�I��ܻP��J
			case 3:
				/*
				 * locationBox=(EditText)
				 * editView.findViewById(R.id.editTextbox);
				 * locationBox.setHint("��Ĳ�H��J�a�I");
				 * locationBox.setHintTextColor(R.color.background_window);
				 * locationBox.setText(locationName); locationTittle =
				 * (TextView) editView.findViewById(R.id.name);
				 * locationTittle.setText(strs[position]);
				 */
				locationTittle = (TextView) textView.findViewById(R.id.name);
				locationDesc = (TextView) textView.findViewById(R.id.desc);
				locationTittle.setText(strs[position]);

				locationDesc.setText(locationName);
				contentDesc.setTextColor(Color.GRAY);

				return textView;
				// �O�_�}�Ҵ���
				/*
				 * case 5: ctv2 = (CheckedTextView) li .inflate(
				 * android.R.layout.simple_list_item_multiple_choice, null);
				 * ctv2.setText(strs[position]);
				 * 
				 * if (alarm == 0) { ctv2.setChecked(false); } else {
				 * ctv2.setChecked(true); } return ctv2;
				 */
			default:
				break;
			}

			return null;
		}
	}

	private OnItemClickListener ViewAdapterClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int position, long id) {

			switch (position) {
			// �]�w�O�_�}�Ҵ���
			/*
			 * case 0:
			 * 
			 * ctv1 = (CheckedTextView) v; if (ctv1.isChecked()) { on_off = 0; }
			 * else { on_off = 1; }
			 * 
			 * break;
			 */
			// �]�w�������
			case 0:
				showDialog(DATE_DIALOG_ID);
				break;
			// �]�w�����ɶ�
			case 1:
				showDialog(TIME_DIALOG_ID);
				break;
			// �]�w�������e
			case 2:
				showDialog1("�п�J���e�G", "���e", position);
				break;

			// �]�w�O�_�}�һy������
			case 3:
				/*
				 * ctv2 = (CheckedTextView) v; if (ctv2.isChecked()) { alarm =
				 * 0; setAlarm(false); } else { alarm = 1; setAlarm(true); }
				 */

				showDialog1("�п�J�a�I�G", "�a�I", position);
				break;

			default:
				break;
			}
		}

	};

	// ��ܹ�ܤ��
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// ��ܤ����ܤ��
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth - 1, mDay);
			// ��ܮɶ���ܤ��
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}

	// �]�w�q������
	private void setAlarm(boolean flag) {
		final String BC_ACTION = "com.amaker.ch17.TaskReceiver";
		// ���oAlarmManager���
		final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// ��Ҥ�Intent
		Intent intent = new Intent();
		// �]�wIntent action�ݩ�
		intent.setAction(BC_ACTION);
		intent.putExtra("msg", content);
		// ��Ҥ�PendingIntent
		final PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		// ���o�t�ήɶ�
		final long time1 = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.set(mYear, mMonth, mDay, mHour, mMinute);
		long time2 = c.getTimeInMillis();
		if (flag && (time2 - time1) > 0 && on_off == 1) {
			am.set(AlarmManager.RTC_WAKEUP, time2, pi);
		} else {
			am.cancel(pi);
		}
	}

	/*
	 * �]�w���ܤ����ܤ��
	 */
	private void showDialog1(String msg, String tittle, int target) {
		View v = li
				.inflate(R.layout.activity_event_editor_parts_editview, null);
		final TextView editTextTittle = (TextView) v.findViewById(R.id.name);
		final EditText editTextbox = (EditText) v
				.findViewById(R.id.editTextbox);
		editTextTittle.setText(tittle + target);

		switch (target) {
		case 2:
			switcher = content;
			break;
		case 3:
			switcher = locationName;
		default:
			break;
		}

		editTextbox.setText(switcher);

		new AlertDialog.Builder(this).setView(v).setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						content = editTextbox.getText().toString();

						contentDesc.setText(switcher);
						locationDesc.setText(switcher);
					}
				}).show();

	}

	// �ɶ���ܹ�ܤ��
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			timeDesc.setText(mHour + ":" + mMinute);
		}
	};
	// �����ܹ�ܤ��
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			dateDesc.setText(mYear + "/" + mMonth + "/" + mDay);
		}
	};

	// �x�s�έק�Ƨѿ���T
	@Override
	protected void onPause() {
		super.onPause();
		locationName = null;
		content = null;
	};

	// �x�s�έק�Ƨѿ���T
	private boolean saveOrUpdate() {
		try {
			Date curDate = new Date(System.currentTimeMillis());

			ContentValues values = new ContentValues();

			values.clear();

			// �s�J���D
			values.put(RemindmeTaskCursor.KeyColumns.Tittle, tittlEditText
					.getText().toString());
			// �s�J���
			values.put(RemindmeTaskCursor.KeyColumns.StartDate,
					curDate.toString());
			values.put(RemindmeTaskCursor.KeyColumns.EndDate, dateDesc
					.getText().toString());
			// save the selected value of time
			values.put(RemindmeTaskCursor.KeyColumns.StartTime,
					curDate.toString());
			values.put(RemindmeTaskCursor.KeyColumns.EndTime, timeDesc
					.getText().toString());
			// save contents
			values.put(RemindmeTaskCursor.KeyColumns.CONTENT, contentDesc
					.getText().toString());
			// save the name string of location
			values.put(RemindmeTaskCursor.KeyColumns.LocationName, locationDesc
					.getText().toString());
			// save the value of loaction picker
			/*
			 * values.put(RemindmeTasks.EndDate, dateDesc.getText().toString());
			 * values.put(RemindmeTasks.StartTime,
			 * timeDesc.getText().toString()); values.put(RemindmeTasks.EndTime,
			 * timeDesc.getText().toString());
			 * 
			 * values.put(RemindmeTasks.Is_Alarm_ON, ctv1.isChecked() ? 1 : 0);
			 * values.put(RemindmeTasks.Is_Hide_ON, ctv2.isChecked() ? 1 : 0);
			 */
			// �ק�
			if (id1 != 0) {
				Uri uri = ContentUris.withAppendedId(CommonUtils.CONTENT_URI,
						id1);
				getContentResolver().update(uri, values, null, null);
				Toast.makeText(this, "�ƶ���s���\�I" + curDate.toString(),
						Toast.LENGTH_SHORT).show();
				// �x�s
			} else {
				Uri uri = CommonUtils.CONTENT_URI;
				getContentResolver().insert(uri, values);
				Toast.makeText(this, "�s�ƶ��w�g�x�s" + curDate.toString(),
						Toast.LENGTH_SHORT).show();
			}
			setAlarm(true);
			return true;
		} catch (Exception e) {
			Toast.makeText(getApplication(), "�x�s�X���I", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

	}

	// This is the action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���editor_activity_actionbar.xml���e
		getMenuInflater().inflate(R.menu.editor_activity_actionbar, menu);

		// �ҥ�actionbar��^�����b�Y
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("�إ߫ݿ�ƶ�");

		// actionAdd
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		actionAdd.setOnMenuItemClickListener(btnActionAddClick);

		// actionCancel
		MenuItem actionCancel = menu.findItem(R.id.action_cancel);
		actionCancel.setOnMenuItemClickListener(btnActionCancelClick);

		return true;
	}

	// actionbar�b�Y��^�����ʧ@
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 
	 */
	private MenuItem.OnMenuItemClickListener btnActionAddClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			saveOrUpdate();
			finish();
			return true;
		}

	};

	/*
	 * 
	 */
	private MenuItem.OnMenuItemClickListener btnActionCancelClick = new MenuItem.OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub

			// Intent EventEditor = new Intent();
			// EventEditor.setClass(getApplication(),RemindmeTaskEditorActivity.class);
			// EventEditor.setClass(getApplication(), IconRequest.class);
			// startActivity(EventEditor);

			// saveOrUpdate();
			finish();

			return false;
		}

	};

}

// * CLASS JUST FOR THE CUSTOM ALERT DIALOG
class CustomAlertDialog extends AlertDialog {
	public CustomAlertDialog(Context context) {
		super(context);
	}
}
