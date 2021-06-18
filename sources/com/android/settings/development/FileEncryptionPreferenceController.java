package com.android.settings.development;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IStorageManager;
import android.sysprop.CryptoProperties;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class FileEncryptionPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    private final IStorageManager mStorageManager = getStorageManager();

    public String getPreferenceKey() {
        return "convert_to_file_encryption";
    }

    public FileEncryptionPreferenceController(Context context) {
        super(context);
    }

    public boolean isAvailable() {
        IStorageManager iStorageManager = this.mStorageManager;
        if (iStorageManager == null) {
            return false;
        }
        try {
            return iStorageManager.isConvertibleToFBE();
        } catch (RemoteException unused) {
            return false;
        }
    }

    public void updateState(Preference preference) {
        if (CryptoProperties.type().orElse(CryptoProperties.type_values.NONE) == CryptoProperties.type_values.FILE) {
            this.mPreference.setEnabled(false);
            this.mPreference.setSummary((CharSequence) this.mContext.getResources().getString(R.string.convert_to_file_encryption_done));
        }
    }

    private IStorageManager getStorageManager() {
        try {
            return IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        } catch (VerifyError unused) {
            return null;
        }
    }
}
