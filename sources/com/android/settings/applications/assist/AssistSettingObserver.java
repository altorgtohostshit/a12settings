package com.android.settings.applications.assist;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import com.android.settingslib.utils.ThreadUtils;
import java.util.List;

public abstract class AssistSettingObserver extends ContentObserver {
    private final Uri ASSIST_URI = Settings.Secure.getUriFor("assistant");

    /* access modifiers changed from: protected */
    public abstract List<Uri> getSettingUris();

    /* renamed from: onSettingChange */
    public abstract void lambda$onChange$0();

    public AssistSettingObserver() {
        super((Handler) null);
    }

    public void register(ContentResolver contentResolver, boolean z) {
        if (z) {
            contentResolver.registerContentObserver(this.ASSIST_URI, false, this);
            List<Uri> settingUris = getSettingUris();
            if (settingUris != null) {
                for (Uri registerContentObserver : settingUris) {
                    contentResolver.registerContentObserver(registerContentObserver, false, this);
                }
                return;
            }
            return;
        }
        contentResolver.unregisterContentObserver(this);
    }

    public void onChange(boolean z, Uri uri) {
        super.onChange(z, uri);
        List<Uri> settingUris = getSettingUris();
        if (this.ASSIST_URI.equals(uri) || (settingUris != null && settingUris.contains(uri))) {
            ThreadUtils.postOnMainThread(new AssistSettingObserver$$ExternalSyntheticLambda0(this));
        }
    }
}
