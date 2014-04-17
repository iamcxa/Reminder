package me.iamcxa.remindme;

import me.iamcxa.remindme.service.TaskSortingService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author iamcxa �w�ɴ����s��
 */
public class RemindmeBootReciver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setClass(context, TaskSortingService.class);
		
		CommonUtils.debugMsg(0,"�ǳƱҰ�TaskSortingService");
		try {
			context.startActivity(intent);

			CommonUtils.debugMsg(0,"TaskSortingService �Ұʧ���");
		} catch (Exception e) {
			// TODO: handle exception
			CommonUtils.debugMsg(0,"�Ұ�TaskSortingService���ѡIerror="+e.toString());
		}
	}
}