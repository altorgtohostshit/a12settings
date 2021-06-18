package com.android.settings.privacy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.dashboard.profileselector.UserAdapter;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.utils.ContentCaptureUtils;
import java.util.ArrayList;
import java.util.List;

public final class EnableContentCaptureWithServiceSettingsPreferenceController extends TogglePreferenceController {
    private static final String TAG = "ContentCaptureController";
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EnableContentCaptureWithServiceSettingsPreferenceController(Context context, String str) {
        super(context, str);
        this.mUserManager = UserManager.get(context);
    }

    public boolean isChecked() {
        return ContentCaptureUtils.isEnabledForUser(this.mContext);
    }

    public boolean setChecked(boolean z) {
        ContentCaptureUtils.setEnabledForUser(this.mContext, z);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ComponentName serviceSettingsComponentName = ContentCaptureUtils.getServiceSettingsComponentName();
        if (serviceSettingsComponentName != null) {
            preference.setIntent(new Intent("android.intent.action.MAIN").setComponent(serviceSettingsComponentName));
        } else {
            Log.w(TAG, "No component name for custom service settings");
            preference.setSelectable(false);
        }
        preference.setOnPreferenceClickListener(new C1238xa5b4eea8(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateState$0(Preference preference) {
        ProfileSelectDialog.show(this.mContext, preference);
        return true;
    }

    public int getAvailabilityStatus() {
        if (ContentCaptureUtils.isFeatureAvailable() && ContentCaptureUtils.getServiceSettingsComponentName() != null) {
            return 0;
        }
        return 3;
    }

    private static final class ProfileSelectDialog {
        public static void show(Context context, Preference preference) {
            UserManager userManager = UserManager.get(context);
            List<UserInfo> users = userManager.getUsers();
            ArrayList arrayList = new ArrayList(users.size());
            for (UserInfo userHandle : users) {
                arrayList.add(userHandle.getUserHandle());
            }
            if (arrayList.size() == 1) {
                context.startActivityAsUser(preference.getIntent().addFlags(32768), (UserHandle) arrayList.get(0));
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((int) R.string.choose_profile).setAdapter(UserAdapter.createUserAdapter(userManager, context, arrayList), new C1239xde084dd1(arrayList, preference, context)).show();
        }
    }
}
