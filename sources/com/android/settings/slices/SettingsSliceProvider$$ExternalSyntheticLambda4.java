package com.android.settings.slices;

public final /* synthetic */ class SettingsSliceProvider$$ExternalSyntheticLambda4 implements Runnable {
    public static final /* synthetic */ SettingsSliceProvider$$ExternalSyntheticLambda4 INSTANCE = new SettingsSliceProvider$$ExternalSyntheticLambda4();

    private /* synthetic */ SettingsSliceProvider$$ExternalSyntheticLambda4() {
    }

    public final void run() {
        SliceBackgroundWorker.shutdown();
    }
}
