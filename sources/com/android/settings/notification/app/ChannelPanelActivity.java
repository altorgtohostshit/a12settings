package com.android.settings.notification.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;

public class ChannelPanelActivity extends FragmentActivity {
    final Bundle mBundle = new Bundle();
    NotificationSettings mPanelFragment;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!getIntent().hasExtra("android.provider.extra.CHANNEL_FILTER_LIST")) {
            launchFullSettings();
        }
        getApplicationContext().getTheme().rebase();
        createOrUpdatePanel();
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        createOrUpdatePanel();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    private void launchFullSettings() {
        Bundle extras = getIntent().getExtras();
        extras.remove("android.provider.extra.CHANNEL_FILTER_LIST");
        startActivity(new SubSettingLauncher(this).setDestination(ChannelNotificationSettings.class.getName()).setExtras(extras).setSourceMetricsCategory(265).toIntent());
        finish();
    }

    private void createOrUpdatePanel() {
        NotificationSettings notificationSettings;
        Intent intent = getIntent();
        if (intent == null) {
            Log.e("ChannelPanelActivity", "Null intent, closing Panel Activity");
            finish();
            return;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        setContentView((int) R.layout.notification_channel_panel);
        Window window = getWindow();
        window.setGravity(80);
        window.setLayout(-1, -2);
        findViewById(R.id.done).setOnClickListener(new ChannelPanelActivity$$ExternalSyntheticLambda1(this));
        findViewById(R.id.see_more).setOnClickListener(new ChannelPanelActivity$$ExternalSyntheticLambda0(this));
        if (intent.hasExtra("android.provider.extra.CONVERSATION_ID")) {
            notificationSettings = new ConversationNotificationSettings();
        } else {
            notificationSettings = new ChannelNotificationSettings();
        }
        this.mPanelFragment = notificationSettings;
        notificationSettings.setArguments(new Bundle(this.mBundle));
        supportFragmentManager.beginTransaction().replace(16908351, this.mPanelFragment).commit();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createOrUpdatePanel$0(View view) {
        finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createOrUpdatePanel$1(View view) {
        launchFullSettings();
    }
}
