package com.android.settings.media;

import android.content.ComponentName;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

public class MediaOutputUtils {
    private static final boolean DEBUG = Log.isLoggable("MediaOutputUtils", 3);

    public static MediaController getActiveLocalMediaController(MediaSessionManager mediaSessionManager) {
        PlaybackState playbackState;
        ArrayList arrayList = new ArrayList();
        MediaController mediaController = null;
        for (MediaController next : mediaSessionManager.getActiveSessions((ComponentName) null)) {
            MediaController.PlaybackInfo playbackInfo = next.getPlaybackInfo();
            if (!(playbackInfo == null || (playbackState = next.getPlaybackState()) == null)) {
                if (DEBUG) {
                    Log.d("MediaOutputUtils", "getActiveLocalMediaController() package name : " + next.getPackageName() + ", play back type : " + playbackInfo.getPlaybackType() + ", play back state : " + playbackState.getState());
                }
                if (playbackState.getState() == 3) {
                    if (playbackInfo.getPlaybackType() == 2) {
                        if (mediaController != null && TextUtils.equals(mediaController.getPackageName(), next.getPackageName())) {
                            mediaController = null;
                        }
                        if (!arrayList.contains(next.getPackageName())) {
                            arrayList.add(next.getPackageName());
                        }
                    } else if (playbackInfo.getPlaybackType() == 1 && mediaController == null && !arrayList.contains(next.getPackageName())) {
                        mediaController = next;
                    }
                }
            }
        }
        return mediaController;
    }
}
