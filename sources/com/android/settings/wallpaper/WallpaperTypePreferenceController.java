package com.android.settings.wallpaper;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import java.util.List;
import java.util.stream.Collectors;

public class WallpaperTypePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart {
    private PreferenceScreen mScreen;

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

    public WallpaperTypePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (preference.getIntent() == null) {
            return super.handlePreferenceTreeClick(preference);
        }
        this.mContext.startActivity(preference.getIntent());
        return true;
    }

    public void onStart() {
        populateWallpaperTypes();
    }

    /* JADX WARNING: type inference failed for: r6v2, types: [java.lang.CharSequence] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void populateWallpaperTypes() {
        /*
            r9 = this;
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r1 = "android.intent.action.SET_WALLPAPER"
            r0.<init>(r1)
            android.content.Context r1 = r9.mContext
            android.content.pm.PackageManager r1 = r1.getPackageManager()
            r2 = 65536(0x10000, float:9.18355E-41)
            java.util.List r2 = r1.queryIntentActivities(r0, r2)
            r9.removeUselessExistingPreference(r2)
            androidx.preference.PreferenceScreen r3 = r9.mScreen
            r4 = 0
            r3.setOrderingAsAdded(r4)
            java.util.Iterator r2 = r2.iterator()
        L_0x0020:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0078
            java.lang.Object r3 = r2.next()
            android.content.pm.ResolveInfo r3 = (android.content.pm.ResolveInfo) r3
            android.content.pm.ActivityInfo r4 = r3.activityInfo
            java.lang.String r4 = r4.packageName
            androidx.preference.PreferenceScreen r5 = r9.mScreen
            androidx.preference.Preference r5 = r5.findPreference(r4)
            if (r5 != 0) goto L_0x0043
            androidx.preference.Preference r5 = new androidx.preference.Preference
            androidx.preference.PreferenceScreen r6 = r9.mScreen
            android.content.Context r6 = r6.getContext()
            r5.<init>(r6)
        L_0x0043:
            android.content.Intent r6 = new android.content.Intent
            r6.<init>(r0)
            r7 = 33554432(0x2000000, float:9.403955E-38)
            android.content.Intent r6 = r6.addFlags(r7)
            android.content.ComponentName r7 = new android.content.ComponentName
            android.content.pm.ActivityInfo r8 = r3.activityInfo
            java.lang.String r8 = r8.name
            r7.<init>(r4, r8)
            r6.setComponent(r7)
            r5.setIntent(r6)
            r5.setKey(r4)
            java.lang.CharSequence r6 = r3.loadLabel(r1)
            if (r6 != 0) goto L_0x0067
            goto L_0x0068
        L_0x0067:
            r4 = r6
        L_0x0068:
            r5.setTitle((java.lang.CharSequence) r4)
            android.graphics.drawable.Drawable r3 = r3.loadIcon(r1)
            r5.setIcon((android.graphics.drawable.Drawable) r3)
            androidx.preference.PreferenceScreen r3 = r9.mScreen
            r3.addPreference(r5)
            goto L_0x0020
        L_0x0078:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wallpaper.WallpaperTypePreferenceController.populateWallpaperTypes():void");
    }

    private void removeUselessExistingPreference(List<ResolveInfo> list) {
        int preferenceCount = this.mScreen.getPreferenceCount();
        if (preferenceCount > 0) {
            for (int i = preferenceCount - 1; i >= 0; i--) {
                Preference preference = this.mScreen.getPreference(i);
                if (((List) list.stream().filter(new WallpaperTypePreferenceController$$ExternalSyntheticLambda0(preference)).collect(Collectors.toList())).isEmpty()) {
                    this.mScreen.removePreference(preference);
                }
            }
        }
    }
}
