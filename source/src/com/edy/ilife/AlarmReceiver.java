package com.edy.ilife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.edy.ilife.R;
import com.edy.ilife.model.Alarm;
import com.edy.ilife.model.AlarmMsg;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long alarmMsgId = intent.getLongExtra(AlarmMsg.COL_ID, -1);
		long alarmId = intent.getLongExtra(AlarmMsg.COL_ALARMID, -1);
		
		AlarmMsg alarmMsg = new AlarmMsg(alarmMsgId);
		alarmMsg.setStatus(AlarmMsg.EXPIRED);
		alarmMsg.persist(RemindMe.db);
		
		Alarm alarm = new Alarm(alarmId);
		alarm.load(RemindMe.db);
		
		Notification n = new Notification(R.drawable.ic_launcher, alarm.getName(), System.currentTimeMillis());
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
		
		n.setLatestEventInfo(context, "Remind Me", alarm.getName(), pi);		
		if (RemindMe.isVibrate()) {
			n.defaults |= Notification.DEFAULT_VIBRATE;
		}
		if (alarm.getSound()) {
			n.sound = Uri.parse(RemindMe.getRingtone());
		}		
		n.flags |= Notification.FLAG_AUTO_CANCEL;		
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify((int)alarmMsgId, n);
	}

}
