package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.ArrayList;
import java.util.List;

public class SwitchBar extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    private static final int[] XML_ATTRIBUTES = {R.attr.switchBarMarginStart, R.attr.switchBarMarginEnd, R.attr.switchBarBackgroundColor, R.attr.switchBarBackgroundActivatedColor, R.attr.switchBarRestrictionIcon};
    private int mBackgroundActivatedColor;
    private int mBackgroundColor;
    /* access modifiers changed from: private */
    public boolean mDisabledByAdmin;
    /* access modifiers changed from: private */
    public RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private String mLabel;
    private boolean mLoggingIntialized;
    /* access modifiers changed from: private */
    public final MetricsFeatureProvider mMetricsFeatureProvider;
    /* access modifiers changed from: private */
    public String mMetricsTag;
    private String mOffText;
    private String mOnText;
    private ImageView mRestrictedIcon;
    private String mSummary;
    private final TextAppearanceSpan mSummarySpan;
    private ToggleSwitch mSwitch;
    private final List<OnSwitchChangeListener> mSwitchChangeListeners;
    private TextView mTextView;

    public interface OnSwitchChangeListener {
        void onSwitchChanged(Switch switchR, boolean z);
    }

    public SwitchBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwitchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwitchBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SwitchBar(final Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mSwitchChangeListeners = new ArrayList();
        this.mEnforcedAdmin = null;
        LayoutInflater.from(context).inflate(R.layout.switch_bar, this);
        setFocusable(true);
        setClickable(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, XML_ATTRIBUTES);
        this.mBackgroundColor = obtainStyledAttributes.getColor(2, 0);
        this.mBackgroundActivatedColor = obtainStyledAttributes.getColor(3, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(4);
        obtainStyledAttributes.recycle();
        this.mTextView = (TextView) findViewById(R.id.switch_text);
        this.mSummarySpan = new TextAppearanceSpan(this.mContext, 2131952323);
        ((ViewGroup.MarginLayoutParams) this.mTextView.getLayoutParams()).setMarginStart((int) obtainStyledAttributes.getDimension(0, 0.0f));
        ToggleSwitch toggleSwitch = (ToggleSwitch) findViewById(R.id.switch_widget);
        this.mSwitch = toggleSwitch;
        toggleSwitch.setSaveEnabled(false);
        this.mSwitch.setFocusable(false);
        this.mSwitch.setClickable(false);
        ((ViewGroup.MarginLayoutParams) this.mSwitch.getLayoutParams()).setMarginEnd((int) obtainStyledAttributes.getDimension(1, 0.0f));
        setBackgroundColor(this.mBackgroundColor);
        setSwitchBarText(R.string.switch_on_text, R.string.switch_off_text);
        addOnSwitchChangeListener(new SwitchBar$$ExternalSyntheticLambda0(this));
        ImageView imageView = (ImageView) findViewById(R.id.restricted_icon);
        this.mRestrictedIcon = imageView;
        imageView.setImageDrawable(drawable);
        this.mRestrictedIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SwitchBar.this.mDisabledByAdmin) {
                    MetricsFeatureProvider access$200 = SwitchBar.this.mMetricsFeatureProvider;
                    access$200.action(0, 853, 0, SwitchBar.this.mMetricsTag + "/switch_bar|restricted", 1);
                    RestrictedLockUtils.sendShowAdminSupportDetailsIntent(context, SwitchBar.this.mEnforcedAdmin);
                }
            }
        });
        setVisibility(8);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Switch switchR, boolean z) {
        setTextViewLabelAndBackground(z);
    }

    public boolean performClick() {
        return getDelegatingView().performClick();
    }

    public void setMetricsTag(String str) {
        this.mMetricsTag = str;
    }

    public void setTextViewLabelAndBackground(boolean z) {
        this.mLabel = z ? this.mOnText : this.mOffText;
        setBackgroundColor(z ? this.mBackgroundActivatedColor : this.mBackgroundColor);
        updateText();
    }

    public void setSwitchBarText(int i, int i2) {
        this.mOnText = getResources().getString(i);
        this.mOffText = getResources().getString(i2);
        setTextViewLabelAndBackground(isChecked());
    }

    public void setSummary(String str) {
        this.mSummary = str;
        updateText();
    }

    private void updateText() {
        if (TextUtils.isEmpty(this.mSummary)) {
            this.mTextView.setText(this.mLabel);
            return;
        }
        SpannableStringBuilder append = new SpannableStringBuilder(this.mLabel).append(10);
        int length = append.length();
        append.append(this.mSummary);
        append.setSpan(this.mSummarySpan, length, append.length(), 0);
        this.mTextView.setText(append);
    }

    public void setChecked(boolean z) {
        setTextViewLabelAndBackground(z);
        this.mSwitch.setChecked(z);
    }

    public void setCheckedInternal(boolean z) {
        setTextViewLabelAndBackground(z);
        this.mSwitch.setCheckedInternal(z);
    }

    public boolean isChecked() {
        return this.mSwitch.isChecked();
    }

    public void setEnabled(boolean z) {
        if (!z || !this.mDisabledByAdmin) {
            super.setEnabled(z);
            this.mTextView.setEnabled(z);
            this.mSwitch.setEnabled(z);
            return;
        }
        setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
    }

    /* access modifiers changed from: package-private */
    public View getDelegatingView() {
        return this.mDisabledByAdmin ? this.mRestrictedIcon : this.mSwitch;
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mEnforcedAdmin = enforcedAdmin;
        if (enforcedAdmin != null) {
            super.setEnabled(true);
            this.mDisabledByAdmin = true;
            this.mTextView.setEnabled(false);
            this.mSwitch.setEnabled(false);
            this.mSwitch.setVisibility(8);
            this.mRestrictedIcon.setVisibility(0);
            return;
        }
        this.mDisabledByAdmin = false;
        this.mSwitch.setVisibility(0);
        this.mRestrictedIcon.setVisibility(8);
        setEnabled(true);
    }

    public final ToggleSwitch getSwitch() {
        return this.mSwitch;
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    public void propagateChecked(boolean z) {
        int size = this.mSwitchChangeListeners.size();
        for (int i = 0; i < size; i++) {
            this.mSwitchChangeListeners.get(i).onSwitchChanged(this.mSwitch, z);
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (this.mLoggingIntialized) {
            MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
            metricsFeatureProvider.action(0, 853, 0, this.mMetricsTag + "/switch_bar", z ? 1 : 0);
        }
        this.mLoggingIntialized = true;
        propagateChecked(z);
    }

    public void addOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        if (!this.mSwitchChangeListeners.contains(onSwitchChangeListener)) {
            this.mSwitchChangeListeners.add(onSwitchChangeListener);
            return;
        }
        throw new IllegalStateException("Cannot add twice the same OnSwitchChangeListener");
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean checked;
        boolean visible;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.checked = ((Boolean) parcel.readValue((ClassLoader) null)).booleanValue();
            this.visible = ((Boolean) parcel.readValue((ClassLoader) null)).booleanValue();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeValue(Boolean.valueOf(this.checked));
            parcel.writeValue(Boolean.valueOf(this.visible));
        }

        public String toString() {
            return "SwitchBar.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + this.checked + " visible=" + this.visible + "}";
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.checked = this.mSwitch.isChecked();
        savedState.visible = isShowing();
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mSwitch.setCheckedInternal(savedState.checked);
        setTextViewLabelAndBackground(savedState.checked);
        setVisibility(savedState.visible ? 0 : 8);
        this.mSwitch.setOnCheckedChangeListener(savedState.visible ? this : null);
        requestLayout();
    }
}
