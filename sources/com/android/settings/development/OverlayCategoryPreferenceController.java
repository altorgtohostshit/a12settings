package com.android.settings.development;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OverlayCategoryPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    private static final Comparator<OverlayInfo> OVERLAY_INFO_COMPARATOR = Comparator.comparingInt(OverlayCategoryPreferenceController$$ExternalSyntheticLambda2.INSTANCE);
    static final String PACKAGE_DEVICE_DEFAULT = "package_device_default";
    private final boolean mAvailable;
    private final String mCategory;
    /* access modifiers changed from: private */
    public final IOverlayManager mOverlayManager;
    private final PackageManager mPackageManager;
    /* access modifiers changed from: private */
    public ListPreference mPreference;

    OverlayCategoryPreferenceController(Context context, PackageManager packageManager, IOverlayManager iOverlayManager, String str) {
        super(context);
        this.mOverlayManager = iOverlayManager;
        this.mPackageManager = packageManager;
        this.mCategory = str;
        this.mAvailable = iOverlayManager != null && !getOverlayInfos().isEmpty();
    }

    public boolean isAvailable() {
        return this.mAvailable;
    }

    public String getPreferenceKey() {
        return this.mCategory;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        setPreference((ListPreference) preferenceScreen.findPreference(getPreferenceKey()));
    }

    /* access modifiers changed from: package-private */
    public void setPreference(ListPreference listPreference) {
        this.mPreference = listPreference;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return setOverlay((String) obj);
    }

    private boolean setOverlay(final String str) {
        final String str2 = (String) getOverlayInfos().stream().filter(OverlayCategoryPreferenceController$$ExternalSyntheticLambda1.INSTANCE).map(OverlayCategoryPreferenceController$$ExternalSyntheticLambda0.INSTANCE).findFirst().orElse((Object) null);
        if ((PACKAGE_DEVICE_DEFAULT.equals(str) && TextUtils.isEmpty(str2)) || TextUtils.equals(str, str2)) {
            return true;
        }
        new AsyncTask<Void, Void, Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean doInBackground(Void... voidArr) {
                try {
                    if (OverlayCategoryPreferenceController.PACKAGE_DEVICE_DEFAULT.equals(str)) {
                        return Boolean.valueOf(OverlayCategoryPreferenceController.this.mOverlayManager.setEnabled(str2, false, 0));
                    }
                    return Boolean.valueOf(OverlayCategoryPreferenceController.this.mOverlayManager.setEnabledExclusiveInCategory(str, 0));
                } catch (RemoteException | IllegalStateException | SecurityException e) {
                    Log.w("OverlayCategoryPC", "Error enabling overlay.", e);
                    return Boolean.FALSE;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Boolean bool) {
                OverlayCategoryPreferenceController overlayCategoryPreferenceController = OverlayCategoryPreferenceController.this;
                overlayCategoryPreferenceController.updateState(overlayCategoryPreferenceController.mPreference);
                if (!bool.booleanValue()) {
                    Toast.makeText(OverlayCategoryPreferenceController.this.mContext, R.string.overlay_toast_failed_to_apply, 1).show();
                }
            }
        }.execute(new Void[0]);
        return true;
    }

    public void updateState(Preference preference) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String string = this.mContext.getString(R.string.overlay_option_device_default);
        String str = PACKAGE_DEVICE_DEFAULT;
        arrayList.add(str);
        arrayList2.add(string);
        for (OverlayInfo next : getOverlayInfos()) {
            arrayList.add(next.packageName);
            try {
                arrayList2.add(this.mPackageManager.getApplicationInfo(next.packageName, 0).loadLabel(this.mPackageManager).toString());
            } catch (PackageManager.NameNotFoundException unused) {
                arrayList2.add(next.packageName);
            }
            if (next.isEnabled()) {
                String str2 = (String) arrayList2.get(arrayList2.size() - 1);
                str = (String) arrayList.get(arrayList.size() - 1);
                string = str2;
            }
        }
        this.mPreference.setEntries((CharSequence[]) arrayList2.toArray(new String[arrayList2.size()]));
        this.mPreference.setEntryValues((CharSequence[]) arrayList.toArray(new String[arrayList.size()]));
        this.mPreference.setValue(str);
        this.mPreference.setSummary(string);
    }

    private List<OverlayInfo> getOverlayInfos() {
        ArrayList arrayList = new ArrayList();
        try {
            for (OverlayInfo overlayInfo : this.mOverlayManager.getOverlayInfosForTarget("android", 0)) {
                if (this.mCategory.equals(overlayInfo.category)) {
                    arrayList.add(overlayInfo);
                }
            }
            arrayList.sort(OVERLAY_INFO_COMPARATOR);
            return arrayList;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        setOverlay(PACKAGE_DEVICE_DEFAULT);
        updateState(this.mPreference);
    }
}
