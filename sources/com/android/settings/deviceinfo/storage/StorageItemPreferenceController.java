package com.android.settings.deviceinfo.storage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.VolumeInfo;
import android.util.SparseArray;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.applications.manageapplications.ManageApplications;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.deviceinfo.StorageItemPreference;
import com.android.settings.deviceinfo.storage.StorageAsyncLoader;
import com.android.settings.deviceinfo.storage.StorageUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.applications.StorageStatsSource;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.deviceinfo.StorageVolumeProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StorageItemPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    static final String APPS_KEY = "pref_apps";
    static final String AUDIOS_KEY = "pref_audios";
    static final String DOCUMENTS_AND_OTHER_KEY = "pref_documents_and_other";
    static final String GAMES_KEY = "pref_games";
    static final String IMAGES_KEY = "pref_images";
    static final String PUBLIC_STORAGE_KEY = "pref_public_storage";
    static final String SYSTEM_KEY = "pref_system";
    static final String TRASH_KEY = "pref_trash";
    static final String VIDEOS_KEY = "pref_videos";
    StorageItemPreference mAppsPreference;
    StorageItemPreference mAudiosPreference;
    final Uri mAudiosUri;
    StorageItemPreference mDocumentsAndOtherPreference;
    final Uri mDocumentsAndOtherUri;
    private final Fragment mFragment;
    StorageItemPreference mGamesPreference;
    StorageItemPreference mImagesPreference;
    final Uri mImagesUri;
    private boolean mIsWorkProfile;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private PackageManager mPackageManager;
    private List<StorageItemPreference> mPrivateStorageItemPreferences;
    Preference mPublicStoragePreference;
    private PreferenceScreen mScreen;
    private final StorageVolumeProvider mSvp;
    StorageItemPreference mSystemPreference;
    private long mTotalSize;
    StorageItemPreference mTrashPreference;
    private long mUsedBytes;
    private int mUserId = getCurrentUserId();
    private UserManager mUserManager;
    StorageItemPreference mVideosPreference;
    final Uri mVideosUri;
    private VolumeInfo mVolume;

    private long getTrashSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        return 0;
    }

    public String getPreferenceKey() {
        return null;
    }

    public boolean isAvailable() {
        return true;
    }

    public StorageItemPreferenceController(Context context, Fragment fragment, VolumeInfo volumeInfo, StorageVolumeProvider storageVolumeProvider, boolean z) {
        super(context);
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mFragment = fragment;
        this.mVolume = volumeInfo;
        this.mSvp = storageVolumeProvider;
        this.mIsWorkProfile = z;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mImagesUri = Uri.parse(context.getResources().getString(R.string.config_images_storage_category_uri));
        this.mVideosUri = Uri.parse(context.getResources().getString(R.string.config_videos_storage_category_uri));
        this.mAudiosUri = Uri.parse(context.getResources().getString(R.string.config_audios_storage_category_uri));
        this.mDocumentsAndOtherUri = Uri.parse(context.getResources().getString(R.string.config_documents_and_other_storage_category_uri));
    }

    /* access modifiers changed from: package-private */
    public int getCurrentUserId() {
        return Utils.getCurrentUserId(this.mUserManager, this.mIsWorkProfile);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (preference.getKey() == null) {
            return false;
        }
        String key = preference.getKey();
        key.hashCode();
        char c = 65535;
        switch (key.hashCode()) {
            case -1641885275:
                if (key.equals(GAMES_KEY)) {
                    c = 0;
                    break;
                }
                break;
            case -1629384164:
                if (key.equals(TRASH_KEY)) {
                    c = 1;
                    break;
                }
                break;
            case -1300054258:
                if (key.equals(APPS_KEY)) {
                    c = 2;
                    break;
                }
                break;
            case -917633983:
                if (key.equals(PUBLIC_STORAGE_KEY)) {
                    c = 3;
                    break;
                }
                break;
            case 487595257:
                if (key.equals(AUDIOS_KEY)) {
                    c = 4;
                    break;
                }
                break;
            case 709148692:
                if (key.equals(IMAGES_KEY)) {
                    c = 5;
                    break;
                }
                break;
            case 930440645:
                if (key.equals(DOCUMENTS_AND_OTHER_KEY)) {
                    c = 6;
                    break;
                }
                break;
            case 1007071179:
                if (key.equals(SYSTEM_KEY)) {
                    c = 7;
                    break;
                }
                break;
            case 1077721332:
                if (key.equals(VIDEOS_KEY)) {
                    c = 8;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                launchGamesIntent();
                return true;
            case 1:
                launchTrashIntent();
                return true;
            case 2:
                launchAppsIntent();
                return true;
            case 3:
                launchPublicStorageIntent();
                return true;
            case 4:
                launchActivityWithUri(this.mAudiosUri);
                return true;
            case 5:
                launchActivityWithUri(this.mImagesUri);
                return true;
            case 6:
                launchActivityWithUri(this.mDocumentsAndOtherUri);
                return true;
            case 7:
                StorageUtils.SystemInfoFragment systemInfoFragment = new StorageUtils.SystemInfoFragment();
                systemInfoFragment.setTargetFragment(this.mFragment, 0);
                systemInfoFragment.show(this.mFragment.getFragmentManager(), "SystemInfo");
                return true;
            case 8:
                launchActivityWithUri(this.mVideosUri);
                return true;
            default:
                return super.handlePreferenceTreeClick(preference);
        }
    }

    public void setVolume(VolumeInfo volumeInfo) {
        this.mVolume = volumeInfo;
        updateCategoryPreferencesVisibility();
        updatePrivateStorageCategoryPreferencesOrder();
    }

    private boolean isValidPrivateVolume() {
        VolumeInfo volumeInfo = this.mVolume;
        if (volumeInfo != null && volumeInfo.getType() == 1 && (this.mVolume.getState() == 2 || this.mVolume.getState() == 3)) {
            return true;
        }
        return false;
    }

    private boolean isValidPublicVolume() {
        VolumeInfo volumeInfo = this.mVolume;
        return volumeInfo != null && (volumeInfo.getType() == 0 || this.mVolume.getType() == 5) && (this.mVolume.getState() == 2 || this.mVolume.getState() == 3);
    }

    private void updateCategoryPreferencesVisibility() {
        if (this.mScreen != null) {
            this.mPublicStoragePreference.setVisible(isValidPublicVolume());
            boolean isValidPrivateVolume = isValidPrivateVolume();
            this.mImagesPreference.setVisible(isValidPrivateVolume);
            this.mVideosPreference.setVisible(isValidPrivateVolume);
            this.mAudiosPreference.setVisible(isValidPrivateVolume);
            this.mAppsPreference.setVisible(isValidPrivateVolume);
            this.mGamesPreference.setVisible(isValidPrivateVolume);
            this.mDocumentsAndOtherPreference.setVisible(isValidPrivateVolume);
            this.mSystemPreference.setVisible(isValidPrivateVolume);
            this.mTrashPreference.setVisible(false);
            if (isValidPrivateVolume) {
                VolumeInfo findEmulatedForPrivate = this.mSvp.findEmulatedForPrivate(this.mVolume);
                if (findEmulatedForPrivate == null || !findEmulatedForPrivate.isMountedReadable()) {
                    this.mDocumentsAndOtherPreference.setVisible(false);
                }
            }
        }
    }

    private void updatePrivateStorageCategoryPreferencesOrder() {
        if (this.mScreen != null && isValidPrivateVolume()) {
            if (this.mPrivateStorageItemPreferences == null) {
                ArrayList arrayList = new ArrayList();
                this.mPrivateStorageItemPreferences = arrayList;
                arrayList.add(this.mImagesPreference);
                this.mPrivateStorageItemPreferences.add(this.mVideosPreference);
                this.mPrivateStorageItemPreferences.add(this.mAudiosPreference);
                this.mPrivateStorageItemPreferences.add(this.mAppsPreference);
                this.mPrivateStorageItemPreferences.add(this.mGamesPreference);
                this.mPrivateStorageItemPreferences.add(this.mDocumentsAndOtherPreference);
                this.mPrivateStorageItemPreferences.add(this.mSystemPreference);
                this.mPrivateStorageItemPreferences.add(this.mTrashPreference);
            }
            this.mScreen.removePreference(this.mImagesPreference);
            this.mScreen.removePreference(this.mVideosPreference);
            this.mScreen.removePreference(this.mAudiosPreference);
            this.mScreen.removePreference(this.mAppsPreference);
            this.mScreen.removePreference(this.mGamesPreference);
            this.mScreen.removePreference(this.mDocumentsAndOtherPreference);
            this.mScreen.removePreference(this.mSystemPreference);
            this.mScreen.removePreference(this.mTrashPreference);
            Collections.sort(this.mPrivateStorageItemPreferences, Comparator.comparingLong(StorageItemPreferenceController$$ExternalSyntheticLambda0.INSTANCE));
            int i = 200;
            for (StorageItemPreference next : this.mPrivateStorageItemPreferences) {
                next.setOrder(i);
                this.mScreen.addPreference(next);
                i--;
            }
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mScreen = preferenceScreen;
        this.mPublicStoragePreference = preferenceScreen.findPreference(PUBLIC_STORAGE_KEY);
        this.mImagesPreference = (StorageItemPreference) preferenceScreen.findPreference(IMAGES_KEY);
        this.mVideosPreference = (StorageItemPreference) preferenceScreen.findPreference(VIDEOS_KEY);
        this.mAudiosPreference = (StorageItemPreference) preferenceScreen.findPreference(AUDIOS_KEY);
        this.mAppsPreference = (StorageItemPreference) preferenceScreen.findPreference(APPS_KEY);
        this.mGamesPreference = (StorageItemPreference) preferenceScreen.findPreference(GAMES_KEY);
        this.mDocumentsAndOtherPreference = (StorageItemPreference) preferenceScreen.findPreference(DOCUMENTS_AND_OTHER_KEY);
        this.mSystemPreference = (StorageItemPreference) preferenceScreen.findPreference(SYSTEM_KEY);
        this.mTrashPreference = (StorageItemPreference) preferenceScreen.findPreference(TRASH_KEY);
        updateCategoryPreferencesVisibility();
        updatePrivateStorageCategoryPreferencesOrder();
    }

    public void onLoadFinished(SparseArray<StorageAsyncLoader.AppsStorageResult> sparseArray, int i) {
        StorageAsyncLoader.AppsStorageResult appsStorageResult = sparseArray.get(i);
        this.mImagesPreference.setStorageSize(getImagesSize(appsStorageResult), this.mTotalSize);
        this.mVideosPreference.setStorageSize(getVideosSize(appsStorageResult), this.mTotalSize);
        this.mAudiosPreference.setStorageSize(getAudiosSize(appsStorageResult), this.mTotalSize);
        this.mAppsPreference.setStorageSize(getAppsSize(appsStorageResult), this.mTotalSize);
        this.mGamesPreference.setStorageSize(getGamesSize(appsStorageResult), this.mTotalSize);
        this.mDocumentsAndOtherPreference.setStorageSize(getDocumentsAndOtherSize(appsStorageResult), this.mTotalSize);
        this.mTrashPreference.setStorageSize(getTrashSize(appsStorageResult), this.mTotalSize);
        if (this.mSystemPreference != null) {
            long j = 0;
            for (int i2 = 0; i2 < sparseArray.size(); i2++) {
                StorageAsyncLoader.AppsStorageResult valueAt = sparseArray.valueAt(i2);
                long j2 = j + valueAt.gamesSize + valueAt.musicAppsSize + valueAt.videoAppsSize + valueAt.photosAppsSize + valueAt.otherAppsSize;
                StorageStatsSource.ExternalStorageStats externalStorageStats = valueAt.externalStats;
                j = (j2 + (externalStorageStats.totalBytes - externalStorageStats.appBytes)) - valueAt.duplicateCodeSize;
            }
            this.mSystemPreference.setStorageSize(Math.max(1073741824, this.mUsedBytes - j), this.mTotalSize);
        }
        updatePrivateStorageCategoryPreferencesOrder();
    }

    public void setUsedSize(long j) {
        this.mUsedBytes = j;
    }

    public void setTotalSize(long j) {
        this.mTotalSize = j;
    }

    private void launchPublicStorageIntent() {
        Intent buildBrowseIntent = this.mVolume.buildBrowseIntent();
        if (buildBrowseIntent != null) {
            this.mContext.startActivityAsUser(buildBrowseIntent, new UserHandle(this.mUserId));
        }
    }

    private void launchActivityWithUri(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(uri);
        this.mContext.startActivityAsUser(intent, new UserHandle(this.mUserId));
    }

    private long getImagesSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        long j = appsStorageResult.photosAppsSize;
        StorageStatsSource.ExternalStorageStats externalStorageStats = appsStorageResult.externalStats;
        return j + externalStorageStats.imageBytes + externalStorageStats.videoBytes;
    }

    private long getVideosSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        return appsStorageResult.videoAppsSize;
    }

    private long getAudiosSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        return appsStorageResult.musicAppsSize + appsStorageResult.externalStats.audioBytes;
    }

    private void launchAppsIntent() {
        Bundle workAnnotatedBundle = getWorkAnnotatedBundle(3);
        workAnnotatedBundle.putString("classname", Settings.StorageUseActivity.class.getName());
        workAnnotatedBundle.putString("volumeUuid", this.mVolume.getFsUuid());
        workAnnotatedBundle.putString("volumeName", this.mVolume.getDescription());
        Intent intent = new SubSettingLauncher(this.mContext).setDestination(ManageApplications.class.getName()).setTitleRes(R.string.apps_storage).setArguments(workAnnotatedBundle).setSourceMetricsCategory(this.mMetricsFeatureProvider.getMetricsCategory(this.mFragment)).toIntent();
        intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
        Utils.launchIntent(this.mFragment, intent);
    }

    private long getAppsSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        return appsStorageResult.otherAppsSize;
    }

    private void launchGamesIntent() {
        Bundle workAnnotatedBundle = getWorkAnnotatedBundle(1);
        workAnnotatedBundle.putString("classname", Settings.GamesStorageActivity.class.getName());
        Intent intent = new SubSettingLauncher(this.mContext).setDestination(ManageApplications.class.getName()).setTitleRes(R.string.game_storage_settings).setArguments(workAnnotatedBundle).setSourceMetricsCategory(this.mMetricsFeatureProvider.getMetricsCategory(this.mFragment)).toIntent();
        intent.putExtra("android.intent.extra.USER_ID", this.mUserId);
        Utils.launchIntent(this.mFragment, intent);
    }

    private long getGamesSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        return appsStorageResult.gamesSize;
    }

    private Bundle getWorkAnnotatedBundle(int i) {
        Bundle bundle = new Bundle(i + 1);
        bundle.putInt(":settings:show_fragment_tab", this.mIsWorkProfile ? 1 : 0);
        return bundle;
    }

    private long getDocumentsAndOtherSize(StorageAsyncLoader.AppsStorageResult appsStorageResult) {
        StorageStatsSource.ExternalStorageStats externalStorageStats = appsStorageResult.externalStats;
        return (((externalStorageStats.totalBytes - externalStorageStats.audioBytes) - externalStorageStats.videoBytes) - externalStorageStats.imageBytes) - externalStorageStats.appBytes;
    }

    private void launchTrashIntent() {
        Intent intent = new Intent("android.settings.VIEW_TRASH");
        if (intent.resolveActivity(this.mPackageManager) == null) {
            EmptyTrashFragment.show(this.mFragment);
        } else {
            this.mContext.startActivityAsUser(intent, new UserHandle(this.mUserId));
        }
    }
}
