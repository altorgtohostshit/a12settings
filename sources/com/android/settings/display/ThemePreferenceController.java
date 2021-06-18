package com.android.settings.display;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThemePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final IOverlayManager mOverlayService;
    private final PackageManager mPackageManager;

    public String getPreferenceKey() {
        return "theme";
    }

    public ThemePreferenceController(Context context) {
        this(context, IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay")));
    }

    ThemePreferenceController(Context context, IOverlayManager iOverlayManager) {
        super(context);
        this.mOverlayService = iOverlayManager;
        this.mPackageManager = context.getPackageManager();
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if ("theme".equals(preference.getKey())) {
            this.mMetricsFeatureProvider.action(this.mContext, 816, (Pair<Integer, Object>[]) new Pair[0]);
        }
        return false;
    }

    public void updateState(Preference preference) {
        ListPreference listPreference = (ListPreference) preference;
        int i = 0;
        String[] availableThemes = getAvailableThemes(false);
        CharSequence[] charSequenceArr = new CharSequence[availableThemes.length];
        for (int i2 = 0; i2 < availableThemes.length; i2++) {
            try {
                charSequenceArr[i2] = this.mPackageManager.getApplicationInfo(availableThemes[i2], 0).loadLabel(this.mPackageManager);
            } catch (PackageManager.NameNotFoundException unused) {
                charSequenceArr[i2] = availableThemes[i2];
            }
        }
        listPreference.setEntries(charSequenceArr);
        listPreference.setEntryValues(availableThemes);
        String currentTheme = getCurrentTheme();
        CharSequence charSequence = null;
        while (true) {
            if (i >= availableThemes.length) {
                break;
            } else if (TextUtils.equals(availableThemes[i], currentTheme)) {
                charSequence = charSequenceArr[i];
                break;
            } else {
                i++;
            }
        }
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = this.mContext.getString(R.string.default_theme);
        }
        listPreference.setSummary(charSequence);
        listPreference.setValue(currentTheme);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (Objects.equals(obj, getCurrentTheme())) {
            return true;
        }
        try {
            this.mOverlayService.setEnabledExclusiveInCategory((String) obj, UserHandle.myUserId());
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private boolean isTheme(OverlayInfo overlayInfo) {
        if (!"android.theme".equals(overlayInfo.category)) {
            return false;
        }
        try {
            PackageInfo packageInfo = this.mPackageManager.getPackageInfo(overlayInfo.packageName, 0);
            if (packageInfo == null || packageInfo.isStaticOverlayPackage()) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public boolean isAvailable() {
        String[] availableThemes;
        if (this.mOverlayService == null || (availableThemes = getAvailableThemes(false)) == null || availableThemes.length <= 1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public String getCurrentTheme() {
        String[] availableThemes = getAvailableThemes(true);
        if (availableThemes.length < 1) {
            return null;
        }
        return availableThemes[0];
    }

    /* access modifiers changed from: package-private */
    public String[] getAvailableThemes(boolean z) {
        try {
            List overlayInfosForTarget = this.mOverlayService.getOverlayInfosForTarget("android", UserHandle.myUserId());
            ArrayList arrayList = new ArrayList(overlayInfosForTarget.size());
            int size = overlayInfosForTarget.size();
            for (int i = 0; i < size; i++) {
                if (isTheme((OverlayInfo) overlayInfosForTarget.get(i))) {
                    if (!((OverlayInfo) overlayInfosForTarget.get(i)).isEnabled() || !z) {
                        arrayList.add(((OverlayInfo) overlayInfosForTarget.get(i)).packageName);
                    } else {
                        return new String[]{((OverlayInfo) overlayInfosForTarget.get(i)).packageName};
                    }
                }
            }
            if (z) {
                return new String[0];
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
