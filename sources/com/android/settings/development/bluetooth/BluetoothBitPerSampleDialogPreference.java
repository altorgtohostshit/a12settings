package com.android.settings.development.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import com.android.settings.R;

public class BluetoothBitPerSampleDialogPreference extends BaseBluetoothDialogPreference {
    /* access modifiers changed from: protected */
    public int getRadioButtonGroupId() {
        return R.id.bluetooth_audio_bit_per_sample_radio_group;
    }

    public BluetoothBitPerSampleDialogPreference(Context context) {
        super(context);
        initialize(context);
    }

    public BluetoothBitPerSampleDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public BluetoothBitPerSampleDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }

    public BluetoothBitPerSampleDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_bit_per_sample_default));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_bit_per_sample_16));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_bit_per_sample_24));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_audio_bit_per_sample_32));
        String[] stringArray = context.getResources().getStringArray(R.array.bluetooth_a2dp_codec_bits_per_sample_titles);
        for (String add : stringArray) {
            this.mRadioButtonStrings.add(add);
        }
        String[] stringArray2 = context.getResources().getStringArray(R.array.bluetooth_a2dp_codec_bits_per_sample_summaries);
        for (String add2 : stringArray2) {
            this.mSummaryStrings.add(add2);
        }
    }
}
