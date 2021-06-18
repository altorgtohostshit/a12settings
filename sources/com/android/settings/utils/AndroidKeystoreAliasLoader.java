package com.android.settings.utils;

import android.security.keystore2.AndroidKeyStoreLoadStoreParameter;
import android.util.Log;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class AndroidKeystoreAliasLoader {
    private final Collection<String> mCaCertAliases = new ArrayList();
    private final Collection<String> mKeyCertAliases = new ArrayList();

    public AndroidKeystoreAliasLoader(Integer num) {
        try {
            KeyStore instance = KeyStore.getInstance("AndroidKeyStore");
            if (num == null || num.intValue() == -1) {
                instance.load((KeyStore.LoadStoreParameter) null);
            } else {
                instance.load(new AndroidKeyStoreLoadStoreParameter(num.intValue()));
            }
            Enumeration<String> aliases = instance.aliases();
            while (aliases.hasMoreElements()) {
                String nextElement = aliases.nextElement();
                try {
                    Key key = instance.getKey(nextElement, (char[]) null);
                    if (key != null) {
                        if (key instanceof PrivateKey) {
                            this.mKeyCertAliases.add(nextElement);
                            Certificate[] certificateChain = instance.getCertificateChain(nextElement);
                            if (certificateChain != null && certificateChain.length >= 2) {
                                this.mCaCertAliases.add(nextElement);
                            }
                        }
                    } else if (instance.getCertificate(nextElement) != null) {
                        this.mCaCertAliases.add(nextElement);
                    }
                } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                    Log.e("SettingsKeystoreUtils", "Failed to load alias: " + nextElement + " from Android Keystore. Ignoring.", e);
                }
            }
        } catch (Exception e2) {
            Log.e("SettingsKeystoreUtils", "Failed to open Android Keystore.", e2);
        }
    }

    public Collection<String> getKeyCertAliases() {
        return this.mKeyCertAliases;
    }

    public Collection<String> getCaCertAliases() {
        return this.mCaCertAliases;
    }
}
