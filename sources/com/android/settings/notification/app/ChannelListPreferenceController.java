package com.android.settings.notification.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChannelListPreferenceController extends NotificationPreferenceController {
    /* access modifiers changed from: private */
    public List<NotificationChannelGroup> mChannelGroupList;
    /* access modifiers changed from: private */
    public PreferenceCategory mPreference;

    public String getPreferenceKey() {
        return "channels";
    }

    /* access modifiers changed from: package-private */
    public boolean isIncludedInFilter() {
        return false;
    }

    public ChannelListPreferenceController(Context context, NotificationBackend notificationBackend) {
        super(context, notificationBackend);
    }

    public boolean isAvailable() {
        NotificationBackend.AppRow appRow = this.mAppRow;
        if (appRow == null || appRow.banned) {
            return false;
        }
        if (this.mChannel == null) {
            return true;
        }
        if (this.mBackend.onlyHasDefaultChannel(appRow.pkg, appRow.uid) || "miscellaneous".equals(this.mChannel.getId())) {
            return false;
        }
        return true;
    }

    public void updateState(Preference preference) {
        this.mPreference = (PreferenceCategory) preference;
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                ChannelListPreferenceController channelListPreferenceController = ChannelListPreferenceController.this;
                NotificationBackend notificationBackend = channelListPreferenceController.mBackend;
                NotificationBackend.AppRow appRow = channelListPreferenceController.mAppRow;
                List unused = channelListPreferenceController.mChannelGroupList = notificationBackend.getGroups(appRow.pkg, appRow.uid).getList();
                Collections.sort(ChannelListPreferenceController.this.mChannelGroupList, NotificationPreferenceController.CHANNEL_GROUP_COMPARATOR);
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                ChannelListPreferenceController channelListPreferenceController = ChannelListPreferenceController.this;
                if (channelListPreferenceController.mContext != null) {
                    channelListPreferenceController.updateFullList(channelListPreferenceController.mPreference, ChannelListPreferenceController.this.mChannelGroupList);
                }
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: package-private */
    public void updateFullList(PreferenceCategory preferenceCategory, List<NotificationChannelGroup> list) {
        if (!list.isEmpty()) {
            updateGroupList(preferenceCategory, list);
        } else if (preferenceCategory.getPreferenceCount() != 1 || !"zeroCategories".equals(preferenceCategory.getPreference(0).getKey())) {
            preferenceCategory.removeAll();
            PreferenceCategory preferenceCategory2 = new PreferenceCategory(this.mContext);
            preferenceCategory2.setTitle((int) R.string.notification_channels);
            preferenceCategory2.setKey("zeroCategories");
            preferenceCategory.addPreference(preferenceCategory2);
            Preference preference = new Preference(this.mContext);
            preference.setTitle((int) R.string.no_channels);
            preference.setEnabled(false);
            preferenceCategory2.addPreference(preference);
        } else {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceCategory.getPreference(0);
            preferenceGroup.setTitle((int) R.string.notification_channels);
            preferenceGroup.getPreference(0).setTitle((int) R.string.no_channels);
        }
    }

    private PreferenceCategory findOrCreateGroupCategoryForKey(PreferenceCategory preferenceCategory, String str, int i) {
        if (str == null) {
            str = "categories";
        }
        int preferenceCount = preferenceCategory.getPreferenceCount();
        if (i < preferenceCount) {
            Preference preference = preferenceCategory.getPreference(i);
            if (str.equals(preference.getKey())) {
                return (PreferenceCategory) preference;
            }
        }
        for (int i2 = 0; i2 < preferenceCount; i2++) {
            Preference preference2 = preferenceCategory.getPreference(i2);
            if (str.equals(preference2.getKey())) {
                preference2.setOrder(i);
                return (PreferenceCategory) preference2;
            }
        }
        PreferenceCategory preferenceCategory2 = new PreferenceCategory(this.mContext);
        preferenceCategory2.setOrder(i);
        preferenceCategory2.setKey(str);
        preferenceCategory.addPreference(preferenceCategory2);
        return preferenceCategory2;
    }

    private void updateGroupList(PreferenceCategory preferenceCategory, List<NotificationChannelGroup> list) {
        int size = list.size();
        int preferenceCount = preferenceCategory.getPreferenceCount();
        ArrayList<PreferenceCategory> arrayList = new ArrayList<>(size);
        boolean z = false;
        for (int i = 0; i < size; i++) {
            NotificationChannelGroup notificationChannelGroup = list.get(i);
            PreferenceCategory findOrCreateGroupCategoryForKey = findOrCreateGroupCategoryForKey(preferenceCategory, notificationChannelGroup.getId(), i);
            arrayList.add(findOrCreateGroupCategoryForKey);
            updateGroupPreferences(notificationChannelGroup, findOrCreateGroupCategoryForKey);
        }
        int preferenceCount2 = preferenceCategory.getPreferenceCount();
        boolean z2 = (preferenceCount == 0 || preferenceCount == size) ? false : true;
        if (preferenceCount2 != size) {
            z = true;
        }
        if (z2 || z) {
            preferenceCategory.removeAll();
            for (PreferenceCategory addPreference : arrayList) {
                preferenceCategory.addPreference(addPreference);
            }
        }
    }

    private PrimarySwitchPreference findOrCreateChannelPrefForKey(PreferenceGroup preferenceGroup, String str, int i) {
        int preferenceCount = preferenceGroup.getPreferenceCount();
        if (i < preferenceCount) {
            Preference preference = preferenceGroup.getPreference(i);
            if (str.equals(preference.getKey())) {
                return (PrimarySwitchPreference) preference;
            }
        }
        for (int i2 = 0; i2 < preferenceCount; i2++) {
            Preference preference2 = preferenceGroup.getPreference(i2);
            if (str.equals(preference2.getKey())) {
                preference2.setOrder(i);
                return (PrimarySwitchPreference) preference2;
            }
        }
        PrimarySwitchPreference primarySwitchPreference = new PrimarySwitchPreference(this.mContext);
        primarySwitchPreference.setOrder(i);
        primarySwitchPreference.setKey(str);
        preferenceGroup.addPreference(primarySwitchPreference);
        return primarySwitchPreference;
    }

    private void updateGroupPreferences(NotificationChannelGroup notificationChannelGroup, PreferenceGroup preferenceGroup) {
        int preferenceCount = preferenceGroup.getPreferenceCount();
        ArrayList<Preference> arrayList = new ArrayList<>();
        if (notificationChannelGroup.getId() == null) {
            preferenceGroup.setTitle((int) R.string.notification_channels_other);
        } else {
            preferenceGroup.setTitle(notificationChannelGroup.getName());
            arrayList.add(addOrUpdateGroupToggle(preferenceGroup, notificationChannelGroup));
        }
        boolean z = true;
        boolean z2 = preferenceGroup.getPreferenceCount() == arrayList.size();
        List<NotificationChannel> emptyList = notificationChannelGroup.isBlocked() ? Collections.emptyList() : notificationChannelGroup.getChannels();
        Collections.sort(emptyList, NotificationPreferenceController.CHANNEL_COMPARATOR);
        for (NotificationChannel next : emptyList) {
            if (TextUtils.isEmpty(next.getConversationId()) || next.isDemoted()) {
                PrimarySwitchPreference findOrCreateChannelPrefForKey = findOrCreateChannelPrefForKey(preferenceGroup, next.getId(), arrayList.size());
                updateSingleChannelPrefs(findOrCreateChannelPrefForKey, next, notificationChannelGroup.isBlocked());
                arrayList.add(findOrCreateChannelPrefForKey);
            }
        }
        int preferenceCount2 = preferenceGroup.getPreferenceCount();
        int size = arrayList.size();
        boolean z3 = !z2 && preferenceCount != size;
        if (preferenceCount2 == size) {
            z = false;
        }
        if (z3 || z) {
            preferenceGroup.removeAll();
            for (Preference addPreference : arrayList) {
                preferenceGroup.addPreference(addPreference);
            }
        }
    }

    private Preference addOrUpdateGroupToggle(PreferenceGroup preferenceGroup, NotificationChannelGroup notificationChannelGroup) {
        boolean z;
        RestrictedSwitchPreference restrictedSwitchPreference;
        boolean z2 = false;
        if (preferenceGroup.getPreferenceCount() <= 0 || !(preferenceGroup.getPreference(0) instanceof RestrictedSwitchPreference)) {
            restrictedSwitchPreference = new RestrictedSwitchPreference(this.mContext);
            z = true;
        } else {
            restrictedSwitchPreference = (RestrictedSwitchPreference) preferenceGroup.getPreference(0);
            z = false;
        }
        restrictedSwitchPreference.setOrder(-1);
        restrictedSwitchPreference.setTitle((CharSequence) this.mContext.getString(R.string.notification_switch_label, new Object[]{notificationChannelGroup.getName()}));
        if (this.mAdmin == null && isChannelGroupBlockable(notificationChannelGroup)) {
            z2 = true;
        }
        restrictedSwitchPreference.setEnabled(z2);
        restrictedSwitchPreference.setChecked(true ^ notificationChannelGroup.isBlocked());
        restrictedSwitchPreference.setOnPreferenceClickListener(new ChannelListPreferenceController$$ExternalSyntheticLambda1(this, notificationChannelGroup));
        if (z) {
            preferenceGroup.addPreference(restrictedSwitchPreference);
        }
        return restrictedSwitchPreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addOrUpdateGroupToggle$0(NotificationChannelGroup notificationChannelGroup, Preference preference) {
        notificationChannelGroup.setBlocked(!((SwitchPreference) preference).isChecked());
        NotificationBackend notificationBackend = this.mBackend;
        NotificationBackend.AppRow appRow = this.mAppRow;
        notificationBackend.updateChannelGroup(appRow.pkg, appRow.uid, notificationChannelGroup);
        onGroupBlockStateChanged(notificationChannelGroup);
        return true;
    }

    private void updateSingleChannelPrefs(PrimarySwitchPreference primarySwitchPreference, NotificationChannel notificationChannel, boolean z) {
        boolean z2 = false;
        primarySwitchPreference.setSwitchEnabled(this.mAdmin == null && isChannelBlockable(notificationChannel) && isChannelConfigurable(notificationChannel) && !z);
        if (notificationChannel.getImportance() > 2) {
            primarySwitchPreference.setIcon(getAlertingIcon());
        } else {
            primarySwitchPreference.setIcon((Drawable) null);
        }
        primarySwitchPreference.setIconSize(2);
        primarySwitchPreference.setTitle(notificationChannel.getName());
        primarySwitchPreference.setSummary(NotificationBackend.getSentSummary(this.mContext, this.mAppRow.sentByChannel.get(notificationChannel.getId()), false));
        if (notificationChannel.getImportance() != 0) {
            z2 = true;
        }
        primarySwitchPreference.setChecked(z2);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", this.mAppRow.uid);
        bundle.putString("package", this.mAppRow.pkg);
        bundle.putString("android.provider.extra.CHANNEL_ID", notificationChannel.getId());
        bundle.putBoolean("fromSettings", true);
        primarySwitchPreference.setIntent(new SubSettingLauncher(this.mContext).setDestination(ChannelNotificationSettings.class.getName()).setArguments(bundle).setTitleRes(R.string.notification_channel_title).setSourceMetricsCategory(72).toIntent());
        primarySwitchPreference.setOnPreferenceChangeListener(new ChannelListPreferenceController$$ExternalSyntheticLambda0(this, notificationChannel));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateSingleChannelPrefs$1(NotificationChannel notificationChannel, Preference preference, Object obj) {
        notificationChannel.setImportance(((Boolean) obj).booleanValue() ? Math.max(notificationChannel.getOriginalImportance(), 2) : 0);
        notificationChannel.lockFields(4);
        PrimarySwitchPreference primarySwitchPreference = (PrimarySwitchPreference) preference;
        primarySwitchPreference.setIcon((Drawable) null);
        if (notificationChannel.getImportance() > 2) {
            primarySwitchPreference.setIcon(getAlertingIcon());
        }
        NotificationBackend notificationBackend = this.mBackend;
        NotificationBackend.AppRow appRow = this.mAppRow;
        notificationBackend.updateChannel(appRow.pkg, appRow.uid, notificationChannel);
        return true;
    }

    private Drawable getAlertingIcon() {
        Drawable drawable = this.mContext.getDrawable(R.drawable.ic_notifications_alert);
        drawable.setTintList(Utils.getColorAccent(this.mContext));
        return drawable;
    }

    /* access modifiers changed from: protected */
    public void onGroupBlockStateChanged(NotificationChannelGroup notificationChannelGroup) {
        PreferenceGroup preferenceGroup;
        if (notificationChannelGroup != null && (preferenceGroup = (PreferenceGroup) this.mPreference.findPreference(notificationChannelGroup.getId())) != null) {
            updateGroupPreferences(notificationChannelGroup, preferenceGroup);
        }
    }
}
