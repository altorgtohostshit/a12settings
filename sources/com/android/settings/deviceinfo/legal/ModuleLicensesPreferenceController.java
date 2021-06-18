package com.android.settings.deviceinfo.legal;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageManager;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.util.ArrayUtils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.io.IOException;
import java.util.Comparator;

public class ModuleLicensesPreferenceController extends BasePreferenceController {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
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

    public ModuleLicensesPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mContext.getPackageManager().getInstalledModules(0).stream().sorted(Comparator.comparing(ModuleLicensesPreferenceController$$ExternalSyntheticLambda1.INSTANCE)).filter(new Predicate(this.mContext)).forEach(new ModuleLicensesPreferenceController$$ExternalSyntheticLambda0((PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey())));
    }

    static class Predicate implements java.util.function.Predicate<ModuleInfo> {
        private final Context mContext;

        public Predicate(Context context) {
            this.mContext = context;
        }

        public boolean test(ModuleInfo moduleInfo) {
            try {
                return ArrayUtils.contains(ModuleLicenseProvider.getPackageAssetManager(this.mContext.getPackageManager(), moduleInfo.getPackageName()).list(""), "NOTICE.html.gz");
            } catch (PackageManager.NameNotFoundException | IOException unused) {
                return false;
            }
        }
    }
}
