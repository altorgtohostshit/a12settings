package com.android.settings.development.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import com.android.settings.R;

public class BluetoothCodecDialogPreference extends BaseBluetoothDialogPreference {
    /* access modifiers changed from: protected */
    public int getRadioButtonGroupId() {
        return R.id.bluetooth_audio_codec_radio_group;
    }

    public BluetoothCodecDialogPreference(Context context) {
        super(context);
        initialize(context);
    }

    public BluetoothCodecDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public BluetoothCodecDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }

    public BluetoothCodecDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_codec_default));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_codec_sbc));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_codec_aac));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_codec_aptx));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_codec_aptx_hd));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_codec_ldac));
        String[] stringArray = context.getResources().getStringArray(R.array.bluetooth_a2dp_codec_titles);
        for (String add : stringArray) {
            this.mRadioButtonStrings.add(add);
        }
        String[] stringArray2 = context.getResources().getStringArray(R.array.bluetooth_a2dp_codec_summaries);
        for (String add2 : stringArray2) {
            this.mSummaryStrings.add(add2);
        }
    }
}
