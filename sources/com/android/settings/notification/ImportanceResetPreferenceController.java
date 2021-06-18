package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.LayoutPreference;

public class ImportanceResetPreferenceController extends BasePreferenceController implements View.OnClickListener {
    public static final String KEY = "asst_importance_reset";
    private static final String TAG = "ResetImportanceButton";
    private NotificationBackend mBackend = new NotificationBackend();
    private Button mButton;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ImportanceResetPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        Button button = (Button) ((LayoutPreference) preference).findViewById(R.id.reset_importance_button);
        this.mButton = button;
        button.setOnClickListener(this);
    }

    public void onClick(View view) {
        this.mBackend.resetNotificationImportance();
        Toast.makeText(this.mContext, R.string.reset_importance_completed, 0).show();
    }
}
