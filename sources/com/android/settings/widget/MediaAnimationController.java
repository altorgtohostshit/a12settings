package com.android.settings.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.android.settings.widget.VideoPreference;

class MediaAnimationController implements VideoPreference.AnimationController {
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public Surface mSurface;
    /* access modifiers changed from: private */
    public boolean mVideoReady;

    MediaAnimationController(Context context, int i) {
        MediaPlayer create = MediaPlayer.create(context, new Uri.Builder().scheme("android.resource").authority(context.getPackageName()).appendPath(String.valueOf(i)).build());
        this.mMediaPlayer = create;
        if (create != null) {
            create.seekTo(0);
            this.mMediaPlayer.setOnSeekCompleteListener(new MediaAnimationController$$ExternalSyntheticLambda1(this));
            this.mMediaPlayer.setOnPreparedListener(MediaAnimationController$$ExternalSyntheticLambda0.INSTANCE);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(MediaPlayer mediaPlayer) {
        this.mVideoReady = true;
    }

    public int getVideoWidth() {
        return this.mMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        return this.mMediaPlayer.getVideoHeight();
    }

    public int getDuration() {
        return this.mMediaPlayer.getDuration();
    }

    public void attachView(TextureView textureView, final View view, final View view2) {
        updateViewStates(view, view2);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                MediaAnimationController.this.setSurface(surfaceTexture);
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                view.setVisibility(0);
                Surface unused = MediaAnimationController.this.mSurface = null;
                return false;
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                MediaAnimationController.this.setSurface(surfaceTexture);
                if (MediaAnimationController.this.mVideoReady) {
                    if (view.getVisibility() == 0) {
                        view.setVisibility(8);
                    }
                    if (MediaAnimationController.this.mMediaPlayer != null && !MediaAnimationController.this.mMediaPlayer.isPlaying()) {
                        MediaAnimationController.this.mMediaPlayer.start();
                        view2.setVisibility(8);
                    }
                }
                if (MediaAnimationController.this.mMediaPlayer != null && !MediaAnimationController.this.mMediaPlayer.isPlaying() && view2.getVisibility() != 0) {
                    view2.setVisibility(0);
                }
            }
        });
        textureView.setOnClickListener(new MediaAnimationController$$ExternalSyntheticLambda2(this, view, view2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attachView$2(View view, View view2, View view3) {
        updateViewStates(view, view2);
    }

    public void release() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mVideoReady = false;
        }
    }

    private void updateViewStates(View view, View view2) {
        if (this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            view2.setVisibility(0);
            view.setVisibility(0);
            return;
        }
        view.setVisibility(8);
        view2.setVisibility(8);
        this.mMediaPlayer.start();
    }

    /* access modifiers changed from: private */
    public void setSurface(SurfaceTexture surfaceTexture) {
        if (this.mMediaPlayer != null && this.mSurface == null) {
            Surface surface = new Surface(surfaceTexture);
            this.mSurface = surface;
            this.mMediaPlayer.setSurface(surface);
        }
    }
}
