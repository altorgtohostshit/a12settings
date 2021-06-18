package com.android.settings.notification.zen;

import android.content.Context;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.LayoutPreference;

public class ZenModeSendersImagePreferenceController extends AbstractZenModePreferenceController {
    private ImageView mImageView;
    private final boolean mIsMessages;

    public boolean isAvailable() {
        return true;
    }

    public ZenModeSendersImagePreferenceController(Context context, String str, Lifecycle lifecycle, boolean z) {
        super(context, str, lifecycle);
        this.mIsMessages = z;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mImageView = (ImageView) ((LayoutPreference) preferenceScreen.findPreference(this.KEY)).findViewById(R.id.zen_mode_settings_senders_image);
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void updateState(Preference preference) {
        int i;
        String str;
        int prioritySenders = getPrioritySenders();
        if (prioritySenders == 0) {
            i = this.mIsMessages ? R.drawable.zen_messages_any : R.drawable.zen_calls_any;
            str = this.mContext.getString(R.string.zen_mode_from_anyone);
        } else if (1 == prioritySenders) {
            i = this.mIsMessages ? R.drawable.zen_messages_contacts : R.drawable.zen_calls_contacts;
            str = this.mContext.getString(R.string.zen_mode_from_contacts);
        } else if (2 == prioritySenders) {
            i = this.mIsMessages ? R.drawable.zen_messages_starred : R.drawable.zen_calls_starred;
            str = this.mContext.getString(R.string.zen_mode_from_starred);
        } else {
            boolean z = this.mIsMessages;
            int i2 = z ? R.drawable.zen_messages_none : R.drawable.zen_calls_none;
            str = this.mContext.getString(z ? R.string.zen_mode_none_messages : R.string.zen_mode_none_calls);
            i = i2;
        }
        this.mImageView.setImageResource(i);
        this.mImageView.setContentDescription(str);
    }

    private int getPrioritySenders() {
        if (this.mIsMessages) {
            return this.mBackend.getPriorityMessageSenders();
        }
        return this.mBackend.getPriorityCallSenders();
    }
}
