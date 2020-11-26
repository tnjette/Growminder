package com.tylerjette.growmindv05;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by tylerjette on 3/8/18.
 * This class is activated when the user's phone reboots, via the RECEIVE_REBOOT action specification in the App's Android Manifest file.
 * This class in turn activates the NotificationRebootService class which resets the notifications that were otherwise lost when the user's
 * phone rebooted.
 */

public class GrowmindRebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /**differentiate between android versions */
        if(Build.VERSION.SDK_INT >= 26){
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                NotificationRebootService.enqueueWork(context, new Intent());
            } else {
            }
        }else {
            /**this is old, which worked on older versions, just simply starts the background service*/
            Intent serviceIntent = new Intent(context, NotificationRebootService.class);
            serviceIntent.putExtra("caller", "RebootReceiver");
            context.startService(serviceIntent);
        }
        /**this line produces a Invalid State exception*/
        /**
         * to start this process in android-O, for some reason starting a service from a backrgound task is no longer allowed in version O
         * (found: https://www.truiton.com/2014/10/android-foreground-service-example/)
         * */
    }
}
