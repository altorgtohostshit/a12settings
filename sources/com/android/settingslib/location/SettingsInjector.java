package com.android.settingslib.location;

import android.R;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settingslib.R$string;
import com.android.settingslib.location.InjectedSetting;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public class SettingsInjector {
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    protected final Set<Setting> mSettings;

    /* access modifiers changed from: protected */
    public Preference createPreference(Context context, InjectedSetting injectedSetting) {
        throw null;
    }

    /* access modifiers changed from: protected */
    public void logPreferenceClick(Intent intent) {
        throw null;
    }

    public SettingsInjector(Context context) {
        this.mContext = context;
        HashSet hashSet = new HashSet();
        this.mSettings = hashSet;
        this.mHandler = new StatusLoadingHandler(hashSet);
    }

    /* access modifiers changed from: protected */
    public List<InjectedSetting> getSettings(UserHandle userHandle) {
        PackageManager packageManager = this.mContext.getPackageManager();
        Intent intent = new Intent("android.location.SettingInjectorService");
        int identifier = userHandle.getIdentifier();
        List<ResolveInfo> queryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(intent, 128, identifier);
        if (Log.isLoggable("SettingsInjector", 3)) {
            Log.d("SettingsInjector", "Found services for profile id " + identifier + ": " + queryIntentServicesAsUser);
        }
        PackageManager packageManager2 = this.mContext.createContextAsUser(userHandle, 0).getPackageManager();
        ArrayList arrayList = new ArrayList(queryIntentServicesAsUser.size());
        for (ResolveInfo resolveInfo : queryIntentServicesAsUser) {
            try {
                InjectedSetting parseServiceInfo = parseServiceInfo(resolveInfo, userHandle, packageManager2);
                if (parseServiceInfo == null) {
                    Log.w("SettingsInjector", "Unable to load service info " + resolveInfo);
                } else {
                    arrayList.add(parseServiceInfo);
                }
            } catch (XmlPullParserException e) {
                Log.w("SettingsInjector", "Unable to load service info " + resolveInfo, e);
            } catch (IOException e2) {
                Log.w("SettingsInjector", "Unable to load service info " + resolveInfo, e2);
            }
        }
        if (Log.isLoggable("SettingsInjector", 3)) {
            Log.d("SettingsInjector", "Loaded settings for profile id " + identifier + ": " + arrayList);
        }
        return arrayList;
    }

    private void populatePreference(Preference preference, InjectedSetting injectedSetting) {
        preference.setTitle((CharSequence) injectedSetting.title);
        preference.setSummary(R$string.loading_injected_setting_summary);
        preference.setOnPreferenceClickListener(new ServiceSettingClickedListener(injectedSetting));
    }

    public Map<Integer, List<Preference>> getInjectedSettings(Context context, int i) {
        List<UserHandle> userProfiles = ((UserManager) this.mContext.getSystemService("user")).getUserProfiles();
        ArrayMap arrayMap = new ArrayMap();
        this.mSettings.clear();
        for (UserHandle next : userProfiles) {
            if (i == -2 || i == next.getIdentifier()) {
                ArrayList arrayList = new ArrayList();
                for (InjectedSetting injectedSetting : getSettings(next)) {
                    Preference createPreference = createPreference(context, injectedSetting);
                    populatePreference(createPreference, injectedSetting);
                    arrayList.add(createPreference);
                    this.mSettings.add(new Setting(injectedSetting, createPreference));
                }
                if (!arrayList.isEmpty()) {
                    arrayMap.put(Integer.valueOf(next.getIdentifier()), arrayList);
                }
            }
        }
        reloadStatusMessages();
        return arrayMap;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:27|28|29) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x009e, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("Unable to load resources for package " + r0.packageName);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x009f, code lost:
        if (r3 != null) goto L_0x00a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a1, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a4, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0086 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.android.settingslib.location.InjectedSetting parseServiceInfo(android.content.pm.ResolveInfo r5, android.os.UserHandle r6, android.content.pm.PackageManager r7) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            android.content.pm.ServiceInfo r0 = r5.serviceInfo
            android.content.pm.ApplicationInfo r1 = r0.applicationInfo
            int r1 = r1.flags
            r2 = 1
            r1 = r1 & r2
            r3 = 0
            if (r1 != 0) goto L_0x0029
            r1 = 5
            java.lang.String r4 = "SettingsInjector"
            boolean r1 = android.util.Log.isLoggable(r4, r1)
            if (r1 == 0) goto L_0x0029
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Ignoring attempt to inject setting from app not in system image: "
            r6.append(r7)
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            android.util.Log.w(r4, r5)
            return r3
        L_0x0029:
            java.lang.String r1 = "android.location.SettingInjectorService"
            android.content.res.XmlResourceParser r3 = r0.loadXmlMetaData(r7, r1)     // Catch:{ NameNotFoundException -> 0x0086 }
            if (r3 == 0) goto L_0x0065
            android.util.AttributeSet r5 = android.util.Xml.asAttributeSet(r3)     // Catch:{ NameNotFoundException -> 0x0086 }
        L_0x0035:
            int r1 = r3.next()     // Catch:{ NameNotFoundException -> 0x0086 }
            if (r1 == r2) goto L_0x003f
            r4 = 2
            if (r1 == r4) goto L_0x003f
            goto L_0x0035
        L_0x003f:
            java.lang.String r1 = r3.getName()     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r2 = "injected-location-setting"
            boolean r1 = r2.equals(r1)     // Catch:{ NameNotFoundException -> 0x0086 }
            if (r1 == 0) goto L_0x005d
            java.lang.String r1 = r0.packageName     // Catch:{ NameNotFoundException -> 0x0086 }
            android.content.res.Resources r7 = r7.getResourcesForApplication(r1)     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r1 = r0.packageName     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r2 = r0.name     // Catch:{ NameNotFoundException -> 0x0086 }
            com.android.settingslib.location.InjectedSetting r5 = parseAttributes(r1, r2, r6, r7, r5)     // Catch:{ NameNotFoundException -> 0x0086 }
            r3.close()
            return r5
        L_0x005d:
            org.xmlpull.v1.XmlPullParserException r5 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r6 = "Meta-data does not start with injected-location-setting tag"
            r5.<init>(r6)     // Catch:{ NameNotFoundException -> 0x0086 }
            throw r5     // Catch:{ NameNotFoundException -> 0x0086 }
        L_0x0065:
            org.xmlpull.v1.XmlPullParserException r6 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ NameNotFoundException -> 0x0086 }
            r7.<init>()     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r1 = "No android.location.SettingInjectorService meta-data for "
            r7.append(r1)     // Catch:{ NameNotFoundException -> 0x0086 }
            r7.append(r5)     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r5 = ": "
            r7.append(r5)     // Catch:{ NameNotFoundException -> 0x0086 }
            r7.append(r0)     // Catch:{ NameNotFoundException -> 0x0086 }
            java.lang.String r5 = r7.toString()     // Catch:{ NameNotFoundException -> 0x0086 }
            r6.<init>(r5)     // Catch:{ NameNotFoundException -> 0x0086 }
            throw r6     // Catch:{ NameNotFoundException -> 0x0086 }
        L_0x0084:
            r5 = move-exception
            goto L_0x009f
        L_0x0086:
            org.xmlpull.v1.XmlPullParserException r5 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ all -> 0x0084 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0084 }
            r6.<init>()     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = "Unable to load resources for package "
            r6.append(r7)     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = r0.packageName     // Catch:{ all -> 0x0084 }
            r6.append(r7)     // Catch:{ all -> 0x0084 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0084 }
            r5.<init>(r6)     // Catch:{ all -> 0x0084 }
            throw r5     // Catch:{ all -> 0x0084 }
        L_0x009f:
            if (r3 == 0) goto L_0x00a4
            r3.close()
        L_0x00a4:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.location.SettingsInjector.parseServiceInfo(android.content.pm.ResolveInfo, android.os.UserHandle, android.content.pm.PackageManager):com.android.settingslib.location.InjectedSetting");
    }

    private static InjectedSetting parseAttributes(String str, String str2, UserHandle userHandle, Resources resources, AttributeSet attributeSet) {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, R.styleable.SettingInjectorService);
        try {
            String string = obtainAttributes.getString(1);
            int resourceId = obtainAttributes.getResourceId(0, 0);
            String string2 = obtainAttributes.getString(2);
            String string3 = obtainAttributes.getString(3);
            if (Log.isLoggable("SettingsInjector", 3)) {
                Log.d("SettingsInjector", "parsed title: " + string + ", iconId: " + resourceId + ", settingsActivity: " + string2);
            }
            return new InjectedSetting.Builder().setPackageName(str).setClassName(str2).setTitle(string).setIconId(resourceId).setUserHandle(userHandle).setSettingsActivity(string2).setUserRestriction(string3).build();
        } finally {
            obtainAttributes.recycle();
        }
    }

    public void reloadStatusMessages() {
        if (Log.isLoggable("SettingsInjector", 3)) {
            Log.d("SettingsInjector", "reloadingStatusMessages: " + this.mSettings);
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(1));
    }

    protected class ServiceSettingClickedListener implements Preference.OnPreferenceClickListener {
        private InjectedSetting mInfo;

        public ServiceSettingClickedListener(InjectedSetting injectedSetting) {
            this.mInfo = injectedSetting;
        }

        public boolean onPreferenceClick(Preference preference) {
            Intent intent = new Intent();
            InjectedSetting injectedSetting = this.mInfo;
            intent.setClassName(injectedSetting.packageName, injectedSetting.settingsActivity);
            SettingsInjector.this.logPreferenceClick(intent);
            intent.setFlags(268468224);
            SettingsInjector.this.mContext.startActivityAsUser(intent, this.mInfo.mUserHandle);
            return true;
        }
    }

    private static final class StatusLoadingHandler extends Handler {
        WeakReference<Set<Setting>> mAllSettings;
        private Set<Setting> mSettingsBeingLoaded = new ArraySet();
        private Deque<Setting> mSettingsToLoad = new ArrayDeque();

        public StatusLoadingHandler(Set<Setting> set) {
            super(Looper.getMainLooper());
            this.mAllSettings = new WeakReference<>(set);
        }

        public void handleMessage(Message message) {
            if (Log.isLoggable("SettingsInjector", 3)) {
                Log.d("SettingsInjector", "handleMessage start: " + message + ", " + this);
            }
            int i = message.what;
            if (i == 1) {
                Set set = (Set) this.mAllSettings.get();
                if (set != null) {
                    this.mSettingsToLoad.clear();
                    this.mSettingsToLoad.addAll(set);
                }
            } else if (i == 2) {
                Setting setting = (Setting) message.obj;
                setting.maybeLogElapsedTime();
                this.mSettingsBeingLoaded.remove(setting);
                removeMessages(3, setting);
            } else if (i != 3) {
                Log.wtf("SettingsInjector", "Unexpected what: " + message);
            } else {
                Setting setting2 = (Setting) message.obj;
                this.mSettingsBeingLoaded.remove(setting2);
                if (Log.isLoggable("SettingsInjector", 5)) {
                    Log.w("SettingsInjector", "Timed out after " + setting2.getElapsedTime() + " millis trying to get status for: " + setting2);
                }
            }
            if (this.mSettingsBeingLoaded.size() > 0) {
                if (Log.isLoggable("SettingsInjector", 2)) {
                    Log.v("SettingsInjector", "too many services already live for " + message + ", " + this);
                }
            } else if (!this.mSettingsToLoad.isEmpty()) {
                Setting removeFirst = this.mSettingsToLoad.removeFirst();
                removeFirst.startService();
                this.mSettingsBeingLoaded.add(removeFirst);
                sendMessageDelayed(obtainMessage(3, removeFirst), 1000);
                if (Log.isLoggable("SettingsInjector", 3)) {
                    Log.d("SettingsInjector", "handleMessage end " + message + ", " + this + ", started loading " + removeFirst);
                }
            } else if (Log.isLoggable("SettingsInjector", 2)) {
                Log.v("SettingsInjector", "nothing left to do for " + message + ", " + this);
            }
        }

        public String toString() {
            return "StatusLoadingHandler{mSettingsToLoad=" + this.mSettingsToLoad + ", mSettingsBeingLoaded=" + this.mSettingsBeingLoaded + '}';
        }
    }

    private static class MessengerHandler extends Handler {
        private Handler mHandler;
        private WeakReference<Setting> mSettingRef;

        public MessengerHandler(Setting setting, Handler handler) {
            this.mSettingRef = new WeakReference<>(setting);
            this.mHandler = handler;
        }

        public void handleMessage(Message message) {
            Setting setting = (Setting) this.mSettingRef.get();
            if (setting != null) {
                Preference preference = setting.preference;
                Bundle data = message.getData();
                boolean z = data.getBoolean("enabled", true);
                String string = data.getString("summary", (String) null);
                if (Log.isLoggable("SettingsInjector", 3)) {
                    Log.d("SettingsInjector", setting + ": received " + message + ", bundle: " + data);
                }
                preference.setSummary((CharSequence) string);
                preference.setEnabled(z);
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(2, setting));
            }
        }
    }

    protected final class Setting {
        public final Preference preference;
        public final InjectedSetting setting;
        public long startMillis;

        public Setting(InjectedSetting injectedSetting, Preference preference2) {
            this.setting = injectedSetting;
            this.preference = preference2;
        }

        public String toString() {
            return "Setting{setting=" + this.setting + ", preference=" + this.preference + '}';
        }

        public void startService() {
            if (((ActivityManager) SettingsInjector.this.mContext.getSystemService("activity")).isUserRunning(this.setting.mUserHandle.getIdentifier())) {
                MessengerHandler messengerHandler = new MessengerHandler(this, SettingsInjector.this.mHandler);
                Messenger messenger = new Messenger(messengerHandler);
                Intent serviceIntent = this.setting.getServiceIntent();
                serviceIntent.putExtra("messenger", messenger);
                if (Log.isLoggable("SettingsInjector", 3)) {
                    Log.d("SettingsInjector", this.setting + ": sending update intent: " + serviceIntent + ", handler: " + messengerHandler);
                    this.startMillis = SystemClock.elapsedRealtime();
                } else {
                    this.startMillis = 0;
                }
                SettingsInjector.this.mContext.startServiceAsUser(serviceIntent, this.setting.mUserHandle);
            } else if (Log.isLoggable("SettingsInjector", 2)) {
                Log.v("SettingsInjector", "Cannot start service as user " + this.setting.mUserHandle.getIdentifier() + " is not running");
            }
        }

        public long getElapsedTime() {
            return SystemClock.elapsedRealtime() - this.startMillis;
        }

        public void maybeLogElapsedTime() {
            if (Log.isLoggable("SettingsInjector", 3) && this.startMillis != 0) {
                long elapsedTime = getElapsedTime();
                Log.d("SettingsInjector", this + " update took " + elapsedTime + " millis");
            }
        }
    }
}
