package com.android.settings.biometrics.face;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.LayoutPreference;

public class FaceSettingsEnrollButtonPreferenceController extends BasePreferenceController implements View.OnClickListener {
    static final String KEY = "security_settings_face_enroll_faces_container";
    private static final String TAG = "FaceSettings/Remove";
    private SettingsActivity mActivity;
    private Button mButton;
    private boolean mIsClicked;
    private Listener mListener;
    private byte[] mToken;
    private int mUserId;

    public interface Listener {
        void onStartEnrolling(Intent intent);
    }

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

    public FaceSettingsEnrollButtonPreferenceController(Context context) {
        this(context, KEY);
    }

    public FaceSettingsEnrollButtonPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        Button button = (Button) ((LayoutPreference) preference).findViewById(R.id.security_settings_face_settings_enroll_button);
        this.mButton = button;
        button.setOnClickListener(this);
    }

    public void onClick(View view) {
        this.mIsClicked = true;
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", FaceEnrollIntroduction.class.getName());
        intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
        intent.putExtra("hw_auth_token", this.mToken);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onStartEnrolling(intent);
        } else {
            this.mContext.startActivity(intent);
        }
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public void setToken(byte[] bArr) {
        this.mToken = bArr;
    }

    public boolean isClicked() {
        boolean z = this.mIsClicked;
        this.mIsClicked = false;
        return z;
    }

    public void setActivity(SettingsActivity settingsActivity) {
        this.mActivity = settingsActivity;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }
}
