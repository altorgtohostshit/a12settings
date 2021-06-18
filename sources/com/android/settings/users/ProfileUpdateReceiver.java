package com.android.settings.users;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settings.R;
import com.android.settings.Utils;

public class ProfileUpdateReceiver extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        new Thread() {
            public void run() {
                UserSettings.copyMeProfilePhoto(context, (UserInfo) null);
                ProfileUpdateReceiver.copyProfileName(context);
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public static void copyProfileName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", 0);
        if (!sharedPreferences.contains("name_copied_once")) {
            int myUserId = UserHandle.myUserId();
            UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
            String meProfileName = Utils.getMeProfileName(context, false);
            if (meProfileName != null && meProfileName.length() > 0 && !isCurrentNameInteresting(context, userManager)) {
                userManager.setUserName(myUserId, meProfileName);
                sharedPreferences.edit().putBoolean("name_copied_once", true).commit();
            }
        }
    }

    private static boolean isCurrentNameInteresting(Context context, UserManager userManager) {
        String str;
        String userName = userManager.getUserName();
        if (userManager.isRestrictedProfile() || userManager.isProfile()) {
            str = context.getString(R.string.user_new_profile_name);
        } else {
            str = context.getString(R.string.user_new_user_name);
        }
        return userName != null && !userName.equals(str);
    }
}
