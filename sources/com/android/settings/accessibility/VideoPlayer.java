package com.android.settings.accessibility;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;

public class VideoPlayer implements TextureView.SurfaceTextureListener {
    Surface mAnimationSurface;
    private final Context mContext;
    MediaPlayer mMediaPlayer;
    private final Object mMediaPlayerLock = new Object();
    State mMediaPlayerState = State.NONE;
    private final int mVideoRes;

    public enum State {
        NONE,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        END
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public static VideoPlayer create(Context context, int i, TextureView textureView) {
        return new VideoPlayer(context, i, textureView);
    }

    private VideoPlayer(Context context, int i, TextureView textureView) {
        this.mContext = context;
        this.mVideoRes = i;
        textureView.setSurfaceTextureListener(this);
    }

    public void release() {
        State state;
        synchronized (this.mMediaPlayerLock) {
            State state2 = this.mMediaPlayerState;
            if (!(state2 == State.NONE || state2 == (state = State.END))) {
                this.mMediaPlayerState = state;
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            }
        }
        Surface surface = this.mAnimationSurface;
        if (surface != null) {
            surface.release();
            this.mAnimationSurface = null;
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.mAnimationSurface = new Surface(surfaceTexture);
        synchronized (this.mMediaPlayerLock) {
            MediaPlayer create = MediaPlayer.create(this.mContext, this.mVideoRes);
            this.mMediaPlayer = create;
            this.mMediaPlayerState = State.PREPARED;
            create.setSurface(this.mAnimationSurface);
            this.mMediaPlayer.setLooping(true);
            this.mMediaPlayer.start();
            this.mMediaPlayerState = State.STARTED;
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        release();
        return false;
    }
}
