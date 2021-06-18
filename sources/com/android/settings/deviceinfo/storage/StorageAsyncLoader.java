package com.android.settings.deviceinfo.storage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import com.android.settingslib.applications.StorageStatsSource;
import com.android.settingslib.utils.AsyncLoaderCompat;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StorageAsyncLoader extends AsyncLoaderCompat<SparseArray<AppsStorageResult>> {
    private PackageManager mPackageManager;
    private ArraySet<String> mSeenPackages;
    private StorageStatsSource mStatsManager;
    private UserManager mUserManager;
    private String mUuid;

    public static class AppsStorageResult {
        public long cacheSize;
        public long duplicateCodeSize;
        public StorageStatsSource.ExternalStorageStats externalStats;
        public long gamesSize;
        public long musicAppsSize;
        public long otherAppsSize;
        public long photosAppsSize;
        public long videoAppsSize;
    }

    public interface ResultHandler {
        void handleResult(SparseArray<AppsStorageResult> sparseArray);
    }

    /* access modifiers changed from: protected */
    public void onDiscardResult(SparseArray<AppsStorageResult> sparseArray) {
    }

    public StorageAsyncLoader(Context context, UserManager userManager, String str, StorageStatsSource storageStatsSource, PackageManager packageManager) {
        super(context);
        this.mUserManager = userManager;
        this.mUuid = str;
        this.mStatsManager = storageStatsSource;
        this.mPackageManager = packageManager;
    }

    public SparseArray<AppsStorageResult> loadInBackground() {
        return loadApps();
    }

    private SparseArray<AppsStorageResult> loadApps() {
        this.mSeenPackages = new ArraySet<>();
        SparseArray<AppsStorageResult> sparseArray = new SparseArray<>();
        List users = this.mUserManager.getUsers();
        Collections.sort(users, new Comparator<UserInfo>() {
            public int compare(UserInfo userInfo, UserInfo userInfo2) {
                return Integer.compare(userInfo.id, userInfo2.id);
            }
        });
        int size = users.size();
        for (int i = 0; i < size; i++) {
            int i2 = ((UserInfo) users.get(i)).id;
            sparseArray.put(i2, getStorageResultForUser(i2));
        }
        return sparseArray;
    }

    private AppsStorageResult getStorageResultForUser(int i) {
        Log.d("StorageAsyncLoader", "Loading apps");
        List installedApplicationsAsUser = this.mPackageManager.getInstalledApplicationsAsUser(0, i);
        AppsStorageResult appsStorageResult = new AppsStorageResult();
        UserHandle of = UserHandle.of(i);
        int size = installedApplicationsAsUser.size();
        for (int i2 = 0; i2 < size; i2++) {
            ApplicationInfo applicationInfo = (ApplicationInfo) installedApplicationsAsUser.get(i2);
            try {
                StorageStatsSource.AppStorageStats statsForPackage = this.mStatsManager.getStatsForPackage(this.mUuid, applicationInfo.packageName, of);
                long dataBytes = statsForPackage.getDataBytes();
                long cacheQuotaBytes = this.mStatsManager.getCacheQuotaBytes(this.mUuid, applicationInfo.uid);
                long cacheBytes = statsForPackage.getCacheBytes();
                long codeBytes = dataBytes + statsForPackage.getCodeBytes();
                if (cacheQuotaBytes < cacheBytes) {
                    codeBytes = (codeBytes - cacheBytes) + cacheQuotaBytes;
                }
                if (this.mSeenPackages.contains(applicationInfo.packageName)) {
                    appsStorageResult.duplicateCodeSize += statsForPackage.getCodeBytes();
                } else {
                    this.mSeenPackages.add(applicationInfo.packageName);
                }
                int i3 = applicationInfo.category;
                if (i3 == 0) {
                    appsStorageResult.gamesSize += codeBytes;
                } else if (i3 == 1) {
                    long j = appsStorageResult.musicAppsSize + codeBytes;
                    appsStorageResult.musicAppsSize = j;
                    appsStorageResult.musicAppsSize = j - statsForPackage.getCodeBytes();
                    appsStorageResult.otherAppsSize += codeBytes;
                } else if (i3 == 2) {
                    long j2 = appsStorageResult.videoAppsSize + codeBytes;
                    appsStorageResult.videoAppsSize = j2;
                    appsStorageResult.videoAppsSize = j2 - statsForPackage.getCodeBytes();
                    appsStorageResult.otherAppsSize += codeBytes;
                } else if (i3 == 3) {
                    long j3 = appsStorageResult.photosAppsSize + codeBytes;
                    appsStorageResult.photosAppsSize = j3;
                    appsStorageResult.photosAppsSize = j3 - statsForPackage.getCodeBytes();
                    appsStorageResult.otherAppsSize += codeBytes;
                } else if ((applicationInfo.flags & 33554432) != 0) {
                    appsStorageResult.gamesSize += codeBytes;
                } else {
                    appsStorageResult.otherAppsSize += codeBytes;
                }
            } catch (PackageManager.NameNotFoundException | IOException e) {
                Log.w("StorageAsyncLoader", "App unexpectedly not found", e);
            }
        }
        Log.d("StorageAsyncLoader", "Loading external stats");
        try {
            appsStorageResult.externalStats = this.mStatsManager.getExternalStorageStats(this.mUuid, UserHandle.of(i));
        } catch (IOException e2) {
            Log.w("StorageAsyncLoader", e2);
        }
        Log.d("StorageAsyncLoader", "Obtaining result completed");
        return appsStorageResult;
    }
}
