package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.storage.StorageManager;
import android.text.BidiFormatter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.android.settings.R;
import java.util.Locale;

public class AccessibilityServiceWarning {
    private static final View.OnTouchListener filterTouchListener = AccessibilityServiceWarning$$ExternalSyntheticLambda1.INSTANCE;

    interface UninstallActionPerformer {
        void uninstallPackage();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$static$0(View view, MotionEvent motionEvent) {
        if ((motionEvent.getFlags() & 1) == 0 && (motionEvent.getFlags() & 2) == 0) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            Toast.makeText(view.getContext(), R.string.touch_filtered_warning, 0).show();
        }
        return true;
    }

    public static Dialog createCapabilitiesDialog(Context context, AccessibilityServiceInfo accessibilityServiceInfo, View.OnClickListener onClickListener, UninstallActionPerformer uninstallActionPerformer) {
        AlertDialog create = new AlertDialog.Builder(context).setView(createEnableDialogContentView(context, accessibilityServiceInfo, onClickListener, uninstallActionPerformer)).create();
        Window window = create.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.privateFlags |= 524288;
        window.setAttributes(attributes);
        create.create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private static boolean isFullDiskEncrypted() {
        return StorageManager.isNonDefaultBlockEncrypted();
    }

    private static View createEnableDialogContentView(Context context, AccessibilityServiceInfo accessibilityServiceInfo, View.OnClickListener onClickListener, UninstallActionPerformer uninstallActionPerformer) {
        Drawable drawable;
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.enable_accessibility_service_dialog_content, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.encryption_warning);
        if (isFullDiskEncrypted()) {
            textView.setText(context.getString(R.string.enable_service_encryption_warning, new Object[]{getServiceName(context, accessibilityServiceInfo)}));
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        if (accessibilityServiceInfo.getResolveInfo().getIconResource() == 0) {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_accessibility_generic);
        } else {
            drawable = accessibilityServiceInfo.getResolveInfo().loadIcon(context.getPackageManager());
        }
        ((ImageView) inflate.findViewById(R.id.permissionDialog_icon)).setImageDrawable(drawable);
        ((TextView) inflate.findViewById(R.id.permissionDialog_title)).setText(context.getString(R.string.enable_service_title, new Object[]{getServiceName(context, accessibilityServiceInfo)}));
        Button button = (Button) inflate.findViewById(R.id.permission_enable_allow_button);
        button.setOnClickListener(onClickListener);
        button.setOnTouchListener(filterTouchListener);
        ((Button) inflate.findViewById(R.id.permission_enable_deny_button)).setOnClickListener(onClickListener);
        Button button2 = (Button) inflate.findViewById(R.id.permission_enable_uninstall_button);
        if (!AccessibilityUtil.isSystemApp(accessibilityServiceInfo)) {
            button2.setVisibility(0);
            button2.setOnClickListener(new AccessibilityServiceWarning$$ExternalSyntheticLambda0(uninstallActionPerformer));
        }
        return inflate;
    }

    public static Dialog createDisableDialog(Context context, AccessibilityServiceInfo accessibilityServiceInfo, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context).setTitle((CharSequence) context.getString(R.string.disable_service_title, new Object[]{accessibilityServiceInfo.getResolveInfo().loadLabel(context.getPackageManager())})).setMessage((CharSequence) context.getString(R.string.disable_service_message, new Object[]{context.getString(R.string.accessibility_dialog_button_stop), getServiceName(context, accessibilityServiceInfo)})).setCancelable(true).setPositiveButton((int) R.string.accessibility_dialog_button_stop, onClickListener).setNegativeButton((int) R.string.accessibility_dialog_button_cancel, onClickListener).create();
    }

    private static CharSequence getServiceName(Context context, AccessibilityServiceInfo accessibilityServiceInfo) {
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        return BidiFormatter.getInstance(locale).unicodeWrap(accessibilityServiceInfo.getResolveInfo().loadLabel(context.getPackageManager()));
    }
}
