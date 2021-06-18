package com.android.settings.applications.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.applications.assist.VoiceInputHelper;
import com.android.settings.applications.defaultapps.DefaultAppPickerFragment;
import com.android.settingslib.applications.DefaultAppInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultVoiceInputPicker extends DefaultAppPickerFragment {
    private VoiceInputHelper mHelper;

    public int getMetricsCategory() {
        return 844;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.default_voice_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        VoiceInputHelper voiceInputHelper = new VoiceInputHelper(context);
        this.mHelper = voiceInputHelper;
        voiceInputHelper.buildUi();
    }

    /* access modifiers changed from: protected */
    public List<VoiceInputDefaultAppInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        Context context = getContext();
        Iterator<VoiceInputHelper.RecognizerInfo> it = this.mHelper.mAvailableRecognizerInfos.iterator();
        while (it.hasNext()) {
            Context context2 = context;
            arrayList.add(new VoiceInputDefaultAppInfo(context2, this.mPm, this.mUserId, it.next(), true));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        ComponentName currentService = getCurrentService(this.mHelper);
        if (currentService == null) {
            return null;
        }
        return currentService.flattenToShortString();
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        Iterator<VoiceInputHelper.RecognizerInfo> it = this.mHelper.mAvailableRecognizerInfos.iterator();
        while (true) {
            if (it.hasNext()) {
                if (TextUtils.equals(str, it.next().key)) {
                    Settings.Secure.putString(getContext().getContentResolver(), "voice_recognition_service", str);
                    break;
                }
            } else {
                break;
            }
        }
        return true;
    }

    public static ComponentName getCurrentService(VoiceInputHelper voiceInputHelper) {
        return voiceInputHelper.mCurrentRecognizer;
    }

    public static class VoiceInputDefaultAppInfo extends DefaultAppInfo {
        public VoiceInputHelper.BaseInfo mInfo;

        public VoiceInputDefaultAppInfo(Context context, PackageManager packageManager, int i, VoiceInputHelper.BaseInfo baseInfo, boolean z) {
            super(context, packageManager, i, baseInfo.componentName, (String) null, z);
            this.mInfo = baseInfo;
        }

        public String getKey() {
            return this.mInfo.key;
        }

        public CharSequence loadLabel() {
            return this.mInfo.label;
        }

        public Intent getSettingIntent() {
            if (this.mInfo.settings == null) {
                return null;
            }
            return new Intent("android.intent.action.MAIN").setComponent(this.mInfo.settings);
        }
    }
}
