/**
 * 
 */
package me.iamcxa.remindme;

import java.io.OptionalDataException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author cxa
 * 
 */
public class CommonUtils {

	// ���v�`��
	public static final String AUTHORITY = "me.iamcxa.remindme";

	// URI�`��
	public static final String TASKLIST = "remindmetasklist";

	// �s��Uri
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TASKLIST);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.iamcxa"
			+ "." + TASKLIST;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.iamcxa"
			+ "." + TASKLIST;

	// �w�]�ƧǱ`��
	public static final String DEFAULT_SORT_ORDER = "created DESC";

	// debug msg TAG
	public static final String debugMsgTag = "debugmsg";
	// debug msg on/off
	public static final int debugMsg = 1;

	private CommonUtils() {
	}

	/***********************/
	/** debug msg section **/
	/***********************/
	public static final void debugMsg(int section, String msgs) {
		if (debugMsg == 1) {
			switch (section) {
			case 0:
				Log.w(debugMsgTag, " " + msgs);
				break;
			case 1:
				Log.w(debugMsgTag, "thread " + msgs);
				break;
			case 999:
				Log.w(debugMsgTag, "�ɶ��p�⥢��!," + msgs);
				break;
			default:
				break;
			}
		}

	}

	/***********************/
	/** getDaysLeft **/
	/***********************/
	@SuppressLint("SimpleDateFormat")
	public static long getDaysLeft(String TaskDate) {

		// �w�q�ɶ��榡
		// java.text.SimpleDateFormat sdf = new
		// java.text.SimpleDateFormat("yyyy/MM/dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		// ���o�{�b�ɶ�
		Date now = new Date();
		String nowDate = sdf.format(now);
		debugMsg(0, "now:" + nowDate + ", task:" + TaskDate);
		try {
			// ���o�ƥ�ɶ��P�{�b�ɶ�
			Date dt1 = sdf.parse(nowDate);
			Date dt2 = sdf.parse(TaskDate);

			// ���o��Ӯɶ���Unix�ɶ�
			Long ut1 = dt1.getTime();
			Long ut2 = dt2.getTime();

			Long timeP = ut2 - ut1;// �@��t
			// �۴���o��Ӯɶ��t�Z���@��
			// Long sec = timeP / 1000;// ��t
			// Long min = timeP / 1000 * 60;// ���t
			// Long hr = timeP / 1000 * 60 * 60;// �ɮt
			Long day = timeP / (1000 * 60 * 60 * 24);// ��t
			debugMsg(0, "Get days left Sucessed! " + day);
			return day;
		} catch (Exception e) {
			// TODO: handle exception
			debugMsg(999, e.toString());
			return -1;
		}

	}

	// �������O
	public static final class RemindmeTaskCursor implements BaseColumns {

		private RemindmeTaskCursor() {
		}

		// �d�����}�C
		public static final String[] PROJECTION = new String[] {
				KeyColumns.KEY_ID, // 0
				KeyColumns.Tittle, // 1
				KeyColumns.StartDate, // 2
				KeyColumns.EndDate,// 3
				KeyColumns.StartTime, // 4
				KeyColumns.EndTime,// 5
				KeyColumns.Is_Repeat, // 6
				KeyColumns.Is_AllDay,// 7
				KeyColumns.LocationName, // 8
				KeyColumns.Coordinates,// 9
				KeyColumns.Distance,// 10
				KeyColumns.CONTENT,// 11
				KeyColumns.CREATED,// 12
				KeyColumns.Is_Alarm_ON, // 13
				KeyColumns.Is_Hide_ON,// 14
				KeyColumns.Is_PW_ON,// 15
				KeyColumns.Password,// 16
				KeyColumns.PriorityWeight,// 17
				KeyColumns.Collaborators,// 18
				KeyColumns.CalendarID,// 19
				KeyColumns.GoogleCalSyncID,// 20
				KeyColumns.other // 21
		};

		public static class IndexColumns {
			public static final int KEY_ID = 0;
			public static final int Tittle = 1;
			public static final int StartDate = 2;
			public static final int EndDate = 3;
			public static final int StartTime = 4;
			public static final int EndTime = 5;
			public static final int Is_Repeat = 6;
			public static final int Is_AllDay = 7;
			public static final int LocationName = 8;
			public static final int Coordinates = 9;
			public static final int Distance = 10;
			public static final int CalendarID = 11;
			public static final int CONTENT = 12;
			public static final int CREATED = 13;
			public static final int Is_Alarm_ON = 14;
			public static final int Is_Hide_ON = 15;
			public static final int Is_PW_ON = 16;
			public static final int Password = 17;
			public static final int PriorityWeight = 18;
			public static final int Collaborators = 19;
			public static final int GoogleCalSyncID = 20;
			public static final int other = 21;
			// public static final int AlarmSoundPath = "AlarmSoundPath";
		}

		// ��L���`��
		public static class KeyColumns {
			// 00 index ID
			public static final String KEY_ID = "_id";
			// 01 �ݭn�P�B�� Google calender���ID
			public static final String GoogleCalSyncID = "GoogleCalSyncID";
			// 02 �ƥ���D
			public static final String Tittle = "tittle";
			// 03 �}�l�P�����ɶ�
			public static final String StartTime = "StartTime";
			public static final String EndTime = "EndTime";
			// 04 �}�l�P�������
			public static final String StartDate = "StartDate";
			public static final String EndDate = "EndDate";
			// 05 �}�� - �ƥ�O�_����
			public static final String Is_Repeat = "Is_Repeat";
			// 06 �}�� - �O�_�����Ѩƥ�
			public static final String Is_AllDay = "Is_AllDay";
			// 07 �ƥ�a�I�]�W�١^
			public static final String LocationName = "TaskLocationName";
			// 08 �ƥ�y��
			public static final String Coordinates = "Coordinates";
			// 09 �ƥ�P��U�ϥΪ̦a�I���Z��
			public static final String Distance = "Distance";
			// 10 ��䥻����ID(�ƥ�)
			public static final String CalendarID = "CalendarID";
			// 11 �ƥ󤺮e�]��r�^
			public static final String CONTENT = "content";
			// 12 �إ߮ɶ�
			public static final String CREATED = "created";
			// 13 �}�� - �O�_����
			public static final String Is_Alarm_ON = "Is_Alarm_ON";
			// 14 �}�� - �ƥ�O�_����
			public static final String Is_Hide_ON = "Is_Hide_ON";
			// 15 �}�� - �O�_�[�K
			public static final String Is_PW_ON = "Is_PW_ON";
			// 16 �K�X
			public static final String Password = "password";
			// 17 �Y���v��
			public static final String PriorityWeight = "PriorityWeight";
			// 18 ��@��GMAIL
			public static final String Collaborators = "Collaborators";
			// 19 �ƥ�
			public static final String other = "other";
			// 18 �������n���ɮ׸��|
			// public static final String AlarmSoundPath = "AlarmSoundPath";
		}
	}
}
