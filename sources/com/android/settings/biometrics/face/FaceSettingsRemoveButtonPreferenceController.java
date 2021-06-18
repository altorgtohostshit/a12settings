package com.android.settings.biometrics.face;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.hardware.face.Face;
import android.hardware.face.FaceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;

public class FaceSettingsRemoveButtonPreferenceController extends BasePreferenceController implements View.OnClickListener {
    static final String KEY = "security_settings_face_delete_faces_container";
    private static final String TAG = "FaceSettings/Remove";
    private SettingsActivity mActivity;
    /* access modifiers changed from: private */
    public Button mButton;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final FaceManager mFaceManager;
    /* access modifiers changed from: private */
    public Listener mListener;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final DialogInterface.OnClickListener mOnClickListener;
    private Preference mPreference;
    /* access modifiers changed from: private */
    public final FaceManager.RemovalCallback mRemovalCallback;
    /* access modifiers changed from: private */
    public boolean mRemoving;
    /* access modifiers changed from: private */
    public int mUserId;

    interface Listener {
        void onRemoved();
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public static class ConfirmRemoveDialog extends InstrumentedDialogFragment {
        private DialogInterface.OnClickListener mOnClickListener;

        public int getMetricsCategory() {
            return 1693;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.security_settings_face_settings_remove_dialog_title).setMessage(R.string.security_settings_face_settings_remove_dialog_details).setPositiveButton(R.string.delete, this.mOnClickListener).setNegativeButton(R.string.cancel, this.mOnClickListener);
            AlertDialog create = builder.create();
            create.setCanceledOnTouchOutside(false);
            return create;
        }

        public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
        }
    }

    public FaceSettingsRemoveButtonPreferenceController(Context context, String str) {
        super(context, str);
        this.mRemovalCallback = new FaceManager.RemovalCallback() {
            public void onRemovalError(Face face, int i, CharSequence charSequence) {
                Log.e(FaceSettingsRemoveButtonPreferenceController.TAG, "Unable to remove face: " + face.getBiometricId() + " error: " + i + " " + charSequence);
                Toast.makeText(FaceSettingsRemoveButtonPreferenceController.this.mContext, charSequence, 0).show();
                boolean unused = FaceSettingsRemoveButtonPreferenceController.this.mRemoving = false;
            }

            public void onRemovalSucceeded(Face face, int i) {
                if (i != 0) {
                    Log.v(FaceSettingsRemoveButtonPreferenceController.TAG, "Remaining: " + i);
                } else if (!FaceSettingsRemoveButtonPreferenceController.this.mFaceManager.getEnrolledFaces(FaceSettingsRemoveButtonPreferenceController.this.mUserId).isEmpty()) {
                    FaceSettingsRemoveButtonPreferenceController.this.mButton.setEnabled(true);
                } else {
                    boolean unused = FaceSettingsRemoveButtonPreferenceController.this.mRemoving = false;
                    FaceSettingsRemoveButtonPreferenceController.this.mListener.onRemoved();
                }
            }
        };
        this.mOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    FaceSettingsRemoveButtonPreferenceController.this.mButton.setEnabled(false);
                    List enrolledFaces = FaceSettingsRemoveButtonPreferenceController.this.mFaceManager.getEnrolledFaces(FaceSettingsRemoveButtonPreferenceController.this.mUserId);
                    if (enrolledFaces.isEmpty()) {
                        Log.e(FaceSettingsRemoveButtonPreferenceController.TAG, "No faces");
                        return;
                    }
                    if (enrolledFaces.size() > 1) {
                        Log.e(FaceSettingsRemoveButtonPreferenceController.TAG, "Multiple enrollments: " + enrolledFaces.size());
                    }
                    FaceSettingsRemoveButtonPreferenceController.this.mFaceManager.remove((Face) enrolledFaces.get(0), FaceSettingsRemoveButtonPreferenceController.this.mUserId, FaceSettingsRemoveButtonPreferenceController.this.mRemovalCallback);
                    return;
                }
                FaceSettingsRemoveButtonPreferenceController.this.mButton.setEnabled(true);
                boolean unused = FaceSettingsRemoveButtonPreferenceController.this.mRemoving = false;
            }
        };
        this.mContext = context;
        this.mFaceManager = (FaceManager) context.getSystemService(FaceManager.class);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public FaceSettingsRemoveButtonPreferenceController(Context context) {
        this(context, KEY);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mPreference = preference;
        Button button = (Button) ((LayoutPreference) preference).findViewById(R.id.security_settings_face_settings_remove_button);
        this.mButton = button;
        button.setOnClickListener(this);
        if (!FaceSettings.isFaceHardwareDetected(this.mContext)) {
            this.mButton.setEnabled(false);
        } else {
            this.mButton.setEnabled(!this.mRemoving);
        }
    }

    public void onClick(View view) {
        if (view == this.mButton) {
            this.mMetricsFeatureProvider.logClickedPreference(this.mPreference, getMetricsCategory());
            this.mRemoving = true;
            ConfirmRemoveDialog confirmRemoveDialog = new ConfirmRemoveDialog();
            confirmRemoveDialog.setOnClickListener(this.mOnClickListener);
            confirmRemoveDialog.show(this.mActivity.getSupportFragmentManager(), ConfirmRemoveDialog.class.getName());
        }
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setActivity(SettingsActivity settingsActivity) {
        this.mActivity = settingsActivity;
    }
}
