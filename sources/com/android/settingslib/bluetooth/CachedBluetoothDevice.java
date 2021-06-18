package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AdaptiveOutlineDrawable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CachedBluetoothDevice implements Comparable<CachedBluetoothDevice> {
    private final Collection<Callback> mCallbacks = new CopyOnWriteArrayList();
    private long mConnectAttempted;
    private final Context mContext;
    BluetoothDevice mDevice;
    LruCache<String, BitmapDrawable> mDrawableCache;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                boolean unused = CachedBluetoothDevice.this.mIsHeadsetProfileConnectedFail = true;
            } else if (i == 2) {
                boolean unused2 = CachedBluetoothDevice.this.mIsA2dpProfileConnectedFail = true;
            } else if (i != 21) {
                Log.w("CachedBluetoothDevice", "handleMessage(): unknown message : " + message.what);
            } else {
                boolean unused3 = CachedBluetoothDevice.this.mIsHearingAidProfileConnectedFail = true;
            }
            Log.w("CachedBluetoothDevice", "Connect to profile : " + message.what + " timeout, show error message !");
            CachedBluetoothDevice.this.refresh();
        }
    };
    private long mHiSyncId;
    /* access modifiers changed from: private */
    public boolean mIsA2dpProfileConnectedFail = false;
    private boolean mIsActiveDeviceA2dp = false;
    private boolean mIsActiveDeviceHeadset = false;
    private boolean mIsActiveDeviceHearingAid = false;
    /* access modifiers changed from: private */
    public boolean mIsHeadsetProfileConnectedFail = false;
    /* access modifiers changed from: private */
    public boolean mIsHearingAidProfileConnectedFail = false;
    boolean mJustDiscovered;
    private final BluetoothAdapter mLocalAdapter;
    private boolean mLocalNapRoleConnected;
    private final Object mProfileLock = new Object();
    private final LocalBluetoothProfileManager mProfileManager;
    private final Collection<LocalBluetoothProfile> mProfiles = new CopyOnWriteArrayList();
    private final Collection<LocalBluetoothProfile> mRemovedProfiles = new CopyOnWriteArrayList();
    short mRssi;
    private CachedBluetoothDevice mSubDevice;

    public interface Callback {
        void onDeviceAttributesChanged();
    }

    private boolean isTwsBatteryAvailable(int i, int i2) {
        return i >= 0 && i2 >= 0;
    }

    CachedBluetoothDevice(Context context, LocalBluetoothProfileManager localBluetoothProfileManager, BluetoothDevice bluetoothDevice) {
        this.mContext = context;
        this.mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileManager = localBluetoothProfileManager;
        this.mDevice = bluetoothDevice;
        fillData();
        this.mHiSyncId = 0;
        initDrawableCache();
    }

    private void initDrawableCache() {
        this.mDrawableCache = new LruCache<String, BitmapDrawable>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) {
            /* access modifiers changed from: protected */
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount() / 1024;
            }
        };
    }

    private String describe(LocalBluetoothProfile localBluetoothProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append("Address:");
        sb.append(this.mDevice);
        if (localBluetoothProfile != null) {
            sb.append(" Profile:");
            sb.append(localBluetoothProfile);
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d("CachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device " + this.mDevice.getAlias() + ", newProfileState " + i);
        if (this.mLocalAdapter.getState() == 13) {
            Log.d("CachedBluetoothDevice", " BT Turninig Off...Profile conn state change ignored...");
            return;
        }
        synchronized (this.mProfileLock) {
            if ((localBluetoothProfile instanceof A2dpProfile) || (localBluetoothProfile instanceof HeadsetProfile) || (localBluetoothProfile instanceof HearingAidProfile)) {
                setProfileConnectedStatus(localBluetoothProfile.getProfileId(), false);
                if (i != 0) {
                    if (i == 1) {
                        this.mHandler.sendEmptyMessageDelayed(localBluetoothProfile.getProfileId(), 60000);
                    } else if (i == 2) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    } else if (i != 3) {
                        Log.w("CachedBluetoothDevice", "onProfileStateChanged(): unknown profile state : " + i);
                    } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    }
                } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                    this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    setProfileConnectedStatus(localBluetoothProfile.getProfileId(), true);
                }
            }
            if (i == 2) {
                if (localBluetoothProfile instanceof MapProfile) {
                    localBluetoothProfile.setEnabled(this.mDevice, true);
                }
                if (!this.mProfiles.contains(localBluetoothProfile)) {
                    this.mRemovedProfiles.remove(localBluetoothProfile);
                    this.mProfiles.add(localBluetoothProfile);
                    if ((localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice)) {
                        this.mLocalNapRoleConnected = true;
                    }
                }
            } else if ((localBluetoothProfile instanceof MapProfile) && i == 0) {
                localBluetoothProfile.setEnabled(this.mDevice, false);
            } else if (this.mLocalNapRoleConnected && (localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice) && i == 0) {
                Log.d("CachedBluetoothDevice", "Removing PanProfile from device after NAP disconnect");
                this.mProfiles.remove(localBluetoothProfile);
                this.mRemovedProfiles.add(localBluetoothProfile);
                this.mLocalNapRoleConnected = false;
            }
        }
        fetchActiveDevices();
    }

    /* access modifiers changed from: package-private */
    public void setProfileConnectedStatus(int i, boolean z) {
        if (i == 1) {
            this.mIsHeadsetProfileConnectedFail = z;
        } else if (i == 2) {
            this.mIsA2dpProfileConnectedFail = z;
        } else if (i != 21) {
            Log.w("CachedBluetoothDevice", "setProfileConnectedStatus(): unknown profile id : " + i);
        } else {
            this.mIsHearingAidProfileConnectedFail = z;
        }
    }

    public void disconnect() {
        synchronized (this.mProfileLock) {
            this.mLocalAdapter.disconnectAllEnabledProfiles(this.mDevice);
        }
        PbapServerProfile pbapProfile = this.mProfileManager.getPbapProfile();
        if (pbapProfile != null && isConnectedProfile(pbapProfile)) {
            pbapProfile.setEnabled(this.mDevice, false);
        }
    }

    public void connect() {
        if (ensurePaired()) {
            this.mConnectAttempted = SystemClock.elapsedRealtime();
            connectAllEnabledProfiles();
        }
    }

    public long getHiSyncId() {
        return this.mHiSyncId;
    }

    public void setHiSyncId(long j) {
        this.mHiSyncId = j;
    }

    public boolean isHearingAidDevice() {
        return this.mHiSyncId != 0;
    }

    private void connectAllEnabledProfiles() {
        synchronized (this.mProfileLock) {
            if (this.mProfiles.isEmpty()) {
                Log.d("CachedBluetoothDevice", "No profiles. Maybe we will connect later for device " + this.mDevice);
                return;
            }
            this.mLocalAdapter.connectAllEnabledProfiles(this.mDevice);
        }
    }

    private boolean ensurePaired() {
        if (getBondState() != 10) {
            return true;
        }
        startPairing();
        return false;
    }

    public boolean startPairing() {
        if (this.mLocalAdapter.isDiscovering()) {
            this.mLocalAdapter.cancelDiscovery();
        }
        return this.mDevice.createBond();
    }

    public void unpair() {
        BluetoothDevice bluetoothDevice;
        int bondState = getBondState();
        if (bondState == 11) {
            this.mDevice.cancelBondProcess();
        }
        if (bondState != 10 && (bluetoothDevice = this.mDevice) != null && bluetoothDevice.removeBond()) {
            releaseLruCache();
            Log.d("CachedBluetoothDevice", "Command sent successfully:REMOVE_BOND " + describe((LocalBluetoothProfile) null));
        }
    }

    public int getProfileConnectionState(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null) {
            return localBluetoothProfile.getConnectionStatus(this.mDevice);
        }
        return 0;
    }

    private void fillData() {
        updateProfiles();
        fetchActiveDevices();
        migratePhonebookPermissionChoice();
        migrateMessagePermissionChoice();
        lambda$refresh$0();
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public String getAddress() {
        return this.mDevice.getAddress();
    }

    public String getName() {
        String alias = this.mDevice.getAlias();
        return TextUtils.isEmpty(alias) ? getAddress() : alias;
    }

    public void setName(String str) {
        if (str != null && !TextUtils.equals(str, getName())) {
            this.mDevice.setAlias(str);
            lambda$refresh$0();
        }
    }

    public boolean setActive() {
        boolean z;
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile == null || !isConnectedProfile(a2dpProfile) || !a2dpProfile.setActiveDevice(getDevice())) {
            z = false;
        } else {
            Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: A2DP active device=" + this);
            z = true;
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null && isConnectedProfile(headsetProfile) && headsetProfile.setActiveDevice(getDevice())) {
            Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: Headset active device=" + this);
            z = true;
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile == null || !isConnectedProfile(hearingAidProfile) || !hearingAidProfile.setActiveDevice(getDevice())) {
            return z;
        }
        Log.i("CachedBluetoothDevice", "OnPreferenceClickListener: Hearing Aid active device=" + this);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void refreshName() {
        Log.d("CachedBluetoothDevice", "Device name: " + getName());
        lambda$refresh$0();
    }

    public boolean hasHumanReadableName() {
        return !TextUtils.isEmpty(this.mDevice.getAlias());
    }

    public int getBatteryLevel() {
        return this.mDevice.getBatteryLevel();
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        ThreadUtils.postOnBackgroundThread((Runnable) new CachedBluetoothDevice$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$1() {
        Uri uriMetaData;
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && (uriMetaData = BluetoothUtils.getUriMetaData(getDevice(), 5)) != null && this.mDrawableCache.get(uriMetaData.toString()) == null) {
            this.mDrawableCache.put(uriMetaData.toString(), (BitmapDrawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this).first);
        }
        ThreadUtils.postOnMainThread(new CachedBluetoothDevice$$ExternalSyntheticLambda1(this));
    }

    public void setJustDiscovered(boolean z) {
        if (this.mJustDiscovered != z) {
            this.mJustDiscovered = z;
            lambda$refresh$0();
        }
    }

    public int getBondState() {
        return this.mDevice.getBondState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActiveDeviceChanged(boolean r4, int r5) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            if (r5 == r0) goto L_0x003c
            r2 = 2
            if (r5 == r2) goto L_0x0033
            r2 = 21
            if (r5 == r2) goto L_0x002a
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "onActiveDeviceChanged: unknown profile "
            r0.append(r2)
            r0.append(r5)
            java.lang.String r5 = " isActive "
            r0.append(r5)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            java.lang.String r5 = "CachedBluetoothDevice"
            android.util.Log.w(r5, r4)
            goto L_0x0045
        L_0x002a:
            boolean r5 = r3.mIsActiveDeviceHearingAid
            if (r5 == r4) goto L_0x002f
            goto L_0x0030
        L_0x002f:
            r0 = r1
        L_0x0030:
            r3.mIsActiveDeviceHearingAid = r4
            goto L_0x0044
        L_0x0033:
            boolean r5 = r3.mIsActiveDeviceA2dp
            if (r5 == r4) goto L_0x0038
            goto L_0x0039
        L_0x0038:
            r0 = r1
        L_0x0039:
            r3.mIsActiveDeviceA2dp = r4
            goto L_0x0044
        L_0x003c:
            boolean r5 = r3.mIsActiveDeviceHeadset
            if (r5 == r4) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = r1
        L_0x0042:
            r3.mIsActiveDeviceHeadset = r4
        L_0x0044:
            r1 = r0
        L_0x0045:
            if (r1 == 0) goto L_0x004a
            r3.lambda$refresh$0()
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.onActiveDeviceChanged(boolean, int):void");
    }

    /* access modifiers changed from: package-private */
    public void onAudioModeChanged() {
        lambda$refresh$0();
    }

    public boolean isActiveDevice(int i) {
        if (i == 1) {
            return this.mIsActiveDeviceHeadset;
        }
        if (i == 2) {
            return this.mIsActiveDeviceA2dp;
        }
        if (i == 21) {
            return this.mIsActiveDeviceHearingAid;
        }
        Log.w("CachedBluetoothDevice", "getActiveDevice: unknown profile " + i);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setRssi(short s) {
        if (this.mRssi != s) {
            this.mRssi = s;
            lambda$refresh$0();
        }
    }

    public boolean isConnected() {
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile profileConnectionState : this.mProfiles) {
                if (getProfileConnectionState(profileConnectionState) == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isConnectedProfile(LocalBluetoothProfile localBluetoothProfile) {
        return getProfileConnectionState(localBluetoothProfile) == 2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
        return r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0021 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBusy() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mProfileLock
            monitor-enter(r0)
            java.util.Collection<com.android.settingslib.bluetooth.LocalBluetoothProfile> r1 = r5.mProfiles     // Catch:{ all -> 0x002d }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x002d }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x002d }
            r3 = 1
            if (r2 == 0) goto L_0x0021
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x002d }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r2 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r2     // Catch:{ all -> 0x002d }
            int r2 = r5.getProfileConnectionState(r2)     // Catch:{ all -> 0x002d }
            if (r2 == r3) goto L_0x001f
            r4 = 3
            if (r2 != r4) goto L_0x0009
        L_0x001f:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x0021:
            int r5 = r5.getBondState()     // Catch:{ all -> 0x002d }
            r1 = 11
            if (r5 != r1) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r3 = 0
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x002d:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.isBusy():boolean");
    }

    private boolean updateProfiles() {
        ParcelUuid[] uuids;
        ParcelUuid[] uuids2 = this.mDevice.getUuids();
        if (uuids2 == null || (uuids = this.mLocalAdapter.getUuids()) == null) {
            return false;
        }
        processPhonebookAccess();
        synchronized (this.mProfileLock) {
            this.mProfileManager.updateProfiles(uuids2, uuids, this.mProfiles, this.mRemovedProfiles, this.mLocalNapRoleConnected, this.mDevice);
        }
        Log.d("CachedBluetoothDevice", "updating profiles for " + this.mDevice.getAlias());
        BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            Log.v("CachedBluetoothDevice", "Class: " + bluetoothClass.toString());
        }
        Log.v("CachedBluetoothDevice", "UUID:");
        for (ParcelUuid parcelUuid : uuids2) {
            Log.v("CachedBluetoothDevice", "  " + parcelUuid);
        }
        return true;
    }

    private void fetchActiveDevices() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile != null) {
            this.mIsActiveDeviceA2dp = this.mDevice.equals(a2dpProfile.getActiveDevice());
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null) {
            this.mIsActiveDeviceHeadset = this.mDevice.equals(headsetProfile.getActiveDevice());
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null) {
            this.mIsActiveDeviceHearingAid = hearingAidProfile.getActiveDevices().contains(this.mDevice);
        }
    }

    /* access modifiers changed from: package-private */
    public void onUuidChanged() {
        long j;
        updateProfiles();
        ParcelUuid[] uuids = this.mDevice.getUuids();
        if (ArrayUtils.contains(uuids, BluetoothUuid.HOGP)) {
            j = 30000;
        } else {
            j = ArrayUtils.contains(uuids, BluetoothUuid.HEARING_AID) ? 15000 : 5000;
        }
        Log.d("CachedBluetoothDevice", "onUuidChanged: Time since last connect=" + (SystemClock.elapsedRealtime() - this.mConnectAttempted));
        if (this.mConnectAttempted + j > SystemClock.elapsedRealtime()) {
            Log.d("CachedBluetoothDevice", "onUuidChanged: triggering connectAllEnabledProfiles");
            connectAllEnabledProfiles();
        }
        lambda$refresh$0();
    }

    /* access modifiers changed from: package-private */
    public void onBondingStateChanged(int i) {
        if (i == 10) {
            synchronized (this.mProfileLock) {
                this.mProfiles.clear();
            }
            this.mDevice.setPhonebookAccessPermission(0);
            this.mDevice.setMessageAccessPermission(0);
            this.mDevice.setSimAccessPermission(0);
        }
        refresh();
        if (i == 12 && this.mDevice.isBondingInitiatedLocally()) {
            connect();
        }
    }

    public BluetoothClass getBtClass() {
        return this.mDevice.getBluetoothClass();
    }

    public List<LocalBluetoothProfile> getProfiles() {
        return new ArrayList(this.mProfiles);
    }

    public List<LocalBluetoothProfile> getConnectableProfiles() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile next : this.mProfiles) {
                if (next.accessProfileEnabled()) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public List<LocalBluetoothProfile> getRemovedProfiles() {
        return new ArrayList(this.mRemovedProfiles);
    }

    public void registerCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void unregisterCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: dispatchAttributesChanged */
    public void lambda$refresh$0() {
        for (Callback onDeviceAttributesChanged : this.mCallbacks) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    public String toString() {
        return this.mDevice.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CachedBluetoothDevice)) {
            return false;
        }
        return this.mDevice.equals(((CachedBluetoothDevice) obj).mDevice);
    }

    public int hashCode() {
        return this.mDevice.getAddress().hashCode();
    }

    public int compareTo(CachedBluetoothDevice cachedBluetoothDevice) {
        int i = (cachedBluetoothDevice.isConnected() ? 1 : 0) - (isConnected() ? 1 : 0);
        if (i != 0) {
            return i;
        }
        int i2 = 1;
        int i3 = cachedBluetoothDevice.getBondState() == 12 ? 1 : 0;
        if (getBondState() != 12) {
            i2 = 0;
        }
        int i4 = i3 - i2;
        if (i4 != 0) {
            return i4;
        }
        int i5 = (cachedBluetoothDevice.mJustDiscovered ? 1 : 0) - (this.mJustDiscovered ? 1 : 0);
        if (i5 != 0) {
            return i5;
        }
        int i6 = cachedBluetoothDevice.mRssi - this.mRssi;
        if (i6 != 0) {
            return i6;
        }
        return getName().compareTo(cachedBluetoothDevice.getName());
    }

    private void migratePhonebookPermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_phonebook_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setPhonebookAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setPhonebookAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void migrateMessagePermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_message_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getMessageAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setMessageAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setMessageAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void processPhonebookAccess() {
        if (this.mDevice.getBondState() == 12 && BluetoothUuid.containsAnyUuid(this.mDevice.getUuids(), PbapServerProfile.PBAB_CLIENT_UUIDS)) {
            BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                if (bluetoothClass != null && (bluetoothClass.getDeviceClass() == 1032 || bluetoothClass.getDeviceClass() == 1028)) {
                    EventLog.writeEvent(1397638484, new Object[]{"138529441", -1, ""});
                }
                this.mDevice.setPhonebookAccessPermission(2);
            }
        }
    }

    public String getConnectionSummary() {
        return getConnectionSummary(false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0072, code lost:
        r0 = getBatteryLevel();
        r8 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0078, code lost:
        if (r0 <= -1) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x007a, code lost:
        r0 = com.android.settingslib.Utils.formatPercentage(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x007f, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0080, code lost:
        r10 = com.android.settingslib.R$string.bluetooth_pairing;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0084, code lost:
        if (r4 == false) goto L_0x00df;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x008d, code lost:
        if (com.android.settingslib.bluetooth.BluetoothUtils.getBooleanMetaData(r13.mDevice, 6) == false) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x008f, code lost:
        r8 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r13.mDevice, 10);
        r4 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r13.mDevice, 11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x009e, code lost:
        r4 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00a3, code lost:
        if (isTwsBatteryAvailable(r8, r4) == false) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00a5, code lost:
        r12 = com.android.settingslib.R$string.bluetooth_battery_level_untethered;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a8, code lost:
        if (r0 == null) goto L_0x00ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00aa, code lost:
        r12 = com.android.settingslib.R$string.bluetooth_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ad, code lost:
        r12 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00ae, code lost:
        if (r5 != false) goto L_0x00b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b0, code lost:
        if (r6 != false) goto L_0x00b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00b2, code lost:
        if (r7 == false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00b4, code lost:
        r5 = com.android.settingslib.Utils.isAudioModeOngoingCall(r13.mContext);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00bc, code lost:
        if (r13.mIsActiveDeviceHearingAid != false) goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00c0, code lost:
        if (r13.mIsActiveDeviceHeadset == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00c2, code lost:
        if (r5 != false) goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00c6, code lost:
        if (r13.mIsActiveDeviceA2dp == false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00c8, code lost:
        if (r5 != false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00ce, code lost:
        if (isTwsBatteryAvailable(r8, r4) == false) goto L_0x00d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00d0, code lost:
        if (r14 != false) goto L_0x00d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00d2, code lost:
        r12 = com.android.settingslib.R$string.bluetooth_active_battery_level_untethered;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00d5, code lost:
        if (r0 == null) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00d7, code lost:
        if (r14 != false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00d9, code lost:
        r12 = com.android.settingslib.R$string.bluetooth_active_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00dc, code lost:
        r12 = com.android.settingslib.R$string.bluetooth_active_no_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00df, code lost:
        r4 = -1;
        r12 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00e1, code lost:
        if (r12 != r10) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00e7, code lost:
        if (getBondState() != 11) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00ea, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x00ef, code lost:
        if (isTwsBatteryAvailable(r8, r4) == false) goto L_0x0106;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0105, code lost:
        return r13.mContext.getString(r12, new java.lang.Object[]{com.android.settingslib.Utils.formatPercentage(r8), com.android.settingslib.Utils.formatPercentage(r4)});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0110, code lost:
        return r13.mContext.getString(r12, new java.lang.Object[]{r0});
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getConnectionSummary(boolean r14) {
        /*
            r13 = this;
            boolean r0 = r13.isProfileConnectedFail()
            if (r0 == 0) goto L_0x0015
            boolean r0 = r13.isConnected()
            if (r0 == 0) goto L_0x0015
            android.content.Context r13 = r13.mContext
            int r14 = com.android.settingslib.R$string.profile_connect_timeout_subtext
            java.lang.String r13 = r13.getString(r14)
            return r13
        L_0x0015:
            java.lang.Object r0 = r13.mProfileLock
            monitor-enter(r0)
            java.util.List r1 = r13.getProfiles()     // Catch:{ all -> 0x0111 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0111 }
            r2 = 0
            r3 = 1
            r4 = r2
            r5 = r3
            r6 = r5
            r7 = r6
        L_0x0026:
            boolean r8 = r1.hasNext()     // Catch:{ all -> 0x0111 }
            r9 = 2
            if (r8 == 0) goto L_0x0071
            java.lang.Object r8 = r1.next()     // Catch:{ all -> 0x0111 }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r8 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r8     // Catch:{ all -> 0x0111 }
            int r10 = r13.getProfileConnectionState(r8)     // Catch:{ all -> 0x0111 }
            if (r10 == 0) goto L_0x004f
            if (r10 == r3) goto L_0x0043
            if (r10 == r9) goto L_0x0041
            r8 = 3
            if (r10 == r8) goto L_0x0043
            goto L_0x0026
        L_0x0041:
            r4 = r3
            goto L_0x0026
        L_0x0043:
            android.content.Context r13 = r13.mContext     // Catch:{ all -> 0x0111 }
            int r14 = com.android.settingslib.bluetooth.BluetoothUtils.getConnectionStateSummary(r10)     // Catch:{ all -> 0x0111 }
            java.lang.String r13 = r13.getString(r14)     // Catch:{ all -> 0x0111 }
            monitor-exit(r0)     // Catch:{ all -> 0x0111 }
            return r13
        L_0x004f:
            boolean r9 = r8.isProfileReady()     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x0026
            boolean r9 = r8 instanceof com.android.settingslib.bluetooth.A2dpProfile     // Catch:{ all -> 0x0111 }
            if (r9 != 0) goto L_0x006f
            boolean r9 = r8 instanceof com.android.settingslib.bluetooth.A2dpSinkProfile     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x005e
            goto L_0x006f
        L_0x005e:
            boolean r9 = r8 instanceof com.android.settingslib.bluetooth.HeadsetProfile     // Catch:{ all -> 0x0111 }
            if (r9 != 0) goto L_0x006d
            boolean r9 = r8 instanceof com.android.settingslib.bluetooth.HfpClientProfile     // Catch:{ all -> 0x0111 }
            if (r9 == 0) goto L_0x0067
            goto L_0x006d
        L_0x0067:
            boolean r8 = r8 instanceof com.android.settingslib.bluetooth.HearingAidProfile     // Catch:{ all -> 0x0111 }
            if (r8 == 0) goto L_0x0026
            r7 = r2
            goto L_0x0026
        L_0x006d:
            r6 = r2
            goto L_0x0026
        L_0x006f:
            r5 = r2
            goto L_0x0026
        L_0x0071:
            monitor-exit(r0)     // Catch:{ all -> 0x0111 }
            int r0 = r13.getBatteryLevel()
            r1 = 0
            r8 = -1
            if (r0 <= r8) goto L_0x007f
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r0)
            goto L_0x0080
        L_0x007f:
            r0 = r1
        L_0x0080:
            int r10 = com.android.settingslib.R$string.bluetooth_pairing
            r11 = 11
            if (r4 == 0) goto L_0x00df
            android.bluetooth.BluetoothDevice r4 = r13.mDevice
            r12 = 6
            boolean r4 = com.android.settingslib.bluetooth.BluetoothUtils.getBooleanMetaData(r4, r12)
            if (r4 == 0) goto L_0x009e
            android.bluetooth.BluetoothDevice r4 = r13.mDevice
            r8 = 10
            int r8 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r4, r8)
            android.bluetooth.BluetoothDevice r4 = r13.mDevice
            int r4 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r4, r11)
            goto L_0x009f
        L_0x009e:
            r4 = r8
        L_0x009f:
            boolean r12 = r13.isTwsBatteryAvailable(r8, r4)
            if (r12 == 0) goto L_0x00a8
            int r12 = com.android.settingslib.R$string.bluetooth_battery_level_untethered
            goto L_0x00ae
        L_0x00a8:
            if (r0 == 0) goto L_0x00ad
            int r12 = com.android.settingslib.R$string.bluetooth_battery_level
            goto L_0x00ae
        L_0x00ad:
            r12 = r10
        L_0x00ae:
            if (r5 != 0) goto L_0x00b4
            if (r6 != 0) goto L_0x00b4
            if (r7 == 0) goto L_0x00e1
        L_0x00b4:
            android.content.Context r5 = r13.mContext
            boolean r5 = com.android.settingslib.Utils.isAudioModeOngoingCall(r5)
            boolean r6 = r13.mIsActiveDeviceHearingAid
            if (r6 != 0) goto L_0x00ca
            boolean r6 = r13.mIsActiveDeviceHeadset
            if (r6 == 0) goto L_0x00c4
            if (r5 != 0) goto L_0x00ca
        L_0x00c4:
            boolean r6 = r13.mIsActiveDeviceA2dp
            if (r6 == 0) goto L_0x00e1
            if (r5 != 0) goto L_0x00e1
        L_0x00ca:
            boolean r5 = r13.isTwsBatteryAvailable(r8, r4)
            if (r5 == 0) goto L_0x00d5
            if (r14 != 0) goto L_0x00d5
            int r12 = com.android.settingslib.R$string.bluetooth_active_battery_level_untethered
            goto L_0x00e1
        L_0x00d5:
            if (r0 == 0) goto L_0x00dc
            if (r14 != 0) goto L_0x00dc
            int r12 = com.android.settingslib.R$string.bluetooth_active_battery_level
            goto L_0x00e1
        L_0x00dc:
            int r12 = com.android.settingslib.R$string.bluetooth_active_no_battery_level
            goto L_0x00e1
        L_0x00df:
            r4 = r8
            r12 = r10
        L_0x00e1:
            if (r12 != r10) goto L_0x00eb
            int r14 = r13.getBondState()
            if (r14 != r11) goto L_0x00ea
            goto L_0x00eb
        L_0x00ea:
            return r1
        L_0x00eb:
            boolean r14 = r13.isTwsBatteryAvailable(r8, r4)
            if (r14 == 0) goto L_0x0106
            android.content.Context r13 = r13.mContext
            java.lang.Object[] r14 = new java.lang.Object[r9]
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r8)
            r14[r2] = r0
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r4)
            r14[r3] = r0
            java.lang.String r13 = r13.getString(r12, r14)
            return r13
        L_0x0106:
            android.content.Context r13 = r13.mContext
            java.lang.Object[] r14 = new java.lang.Object[r3]
            r14[r2] = r0
            java.lang.String r13 = r13.getString(r12, r14)
            return r13
        L_0x0111:
            r13 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0111 }
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.getConnectionSummary(boolean):java.lang.String");
    }

    private boolean isProfileConnectedFail() {
        return this.mIsA2dpProfileConnectedFail || this.mIsHearingAidProfileConnectedFail || (!isConnectedSapDevice() && this.mIsHeadsetProfileConnectedFail);
    }

    public boolean isConnectedA2dpDevice() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        return a2dpProfile != null && a2dpProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedHfpDevice() {
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        return headsetProfile != null && headsetProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedHearingAidDevice() {
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        return hearingAidProfile != null && hearingAidProfile.getConnectionStatus(this.mDevice) == 2;
    }

    private boolean isConnectedSapDevice() {
        SapProfile sapProfile = this.mProfileManager.getSapProfile();
        return sapProfile != null && sapProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public CachedBluetoothDevice getSubDevice() {
        return this.mSubDevice;
    }

    public void setSubDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mSubDevice = cachedBluetoothDevice;
    }

    public void switchSubDeviceContent() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        CachedBluetoothDevice cachedBluetoothDevice = this.mSubDevice;
        this.mDevice = cachedBluetoothDevice.mDevice;
        this.mRssi = cachedBluetoothDevice.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice.mJustDiscovered;
        cachedBluetoothDevice.mDevice = bluetoothDevice;
        cachedBluetoothDevice.mRssi = s;
        cachedBluetoothDevice.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public Pair<Drawable, String> getDrawableWithDescription() {
        Uri uriMetaData = BluetoothUtils.getUriMetaData(this.mDevice, 5);
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && uriMetaData != null) {
            BitmapDrawable bitmapDrawable = this.mDrawableCache.get(uriMetaData.toString());
            if (bitmapDrawable != null) {
                return new Pair<>(new AdaptiveOutlineDrawable(this.mContext.getResources(), bitmapDrawable.getBitmap()), (String) BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this).second);
            }
            refresh();
        }
        return BluetoothUtils.getBtRainbowDrawableWithDescription(this.mContext, this);
    }

    /* access modifiers changed from: package-private */
    public void releaseLruCache() {
        this.mDrawableCache.evictAll();
    }
}
