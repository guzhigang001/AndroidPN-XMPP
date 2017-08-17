package org.androidpn.client;

import org.androidpn.demoapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BootCompleteReceviver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Boot", Toast.LENGTH_SHORT).show();
		SharedPreferences sPreferences=context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (sPreferences.getBoolean(Constants.SETTINGS_AUTO_START, true)) {
			     ServiceManager serviceManager = new ServiceManager(context);
		        serviceManager.setNotificationIcon(R.drawable.notification);
		        serviceManager.startService();
		}
	}

}
