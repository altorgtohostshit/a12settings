package com.android.settings.network;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivitySettingsManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.utils.AnnotationSpan;
import com.android.settingslib.CustomDialogPreferenceCompat;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.common.net.InternetDomainName;
import java.util.HashMap;
import java.util.Map;

public class PrivateDnsModeDialogPreference extends CustomDialogPreferenceCompat implements DialogInterface.OnClickListener, RadioGroup.OnCheckedChangeListener, TextWatcher {
    static final String HOSTNAME_KEY = "private_dns_specifier";
    static final String MODE_KEY = "private_dns_mode";
    private static final Map<Integer, Integer> PRIVATE_DNS_MAP;
    EditText mEditText;
    int mMode;
    RadioGroup mRadioGroup;
    private final AnnotationSpan.LinkInfo mUrlLinkInfo = new AnnotationSpan.LinkInfo("url", PrivateDnsModeDialogPreference$$ExternalSyntheticLambda0.INSTANCE);

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    static {
        HashMap hashMap = new HashMap();
        PRIVATE_DNS_MAP = hashMap;
        hashMap.put(1, Integer.valueOf(R.id.private_dns_mode_off));
        hashMap.put(2, Integer.valueOf(R.id.private_dns_mode_opportunistic));
        hashMap.put(3, Integer.valueOf(R.id.private_dns_mode_provider));
    }

    public static String getHostnameFromSettings(ContentResolver contentResolver) {
        return Settings.Global.getString(contentResolver, HOSTNAME_KEY);
    }

    public PrivateDnsModeDialogPreference(Context context) {
        super(context);
        initialize();
    }

    public PrivateDnsModeDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    public PrivateDnsModeDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize();
    }

    public PrivateDnsModeDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(View view) {
        Context context = view.getContext();
        Intent helpIntent = HelpUtils.getHelpIntent(context, context.getString(R.string.help_uri_private_dns), context.getClass().getName());
        if (helpIntent != null) {
            try {
                view.startActivityForResult(helpIntent, 0);
            } catch (ActivityNotFoundException unused) {
                Log.w("PrivateDnsModeDialog", "Activity was not found for intent, " + helpIntent.toString());
            }
        }
    }

    private void initialize() {
        setWidgetLayoutResource(R.layout.restricted_icon);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (isDisabledByAdmin()) {
            preferenceViewHolder.itemView.setEnabled(true);
        }
        View findViewById = preferenceViewHolder.findViewById(R.id.restricted_icon);
        if (findViewById != null) {
            findViewById.setVisibility(isDisabledByAdmin() ? 0 : 8);
        }
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        Context context = getContext();
        ContentResolver contentResolver = context.getContentResolver();
        this.mMode = ConnectivitySettingsManager.getPrivateDnsMode(context);
        EditText editText = (EditText) view.findViewById(R.id.private_dns_mode_provider_hostname);
        this.mEditText = editText;
        editText.addTextChangedListener(this);
        this.mEditText.setText(getHostnameFromSettings(contentResolver));
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.private_dns_radio_group);
        this.mRadioGroup = radioGroup;
        radioGroup.setOnCheckedChangeListener(this);
        this.mRadioGroup.check(PRIVATE_DNS_MAP.getOrDefault(Integer.valueOf(this.mMode), Integer.valueOf(R.id.private_dns_mode_opportunistic)).intValue());
        ((RadioButton) view.findViewById(R.id.private_dns_mode_off)).setText(R.string.private_dns_mode_off);
        ((RadioButton) view.findViewById(R.id.private_dns_mode_opportunistic)).setText(R.string.private_dns_mode_opportunistic);
        ((RadioButton) view.findViewById(R.id.private_dns_mode_provider)).setText(R.string.private_dns_mode_provider);
        TextView textView = (TextView) view.findViewById(R.id.private_dns_help_info);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo(context, "url", HelpUtils.getHelpIntent(context, context.getString(R.string.help_uri_private_dns), context.getClass().getName()));
        if (linkInfo.isActionable()) {
            textView.setText(AnnotationSpan.linkify(context.getText(R.string.private_dns_help_message), linkInfo));
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            Context context = getContext();
            if (this.mMode == 3) {
                ConnectivitySettingsManager.setPrivateDnsHostname(context, this.mEditText.getText().toString());
            }
            FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(context, 1249, this.mMode);
            ConnectivitySettingsManager.setPrivateDnsMode(context, this.mMode);
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.private_dns_mode_off) {
            this.mMode = 1;
        } else if (i == R.id.private_dns_mode_opportunistic) {
            this.mMode = 2;
        } else if (i == R.id.private_dns_mode_provider) {
            this.mMode = 3;
        }
        updateDialogInfo();
    }

    public void afterTextChanged(Editable editable) {
        updateDialogInfo();
    }

    public void performClick() {
        RestrictedLockUtils.EnforcedAdmin enforcedAdmin = getEnforcedAdmin();
        if (enforcedAdmin == null) {
            super.performClick();
        } else {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getContext(), enforcedAdmin);
        }
    }

    private RestrictedLockUtils.EnforcedAdmin getEnforcedAdmin() {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(getContext(), "disallow_config_private_dns", UserHandle.myUserId());
    }

    private boolean isDisabledByAdmin() {
        return getEnforcedAdmin() != null;
    }

    private Button getSaveButton() {
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog == null) {
            return null;
        }
        return alertDialog.getButton(-1);
    }

    private void updateDialogInfo() {
        boolean z = true;
        boolean z2 = 3 == this.mMode;
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setEnabled(z2);
        }
        Button saveButton = getSaveButton();
        if (saveButton != null) {
            if (z2) {
                z = InternetDomainName.isValid(this.mEditText.getText().toString());
            }
            saveButton.setEnabled(z);
        }
    }
}
