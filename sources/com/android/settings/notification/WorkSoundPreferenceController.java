package com.android.settings.notification;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import com.android.settings.DefaultRingtonePreference;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.List;

public class WorkSoundPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener, LifecycleObserver, OnResume, OnPause {
    private final AudioHelper mHelper;
    private int mManagedProfileId;
    private final BroadcastReceiver mManagedProfileReceiver;
    private final SoundSettings mParent;
    private final UserManager mUserManager;
    private final boolean mVoiceCapable;
    private Preference mWorkAlarmRingtonePreference;
    private Preference mWorkNotificationRingtonePreference;
    private Preference mWorkPhoneRingtonePreference;
    private PreferenceGroup mWorkPreferenceCategory;
    private TwoStatePreference mWorkUsePersonalSounds;

    public String getPreferenceKey() {
        return "sound_work_settings_section";
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        return false;
    }

    public WorkSoundPreferenceController(Context context, SoundSettings soundSettings, Lifecycle lifecycle) {
        this(context, soundSettings, lifecycle, new AudioHelper(context));
    }

    WorkSoundPreferenceController(Context context, SoundSettings soundSettings, Lifecycle lifecycle, AudioHelper audioHelper) {
        super(context);
        this.mManagedProfileReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int identifier = ((UserHandle) intent.getExtra("android.intent.extra.USER")).getIdentifier();
                String action = intent.getAction();
                action.hashCode();
                if (action.equals("android.intent.action.MANAGED_PROFILE_ADDED")) {
                    WorkSoundPreferenceController.this.onManagedProfileAdded(identifier);
                } else if (action.equals("android.intent.action.MANAGED_PROFILE_REMOVED")) {
                    WorkSoundPreferenceController.this.onManagedProfileRemoved(identifier);
                }
            }
        };
        this.mUserManager = UserManager.get(context);
        this.mVoiceCapable = Utils.isVoiceCapable(this.mContext);
        this.mParent = soundSettings;
        this.mHelper = audioHelper;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mWorkPreferenceCategory = (PreferenceGroup) preferenceScreen.findPreference("sound_work_settings_section");
    }

    public void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        this.mContext.registerReceiver(this.mManagedProfileReceiver, intentFilter);
        this.mManagedProfileId = this.mHelper.getManagedProfileId(this.mUserManager);
        updateWorkPreferences();
    }

    public void onPause() {
        this.mContext.unregisterReceiver(this.mManagedProfileReceiver);
    }

    public boolean isAvailable() {
        return this.mHelper.getManagedProfileId(this.mUserManager) != -10000 && shouldShowRingtoneSettings();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int i;
        if ("work_ringtone".equals(preference.getKey())) {
            i = 1;
        } else if ("work_notification_ringtone".equals(preference.getKey())) {
            i = 2;
        } else {
            if ("work_alarm_ringtone".equals(preference.getKey())) {
                i = 4;
            }
            return true;
        }
        preference.setSummary(updateRingtoneName(getManagedProfileContext(), i));
        return true;
    }

    public void updateNonIndexableKeys(List<String> list) {
        if (!isAvailable()) {
            list.add("sound_work_settings_section");
            list.add("work_use_personal_sounds");
            list.add("work_notification_ringtone");
            list.add("work_ringtone");
            list.add("work_alarm_ringtone");
        }
    }

    private boolean shouldShowRingtoneSettings() {
        return !this.mHelper.isSingleVolume();
    }

    private CharSequence updateRingtoneName(Context context, int i) {
        if (context == null || !this.mHelper.isUserUnlocked(this.mUserManager, context.getUserId())) {
            return this.mContext.getString(R.string.managed_profile_not_available_label);
        }
        return Ringtone.getTitle(context, RingtoneManager.getActualDefaultRingtoneUri(context, i), false, true);
    }

    private Context getManagedProfileContext() {
        int i = this.mManagedProfileId;
        if (i == -10000) {
            return null;
        }
        return this.mHelper.createPackageContextAsUser(i);
    }

    private DefaultRingtonePreference initWorkPreference(PreferenceGroup preferenceGroup, String str) {
        DefaultRingtonePreference defaultRingtonePreference = (DefaultRingtonePreference) preferenceGroup.findPreference(str);
        defaultRingtonePreference.setOnPreferenceChangeListener(this);
        defaultRingtonePreference.setUserId(this.mManagedProfileId);
        return defaultRingtonePreference;
    }

    private void updateWorkPreferences() {
        if (this.mWorkPreferenceCategory != null) {
            boolean isAvailable = isAvailable();
            this.mWorkPreferenceCategory.setVisible(isAvailable);
            if (isAvailable) {
                if (this.mWorkUsePersonalSounds == null) {
                    TwoStatePreference twoStatePreference = (TwoStatePreference) this.mWorkPreferenceCategory.findPreference("work_use_personal_sounds");
                    this.mWorkUsePersonalSounds = twoStatePreference;
                    twoStatePreference.setOnPreferenceChangeListener(new WorkSoundPreferenceController$$ExternalSyntheticLambda0(this));
                }
                if (this.mWorkPhoneRingtonePreference == null) {
                    this.mWorkPhoneRingtonePreference = initWorkPreference(this.mWorkPreferenceCategory, "work_ringtone");
                }
                if (this.mWorkNotificationRingtonePreference == null) {
                    this.mWorkNotificationRingtonePreference = initWorkPreference(this.mWorkPreferenceCategory, "work_notification_ringtone");
                }
                if (this.mWorkAlarmRingtonePreference == null) {
                    this.mWorkAlarmRingtonePreference = initWorkPreference(this.mWorkPreferenceCategory, "work_alarm_ringtone");
                }
                if (!this.mVoiceCapable) {
                    this.mWorkPhoneRingtonePreference.setVisible(false);
                    this.mWorkPhoneRingtonePreference = null;
                }
                if (Settings.Secure.getIntForUser(getManagedProfileContext().getContentResolver(), "sync_parent_sounds", 0, this.mManagedProfileId) == 1) {
                    enableWorkSyncSettings();
                } else {
                    disableWorkSyncSettings();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWorkPreferences$0(Preference preference, Object obj) {
        if (((Boolean) obj).booleanValue()) {
            UnifyWorkDialogFragment.show(this.mParent);
            return false;
        }
        disableWorkSync();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void enableWorkSync() {
        RingtoneManager.enableSyncFromParent(getManagedProfileContext());
        enableWorkSyncSettings();
    }

    private void enableWorkSyncSettings() {
        this.mWorkUsePersonalSounds.setChecked(true);
        Preference preference = this.mWorkPhoneRingtonePreference;
        if (preference != null) {
            preference.setSummary((int) R.string.work_sound_same_as_personal);
        }
        this.mWorkNotificationRingtonePreference.setSummary((int) R.string.work_sound_same_as_personal);
        this.mWorkAlarmRingtonePreference.setSummary((int) R.string.work_sound_same_as_personal);
    }

    private void disableWorkSync() {
        RingtoneManager.disableSyncFromParent(getManagedProfileContext());
        disableWorkSyncSettings();
    }

    private void disableWorkSyncSettings() {
        Preference preference = this.mWorkPhoneRingtonePreference;
        if (preference != null) {
            preference.setEnabled(true);
        }
        this.mWorkNotificationRingtonePreference.setEnabled(true);
        this.mWorkAlarmRingtonePreference.setEnabled(true);
        updateWorkRingtoneSummaries();
    }

    private void updateWorkRingtoneSummaries() {
        Context managedProfileContext = getManagedProfileContext();
        Preference preference = this.mWorkPhoneRingtonePreference;
        if (preference != null) {
            preference.setSummary(updateRingtoneName(managedProfileContext, 1));
        }
        this.mWorkNotificationRingtonePreference.setSummary(updateRingtoneName(managedProfileContext, 2));
        this.mWorkAlarmRingtonePreference.setSummary(updateRingtoneName(managedProfileContext, 4));
    }

    public void onManagedProfileAdded(int i) {
        if (this.mManagedProfileId == -10000) {
            this.mManagedProfileId = i;
            updateWorkPreferences();
        }
    }

    public void onManagedProfileRemoved(int i) {
        if (this.mManagedProfileId == i) {
            this.mManagedProfileId = this.mHelper.getManagedProfileId(this.mUserManager);
            updateWorkPreferences();
        }
    }

    public static class UnifyWorkDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
        public int getMetricsCategory() {
            return 553;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.work_sync_dialog_title).setMessage((int) R.string.work_sync_dialog_message).setPositiveButton((int) R.string.work_sync_dialog_yes, (DialogInterface.OnClickListener) this).setNegativeButton(17039369, (DialogInterface.OnClickListener) null).create();
        }

        public static void show(SoundSettings soundSettings) {
            FragmentManager fragmentManager = soundSettings.getFragmentManager();
            if (fragmentManager.findFragmentByTag("UnifyWorkDialogFragment") == null) {
                UnifyWorkDialogFragment unifyWorkDialogFragment = new UnifyWorkDialogFragment();
                unifyWorkDialogFragment.setTargetFragment(soundSettings, 200);
                unifyWorkDialogFragment.show(fragmentManager, "UnifyWorkDialogFragment");
            }
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            SoundSettings soundSettings = (SoundSettings) getTargetFragment();
            if (soundSettings.isAdded()) {
                soundSettings.enableWorkSync();
            }
        }
    }
}
