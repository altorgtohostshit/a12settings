package com.android.settings.homepage;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.accounts.AvatarViewMixin;
import com.android.settings.homepage.contextualcards.ContextualCardsFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;
import com.android.settingslib.transition.SettingsTransitionHelper;

public class SettingsHomepageActivity extends FragmentActivity {
    private View mHomepageView;
    private View mSuggestionView;

    public void showHomepageWithSuggestion(boolean z) {
        if (this.mHomepageView != null) {
            Log.i("SettingsHomepageActivity", "showHomepageWithSuggestion: " + z);
            this.mSuggestionView.setVisibility(z ? 0 : 8);
            this.mHomepageView.setVisibility(0);
            this.mHomepageView = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (Utils.isPageTransitionEnabled(this)) {
            getWindow().requestFeature(13);
            SettingsTransitionHelper.applyForwardTransition(this);
            SettingsTransitionHelper.applyBackwardTransition(this);
        }
        super.onCreate(bundle);
        setContentView((int) R.layout.settings_homepage_container);
        findViewById(R.id.app_bar_container).setMinimumHeight(getSearchBoxHeight());
        initHomepageContainer();
        FeatureFactory.getFactory(this).getSearchFeatureProvider().initSearchToolbar(this, (Toolbar) findViewById(R.id.search_action_bar), 1502);
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
        if (!((ActivityManager) getSystemService(ActivityManager.class)).isLowRamDevice()) {
            ImageView imageView = (ImageView) findViewById(R.id.account_avatar);
            if (AvatarViewMixin.isAvatarSupported(this)) {
                imageView.setVisibility(0);
                getLifecycle().addObserver(new AvatarViewMixin(this, imageView));
            }
            if (FeatureFlagUtils.isEnabled(this, "settings_silky_home")) {
                showSuggestionFragment();
            } else {
                findViewById(R.id.homepage_title).setVisibility(8);
                imageView.setVisibility(8);
            }
            if (FeatureFlagUtils.isEnabled(this, "settings_contextual_home")) {
                showFragment(new ContextualCardsFragment(), R.id.contextual_cards_content);
            }
        }
        showFragment(new TopLevelSettings(), R.id.main_content);
        ((FrameLayout) findViewById(R.id.main_content)).getLayoutTransition().enableTransitionType(4);
    }

    public void startActivity(Intent intent) {
        if (Utils.isPageTransitionEnabled(this)) {
            super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        } else {
            super.startActivity(intent);
        }
    }

    private void showSuggestionFragment() {
        Class<? extends Fragment> contextualSuggestionFragment = FeatureFactory.getFactory(this).getSuggestionFeatureProvider(this).getContextualSuggestionFragment();
        if (contextualSuggestionFragment != null) {
            this.mSuggestionView = findViewById(R.id.suggestion_content);
            View findViewById = findViewById(R.id.settings_homepage_container);
            this.mHomepageView = findViewById;
            findViewById.setVisibility(8);
            this.mHomepageView.postDelayed(new SettingsHomepageActivity$$ExternalSyntheticLambda0(this), 300);
            try {
                showFragment((Fragment) contextualSuggestionFragment.getConstructor(new Class[0]).newInstance(new Object[0]), R.id.suggestion_content);
            } catch (Exception e) {
                Log.w("SettingsHomepageActivity", "Cannot show fragment", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestionFragment$0() {
        showHomepageWithSuggestion(false);
    }

    private void showFragment(Fragment fragment, int i) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        Fragment findFragmentById = supportFragmentManager.findFragmentById(i);
        if (findFragmentById == null) {
            beginTransaction.add(i, fragment);
        } else {
            beginTransaction.show(findFragmentById);
        }
        beginTransaction.commit();
    }

    private void initHomepageContainer() {
        View findViewById = findViewById(R.id.homepage_container);
        findViewById.setFocusableInTouchMode(true);
        findViewById.requestFocus();
    }

    private int getSearchBoxHeight() {
        return getResources().getDimensionPixelSize(R.dimen.search_bar_height) + (getResources().getDimensionPixelSize(R.dimen.search_bar_margin) * 2);
    }
}
