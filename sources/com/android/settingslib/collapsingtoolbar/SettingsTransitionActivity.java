package com.android.settingslib.collapsingtoolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.core.p002os.BuildCompat;
import androidx.fragment.app.FragmentActivity;
import com.android.settingslib.transition.SettingsTransitionHelper;

public abstract class SettingsTransitionActivity extends FragmentActivity {
    public abstract Toolbar getToolbar();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (BuildCompat.isAtLeastS()) {
            getWindow().requestFeature(13);
            SettingsTransitionHelper.applyForwardTransition(this);
            SettingsTransitionHelper.applyBackwardTransition(this);
        }
        super.onCreate(bundle);
    }

    public void startActivity(Intent intent) {
        if (!BuildCompat.isAtLeastS()) {
            super.startActivity(intent);
            return;
        }
        Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w("SettingsTransitionActivity", "Toolbar is null. Cannot apply settings transition!");
            super.startActivity(intent);
            return;
        }
        super.startActivity(intent, getActivityOptionsBundle(toolbar));
    }

    public void startActivity(Intent intent, Bundle bundle) {
        if (!BuildCompat.isAtLeastS()) {
            super.startActivity(intent, bundle);
            return;
        }
        Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w("SettingsTransitionActivity", "Toolbar is null. Cannot apply settings transition!");
            super.startActivity(intent, bundle);
        } else if (bundle != null) {
            super.startActivity(intent, getMergedBundleForTransition(bundle));
        } else {
            super.startActivity(intent, getActivityOptionsBundle(toolbar));
        }
    }

    public void startActivityForResult(Intent intent, int i) {
        if (!BuildCompat.isAtLeastS() || i == -1) {
            super.startActivityForResult(intent, i);
            return;
        }
        Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w("SettingsTransitionActivity", "Toolbar is null. Cannot apply settings transition!");
            super.startActivityForResult(intent, i);
            return;
        }
        super.startActivityForResult(intent, i, getActivityOptionsBundle(toolbar));
    }

    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        if (!BuildCompat.isAtLeastS() || i == -1) {
            super.startActivityForResult(intent, i, bundle);
            return;
        }
        Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w("SettingsTransitionActivity", "Toolbar is null. Cannot apply settings transition!");
            super.startActivityForResult(intent, i, bundle);
        } else if (bundle != null) {
            super.startActivityForResult(intent, i, getMergedBundleForTransition(bundle));
        } else {
            super.startActivityForResult(intent, i, getActivityOptionsBundle(toolbar));
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    private Bundle getActivityOptionsBundle(Toolbar toolbar) {
        return ActivityOptions.makeSceneTransitionAnimation(this, toolbar, "shared_element_view").toBundle();
    }

    private Bundle getMergedBundleForTransition(Bundle bundle) {
        Toolbar toolbar = getToolbar();
        Bundle bundle2 = new Bundle();
        bundle2.putAll(bundle);
        Bundle activityOptionsBundle = getActivityOptionsBundle(toolbar);
        if (activityOptionsBundle != null) {
            bundle2.putAll(activityOptionsBundle);
        }
        return bundle2;
    }
}
