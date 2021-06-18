package com.android.settings;

import android.app.Activity;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import java.util.Objects;

public class FallbackHome extends Activity {
    /* access modifiers changed from: private */
    public final WallpaperManager.OnColorsChangedListener mColorsChangedListener = new WallpaperManager.OnColorsChangedListener() {
        public void onColorsChanged(WallpaperColors wallpaperColors, int i) {
            if (wallpaperColors != null) {
                View decorView = FallbackHome.this.getWindow().getDecorView();
                decorView.setSystemUiVisibility(FallbackHome.this.updateVisibilityFlagsFromColors(wallpaperColors, decorView.getSystemUiVisibility()));
                FallbackHome.this.mWallManager.removeOnColorsChangedListener(this);
            }
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            FallbackHome.this.maybeFinish();
        }
    };
    private final Runnable mProgressTimeoutRunnable = new FallbackHome$$ExternalSyntheticLambda0(this);
    private boolean mProvisioned;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            FallbackHome.this.maybeFinish();
        }
    };
    /* access modifiers changed from: private */
    public WallpaperManager mWallManager;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        View inflate = getLayoutInflater().inflate(R.layout.fallback_home_finishing_boot, (ViewGroup) null);
        setContentView(inflate);
        inflate.setAlpha(0.0f);
        inflate.animate().alpha(1.0f).setDuration(500).setInterpolator(AnimationUtils.loadInterpolator(this, 17563661)).start();
        getWindow().addFlags(128);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        boolean z = false;
        if (Settings.Global.getInt(getContentResolver(), "device_provisioned", 0) != 0) {
            z = true;
        }
        this.mProvisioned = z;
        if (!z) {
            setTheme(2131951911);
            i = 4102;
        } else {
            i = 1536;
        }
        WallpaperManager wallpaperManager = (WallpaperManager) getSystemService(WallpaperManager.class);
        this.mWallManager = wallpaperManager;
        if (wallpaperManager == null) {
            Log.w("FallbackHome", "Wallpaper manager isn't ready, can't listen to color changes!");
        } else {
            loadWallpaperColors(i);
        }
        getWindow().getDecorView().setSystemUiVisibility(i);
        registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
        maybeFinish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mProvisioned) {
            this.mHandler.postDelayed(this.mProgressTimeoutRunnable, 2000);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacks(this.mProgressTimeoutRunnable);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
        WallpaperManager wallpaperManager = this.mWallManager;
        if (wallpaperManager != null) {
            wallpaperManager.removeOnColorsChangedListener(this.mColorsChangedListener);
        }
    }

    private void loadWallpaperColors(final int i) {
        new AsyncTask<Object, Void, Integer>() {
            /* access modifiers changed from: protected */
            public Integer doInBackground(Object... objArr) {
                WallpaperColors wallpaperColors = FallbackHome.this.mWallManager.getWallpaperColors(1);
                if (wallpaperColors != null) {
                    return Integer.valueOf(FallbackHome.this.updateVisibilityFlagsFromColors(wallpaperColors, i));
                }
                FallbackHome.this.mWallManager.addOnColorsChangedListener(FallbackHome.this.mColorsChangedListener, (Handler) null);
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Integer num) {
                if (num != null) {
                    FallbackHome.this.getWindow().getDecorView().setSystemUiVisibility(num.intValue());
                }
            }
        }.execute(new Object[0]);
    }

    /* access modifiers changed from: private */
    public void maybeFinish() {
        if (((UserManager) getSystemService(UserManager.class)).isUserUnlocked()) {
            if (Objects.equals(getPackageName(), getPackageManager().resolveActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 0).activityInfo.packageName)) {
                Log.d("FallbackHome", "User unlocked but no home; let's hope someone enables one soon?");
                this.mHandler.sendEmptyMessageDelayed(0, 500);
                return;
            }
            Log.d("FallbackHome", "User unlocked and real home found; let's go!");
            ((PowerManager) getSystemService(PowerManager.class)).userActivity(SystemClock.uptimeMillis(), false);
            finish();
        }
    }

    /* access modifiers changed from: private */
    public int updateVisibilityFlagsFromColors(WallpaperColors wallpaperColors, int i) {
        return (wallpaperColors.getColorHints() & 1) != 0 ? i | 8192 | 16 : i & -8193 & -17;
    }
}
