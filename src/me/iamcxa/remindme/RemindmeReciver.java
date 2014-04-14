package me.iamcxa.remindme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author iamcxa �w�ɴ����s��
 */
public class RemindmeReciver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setClass(context, RemindmeReciver.class);

		context.startActivity(intent);

	}
}