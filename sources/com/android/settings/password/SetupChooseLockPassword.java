package com.android.settings.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.SetupRedactionInterstitial;
import com.android.settings.password.ChooseLockGenericController;
import com.android.settings.password.ChooseLockPassword;
import com.android.settings.password.ChooseLockTypeDialogFragment;

public class SetupChooseLockPassword extends ChooseLockPassword {
    public static Intent modifyIntentForSetup(Context context, Intent intent) {
        intent.setClass(context, SetupChooseLockPassword.class);
        intent.putExtra("extra_prefs_show_button_bar", false);
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return SetupChooseLockPasswordFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: package-private */
    public Class<? extends Fragment> getFragmentClass() {
        return SetupChooseLockPasswordFragment.class;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        findViewById(R.id.content_parent).setFitsSystemWindows(false);
    }

    public static class SetupChooseLockPasswordFragment extends ChooseLockPassword.ChooseLockPasswordFragment implements ChooseLockTypeDialogFragment.OnLockTypeSelectedListener {
        private boolean mLeftButtonIsSkip;
        private Button mOptionsButton;

        /* access modifiers changed from: protected */
        public int getStageType() {
            return 0;
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            FragmentActivity activity = getActivity();
            boolean z = true;
            if (new ChooseLockGenericController.Builder(activity, this.mUserId).setHideInsecureScreenLockTypes(true).build().getVisibleAndEnabledScreenLockTypes().size() <= 0) {
                z = false;
            }
            boolean booleanExtra = activity.getIntent().getBooleanExtra("show_options_button", false);
            if (!z) {
                Log.w("SetupChooseLockPassword", "Visible screen lock types is empty!");
            }
            if (booleanExtra && z) {
                Button button = (Button) view.findViewById(R.id.screen_lock_options);
                this.mOptionsButton = button;
                button.setVisibility(0);
                this.mOptionsButton.setOnClickListener(new C1224x8037cc4f(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onViewCreated$0(View view) {
            ChooseLockTypeDialogFragment.newInstance(this.mUserId).show(getChildFragmentManager(), "skip_screen_lock_dialog");
        }

        /* access modifiers changed from: protected */
        public void onSkipOrClearButtonClick(View view) {
            if (this.mLeftButtonIsSkip) {
                Intent intent = getActivity().getIntent();
                SetupSkipDialog.newInstance(intent.getBooleanExtra(":settings:frp_supported", false), false, this.mIsAlphaMode, intent.getBooleanExtra("for_fingerprint", false), intent.getBooleanExtra("for_face", false), intent.getBooleanExtra("for_biometrics", false)).show(getFragmentManager());
                return;
            }
            super.onSkipOrClearButtonClick(view);
        }

        /* access modifiers changed from: protected */
        public Intent getRedactionInterstitialIntent(Context context) {
            SetupRedactionInterstitial.setEnabled(context, true);
            return null;
        }

        public void onLockTypeSelected(ScreenLockType screenLockType) {
            if (screenLockType != (this.mIsAlphaMode ? ScreenLockType.PASSWORD : ScreenLockType.PIN)) {
                startChooseLockActivity(screenLockType, getActivity());
            }
        }

        /* access modifiers changed from: protected */
        public void updateUi() {
            super.updateUi();
            ChooseLockPassword.ChooseLockPasswordFragment.Stage stage = this.mUiStage;
            ChooseLockPassword.ChooseLockPasswordFragment.Stage stage2 = ChooseLockPassword.ChooseLockPasswordFragment.Stage.Introduction;
            int i = 0;
            if (stage == stage2) {
                this.mSkipOrClearButton.setText(getActivity(), R.string.skip_label);
                this.mLeftButtonIsSkip = true;
            } else {
                this.mSkipOrClearButton.setText(getActivity(), R.string.lockpassword_clear_label);
                this.mLeftButtonIsSkip = false;
            }
            Button button = this.mOptionsButton;
            if (button != null) {
                if (this.mUiStage != stage2) {
                    i = 8;
                }
                button.setVisibility(i);
            }
        }
    }
}
