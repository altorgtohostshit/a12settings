package com.android.settings.widget;

import android.media.MediaPlayer;

public final /* synthetic */ class MediaAnimationController$$ExternalSyntheticLambda0 implements MediaPlayer.OnPreparedListener {
    public static final /* synthetic */ MediaAnimationController$$ExternalSyntheticLambda0 INSTANCE = new MediaAnimationController$$ExternalSyntheticLambda0();

    private /* synthetic */ MediaAnimationController$$ExternalSyntheticLambda0() {
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
    }
}
