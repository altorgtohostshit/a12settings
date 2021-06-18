package com.android.settings.development;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.settings.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DSULoader extends ListActivity {
    /* access modifiers changed from: private */
    public ArrayAdapter<Object> mAdapter;
    /* access modifiers changed from: private */
    public List<Object> mDSUList = new ArrayList();

    private static String readAll(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr, 0, 4096);
            if (read == -1) {
                return sb.toString();
            }
            sb.append(new String(Arrays.copyOf(bArr, read)));
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0085 A[SYNTHETIC, Splitter:B:31:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readAll(java.net.URL r6) throws java.io.IOException {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "fetch "
            r0.append(r1)
            java.lang.String r1 = r6.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "DSULOADER"
            android.util.Slog.i(r1, r0)
            r0 = 0
            java.net.URLConnection r6 = r6.openConnection()     // Catch:{ Exception -> 0x007d, all -> 0x0078 }
            javax.net.ssl.HttpsURLConnection r6 = (javax.net.ssl.HttpsURLConnection) r6     // Catch:{ Exception -> 0x007d, all -> 0x0078 }
            r1 = 10000(0x2710, float:1.4013E-41)
            r6.setReadTimeout(r1)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r6.setConnectTimeout(r1)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            java.lang.String r1 = "GET"
            r6.setRequestMethod(r1)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r1 = 1
            r6.setDoInput(r1)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r6.connect()     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            int r1 = r6.getResponseCode()     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            int r2 = r6.getResponseCode()     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 != r3) goto L_0x0057
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            java.io.InputStream r2 = r6.getInputStream()     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            java.lang.String r0 = readAll((java.io.InputStream) r1)     // Catch:{ Exception -> 0x0055 }
            r1.close()     // Catch:{ IOException -> 0x0051 }
        L_0x0051:
            r6.disconnect()
            return r0
        L_0x0055:
            r0 = move-exception
            goto L_0x0081
        L_0x0057:
            java.io.IOException r2 = new java.io.IOException     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r3.<init>()     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            java.lang.String r4 = "HTTP error code: "
            r3.append(r4)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r3.append(r1)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0073, all -> 0x006e }
            throw r2     // Catch:{ Exception -> 0x0073, all -> 0x006e }
        L_0x006e:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x0083
        L_0x0073:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x0081
        L_0x0078:
            r6 = move-exception
            r1 = r0
            r0 = r6
            r6 = r1
            goto L_0x0083
        L_0x007d:
            r6 = move-exception
            r1 = r0
            r0 = r6
            r6 = r1
        L_0x0081:
            throw r0     // Catch:{ all -> 0x0082 }
        L_0x0082:
            r0 = move-exception
        L_0x0083:
            if (r1 == 0) goto L_0x0088
            r1.close()     // Catch:{ IOException -> 0x0088 }
        L_0x0088:
            if (r6 == 0) goto L_0x008d
            r6.disconnect()
        L_0x008d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.development.DSULoader.readAll(java.net.URL):java.lang.String");
    }

    private class Fetcher implements Runnable {
        private URL mDsuList;

        Fetcher(URL url) {
            this.mDsuList = url;
        }

        private void fetch(URL url) throws IOException, JSONException, MalformedURLException, ParseException {
            JSONObject jSONObject = new JSONObject(DSULoader.readAll(url));
            if (jSONObject.has("include")) {
                JSONArray jSONArray = jSONObject.getJSONArray("include");
                int length = jSONArray.length();
                for (int i = 0; i < length; i++) {
                    if (!jSONArray.isNull(i)) {
                        fetch(new URL(jSONArray.getString(i)));
                    }
                }
            }
            if (jSONObject.has("images")) {
                JSONArray jSONArray2 = jSONObject.getJSONArray("images");
                int length2 = jSONArray2.length();
                for (int i2 = 0; i2 < length2; i2++) {
                    DSUPackage dSUPackage = new DSUPackage(jSONArray2.getJSONObject(i2));
                    if (dSUPackage.isSupported()) {
                        DSULoader.this.mDSUList.add(dSUPackage);
                    }
                }
            }
        }

        public void run() {
            try {
                fetch(this.mDsuList);
            } catch (IOException e) {
                Slog.e("DSULOADER", e.toString());
                DSULoader.this.mDSUList.add(0, "Network Error");
            } catch (Exception e2) {
                Slog.e("DSULOADER", e2.toString());
                DSULoader.this.mDSUList.add(0, "Metadata Error");
            }
            if (DSULoader.this.mDSUList.size() == 0) {
                DSULoader.this.mDSUList.add(0, "No DSU available for this device");
            }
            DSULoader.this.runOnUiThread(new Runnable() {
                public void run() {
                    DSULoader.this.mAdapter.clear();
                    DSULoader.this.mAdapter.addAll(DSULoader.this.mDSUList);
                }
            });
        }
    }

    private class DSUPackage {
        String mCpuAbi = null;
        String mDetails = null;
        String mName = null;
        int mOsVersion = -1;
        String mPubKey = "";
        Date mSPL = null;
        URL mTosUrl = null;
        URL mUri;
        int[] mVndk = null;

        DSUPackage(JSONObject jSONObject) throws JSONException, MalformedURLException, ParseException {
            Slog.i("DSULOADER", "DSUPackage: " + jSONObject.toString());
            this.mName = jSONObject.getString("name");
            this.mDetails = jSONObject.getString("details");
            this.mCpuAbi = jSONObject.getString("cpu_abi");
            this.mUri = new URL(jSONObject.getString("uri"));
            if (jSONObject.has("os_version")) {
                this.mOsVersion = dessertNumber(jSONObject.getString("os_version"), 10);
            }
            if (jSONObject.has("vndk")) {
                JSONArray jSONArray = jSONObject.getJSONArray("vndk");
                this.mVndk = new int[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); i++) {
                    this.mVndk[i] = jSONArray.getInt(i);
                }
            }
            if (jSONObject.has("pubkey")) {
                this.mPubKey = jSONObject.getString("pubkey");
            }
            if (jSONObject.has("tos")) {
                this.mTosUrl = new URL(jSONObject.getString("tos"));
            }
            if (jSONObject.has("spl")) {
                this.mSPL = new SimpleDateFormat("yyyy-MM-dd").parse(jSONObject.getString("spl"));
            }
        }

        /* access modifiers changed from: package-private */
        public int dessertNumber(String str, int i) {
            if (str == null || str.isEmpty()) {
                return -1;
            }
            if (Character.isDigit(str.charAt(0))) {
                return Integer.parseInt(str);
            }
            return (str.toUpperCase().charAt(0) - 'Q') + i;
        }

        /* access modifiers changed from: package-private */
        public int getDeviceVndk() {
            return dessertNumber(SystemProperties.get("ro.vndk.version"), 28);
        }

        /* access modifiers changed from: package-private */
        public int getDeviceOs() {
            return dessertNumber(SystemProperties.get("ro.system.build.version.release"), 10);
        }

        /* access modifiers changed from: package-private */
        public String getDeviceCpu() {
            String lowerCase = SystemProperties.get("ro.product.cpu.abi").toLowerCase();
            return lowerCase.startsWith("aarch64") ? "arm64-v8a" : lowerCase;
        }

        /* access modifiers changed from: package-private */
        public Date getDeviceSPL() {
            String str = SystemProperties.get("ro.build.version.security_patch");
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(str);
            } catch (ParseException unused) {
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isSupported() {
            boolean z;
            String deviceCpu = getDeviceCpu();
            boolean z2 = true;
            boolean z3 = false;
            if (!this.mCpuAbi.equals(deviceCpu)) {
                Slog.i("DSULOADER", this.mCpuAbi + " != " + deviceCpu);
                z = false;
            } else {
                z = true;
            }
            if (this.mOsVersion > 0) {
                int deviceOs = getDeviceOs();
                if (deviceOs < 0) {
                    Slog.i("DSULOADER", "Failed to getDeviceOs");
                } else if (this.mOsVersion < deviceOs) {
                    Slog.i("DSULOADER", this.mOsVersion + " < " + deviceOs);
                }
                z = false;
            }
            if (this.mVndk != null) {
                int deviceVndk = getDeviceVndk();
                if (deviceVndk < 0) {
                    Slog.i("DSULOADER", "Failed to getDeviceVndk");
                } else {
                    int i = 0;
                    while (true) {
                        int[] iArr = this.mVndk;
                        if (i >= iArr.length) {
                            z2 = false;
                            break;
                        } else if (iArr[i] == deviceVndk) {
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (!z2) {
                        Slog.i("DSULOADER", "vndk:" + deviceVndk + " not found");
                    }
                }
                z = false;
            }
            if (this.mSPL != null) {
                Date deviceSPL = getDeviceSPL();
                if (deviceSPL == null) {
                    Slog.i("DSULOADER", "Failed to getDeviceSPL");
                } else if (deviceSPL.getTime() > this.mSPL.getTime()) {
                    Slog.i("DSULOADER", "Device SPL:" + deviceSPL.toString() + " > " + this.mSPL.toString());
                }
                Slog.i("DSULOADER", this.mName + " isSupported " + z3);
                return z3;
            }
            z3 = z;
            Slog.i("DSULOADER", this.mName + " isSupported " + z3);
            return z3;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String str = SystemProperties.get("persist.sys.fflag.override.settings_dynamic_system.list");
        Slog.e("DSULOADER", "Try to get DSU list from: persist.sys.fflag.override.settings_dynamic_system.list");
        if (str == null || str.isEmpty()) {
            str = "https://dl.google.com/developers/android/gsi/gsi-src.json";
        }
        Slog.e("DSULOADER", "DSU list: " + str);
        try {
            URL url = new URL(str);
            DSUPackageListAdapter dSUPackageListAdapter = new DSUPackageListAdapter(this);
            this.mAdapter = dSUPackageListAdapter;
            setListAdapter(dSUPackageListAdapter);
            this.mAdapter.add(getResources().getString(R.string.dsu_loader_loading));
            new Thread(new Fetcher(url)).start();
        } catch (MalformedURLException e) {
            Slog.e("DSULOADER", e.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView listView, View view, int i, long j) {
        Object item = this.mAdapter.getItem(i);
        if (item instanceof DSUPackage) {
            final DSUPackage dSUPackage = (DSUPackage) item;
            this.mAdapter.clear();
            this.mAdapter.add(getResources().getString(R.string.dsu_loader_loading));
            new Thread(new Runnable() {
                public void run() {
                    String str;
                    URL url = dSUPackage.mTosUrl;
                    if (url != null) {
                        try {
                            str = DSULoader.readAll(url);
                        } catch (IOException e) {
                            Slog.e("DSULOADER", e.toString());
                        }
                        Intent intent = new Intent(DSULoader.this, DSUTermsOfServiceActivity.class);
                        intent.putExtra("KEY_TOS", str);
                        intent.setData(Uri.parse(dSUPackage.mUri.toString()));
                        intent.putExtra("KEY_PUBKEY", dSUPackage.mPubKey);
                        DSULoader.this.startActivity(intent);
                    }
                    str = "";
                    Intent intent2 = new Intent(DSULoader.this, DSUTermsOfServiceActivity.class);
                    intent2.putExtra("KEY_TOS", str);
                    intent2.setData(Uri.parse(dSUPackage.mUri.toString()));
                    intent2.putExtra("KEY_PUBKEY", dSUPackage.mPubKey);
                    DSULoader.this.startActivity(intent2);
                }
            }).start();
        }
        finish();
    }

    private class DSUPackageListAdapter extends ArrayAdapter<Object> {
        private final LayoutInflater mInflater;

        DSUPackageListAdapter(Context context) {
            super(context, 0);
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            AppViewHolder createOrRecycle = AppViewHolder.createOrRecycle(this.mInflater, view);
            View view2 = createOrRecycle.rootView;
            Object item = getItem(i);
            if (item instanceof DSUPackage) {
                DSUPackage dSUPackage = (DSUPackage) item;
                createOrRecycle.appName.setText(dSUPackage.mName);
                createOrRecycle.summary.setText(dSUPackage.mDetails);
            } else {
                createOrRecycle.summary.setText((String) item);
            }
            createOrRecycle.appIcon.setImageDrawable((Drawable) null);
            createOrRecycle.disabled.setVisibility(8);
            return view2;
        }
    }
}
