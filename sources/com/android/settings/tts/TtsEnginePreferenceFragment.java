package com.android.settings.tts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TtsEngines;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TtsEnginePreferenceFragment extends RadioButtonPickerFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.tts_engine_picker);
    private Context mContext;
    private Map<String, EngineCandidateInfo> mEngineMap;
    private TtsEngines mEnginesHelper = null;
    private String mPreviousEngine;
    private TextToSpeech mTts = null;
    private final TextToSpeech.OnInitListener mUpdateListener = new TextToSpeech.OnInitListener() {
        public void onInit(int i) {
            TtsEnginePreferenceFragment.this.onUpdateEngine(i);
        }
    };

    public int getMetricsCategory() {
        return 93;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.tts_engine_picker;
    }

    public void onCreate(Bundle bundle) {
        this.mContext = getContext().getApplicationContext();
        this.mEnginesHelper = new TtsEngines(this.mContext);
        this.mEngineMap = new HashMap();
        this.mTts = new TextToSpeech(this.mContext, (TextToSpeech.OnInitListener) null);
        super.onCreate(bundle);
    }

    public void onDestroy() {
        super.onDestroy();
        TextToSpeech textToSpeech = this.mTts;
        if (textToSpeech != null) {
            textToSpeech.shutdown();
            this.mTts = null;
        }
    }

    public void onUpdateEngine(int i) {
        if (i == 0) {
            Log.d("TtsEnginePrefFragment", "Updating engine: Successfully bound to the engine: " + this.mTts.getCurrentEngine());
            Settings.Secure.putString(this.mContext.getContentResolver(), "tts_default_synth", this.mTts.getCurrentEngine());
            return;
        }
        Log.d("TtsEnginePrefFragment", "Updating engine: Failed to bind to engine, reverting.");
        if (this.mPreviousEngine != null) {
            this.mTts = new TextToSpeech(this.mContext, (TextToSpeech.OnInitListener) null, this.mPreviousEngine);
            updateCheckedState(this.mPreviousEngine);
        }
        this.mPreviousEngine = null;
    }

    /* access modifiers changed from: protected */
    public void onRadioButtonConfirmed(String str) {
        EngineCandidateInfo engineCandidateInfo = this.mEngineMap.get(str);
        if (shouldDisplayDataAlert(engineCandidateInfo)) {
            displayDataAlert(engineCandidateInfo, new TtsEnginePreferenceFragment$$ExternalSyntheticLambda0(this, str));
        } else {
            setDefaultKey(str);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onRadioButtonConfirmed$0(String str, DialogInterface dialogInterface, int i) {
        setDefaultKey(str);
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        for (TextToSpeech.EngineInfo engineInfo : this.mEnginesHelper.getEngines()) {
            EngineCandidateInfo engineCandidateInfo = new EngineCandidateInfo(engineInfo);
            arrayList.add(engineCandidateInfo);
            this.mEngineMap.put(engineInfo.name, engineCandidateInfo);
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return this.mEnginesHelper.getDefaultEngine();
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        updateDefaultEngine(str);
        updateCheckedState(str);
        return true;
    }

    private boolean shouldDisplayDataAlert(EngineCandidateInfo engineCandidateInfo) {
        return !engineCandidateInfo.isSystem();
    }

    private void displayDataAlert(EngineCandidateInfo engineCandidateInfo, DialogInterface.OnClickListener onClickListener) {
        Log.i("TtsEnginePrefFragment", "Displaying data alert for :" + engineCandidateInfo.getKey());
        new AlertDialog.Builder(getPrefContext()).setTitle(17039380).setMessage((CharSequence) this.mContext.getString(R.string.tts_engine_security_warning, new Object[]{engineCandidateInfo.loadLabel()})).setCancelable(true).setPositiveButton(17039370, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create().show();
    }

    private void updateDefaultEngine(String str) {
        Log.d("TtsEnginePrefFragment", "Updating default synth to : " + str);
        this.mPreviousEngine = this.mTts.getCurrentEngine();
        Log.i("TtsEnginePrefFragment", "Shutting down current tts engine");
        TextToSpeech textToSpeech = this.mTts;
        if (textToSpeech != null) {
            try {
                textToSpeech.shutdown();
                this.mTts = null;
            } catch (Exception e) {
                Log.e("TtsEnginePrefFragment", "Error shutting down TTS engine" + e);
            }
        }
        Log.i("TtsEnginePrefFragment", "Updating engine : Attempting to connect to engine: " + str);
        this.mTts = new TextToSpeech(this.mContext, this.mUpdateListener, str);
        Log.i("TtsEnginePrefFragment", "Success");
    }

    public static class EngineCandidateInfo extends CandidateInfo {
        private final TextToSpeech.EngineInfo mEngineInfo;

        public Drawable loadIcon() {
            return null;
        }

        EngineCandidateInfo(TextToSpeech.EngineInfo engineInfo) {
            super(true);
            this.mEngineInfo = engineInfo;
        }

        public CharSequence loadLabel() {
            return this.mEngineInfo.label;
        }

        public String getKey() {
            return this.mEngineInfo.name;
        }

        public boolean isSystem() {
            return this.mEngineInfo.system;
        }
    }
}
