package com.google.android.setupcompat.partnerconfig;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import java.util.EnumMap;

public class PartnerConfigHelper {
    public static final String IS_DYNAMIC_COLOR_ENABLED_METHOD = "isDynamicColorEnabled";
    public static final String IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD = "isExtendedPartnerConfigEnabled";
    public static final String IS_SUW_DAY_NIGHT_ENABLED_METHOD = "isSuwDayNightEnabled";
    public static final String KEY_FALLBACK_CONFIG = "fallbackConfig";
    public static final String SUW_AUTHORITY = "com.google.android.setupwizard.partner";
    public static final String SUW_GET_PARTNER_CONFIG_METHOD = "getOverlayConfig";
    private static final String TAG = "PartnerConfigHelper";
    public static Bundle applyDynamicColorBundle = null;
    public static Bundle applyExtendedPartnerConfigBundle = null;
    private static ContentObserver contentObserver = null;
    private static PartnerConfigHelper instance = null;
    private static int savedConfigUiMode = 0;
    private static int savedOrientation = 1;
    static Bundle suwDayNightEnabledBundle;
    final EnumMap<PartnerConfig, Object> partnerResourceCache = new EnumMap<>(PartnerConfig.class);
    Bundle resultBundle = null;

    public static synchronized PartnerConfigHelper get(Context context) {
        PartnerConfigHelper partnerConfigHelper;
        synchronized (PartnerConfigHelper.class) {
            if (!isValidInstance(context)) {
                instance = new PartnerConfigHelper(context);
            }
            partnerConfigHelper = instance;
        }
        return partnerConfigHelper;
    }

    private static boolean isValidInstance(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (instance == null) {
            savedConfigUiMode = configuration.uiMode & 48;
            savedOrientation = configuration.orientation;
            return false;
        }
        if (isSetupWizardDayNightEnabled(context)) {
            int i = configuration.uiMode;
            if ((i & 48) != savedConfigUiMode) {
                savedConfigUiMode = i & 48;
                resetInstance();
                return false;
            }
        }
        int i2 = configuration.orientation;
        if (i2 == savedOrientation) {
            return true;
        }
        savedOrientation = i2;
        resetInstance();
        return false;
    }

    private PartnerConfigHelper(Context context) {
        getPartnerConfigBundle(context);
        registerContentObserver(context);
    }

    public boolean isAvailable() {
        Bundle bundle = this.resultBundle;
        return bundle != null && !bundle.isEmpty();
    }

    public boolean isPartnerConfigAvailable(PartnerConfig partnerConfig) {
        return isAvailable() && this.resultBundle.containsKey(partnerConfig.getResourceName());
    }

    public int getColor(Context context, PartnerConfig partnerConfig) {
        int i;
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.COLOR) {
            throw new IllegalArgumentException("Not a color resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Integer) this.partnerResourceCache.get(partnerConfig)).intValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                TypedValue typedValue = new TypedValue();
                resources.getValue(resourceId, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return 0;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    i = resources.getColor(resourceId, (Resources.Theme) null);
                } else {
                    i = resources.getColor(resourceId);
                }
                int i2 = i;
                this.partnerResourceCache.put(partnerConfig, Integer.valueOf(i2));
                return i2;
            } catch (NullPointerException unused) {
                return 0;
            }
        }
    }

    public Drawable getDrawable(Context context, PartnerConfig partnerConfig) {
        Drawable drawable;
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.DRAWABLE) {
            throw new IllegalArgumentException("Not a drawable resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (Drawable) this.partnerResourceCache.get(partnerConfig);
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                TypedValue typedValue = new TypedValue();
                resources.getValue(resourceId, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return null;
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    drawable = resources.getDrawable(resourceId, (Resources.Theme) null);
                } else {
                    drawable = resources.getDrawable(resourceId);
                }
                Drawable drawable2 = drawable;
                this.partnerResourceCache.put(partnerConfig, drawable2);
                return drawable2;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return null;
            }
        }
    }

    public String getString(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.STRING) {
            throw new IllegalArgumentException("Not a string resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (String) this.partnerResourceCache.get(partnerConfig);
        } else {
            String str = null;
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                str = resourceEntryFromKey.getResources().getString(resourceEntryFromKey.getResourceId());
                this.partnerResourceCache.put(partnerConfig, str);
                return str;
            } catch (NullPointerException unused) {
                return str;
            }
        }
    }

    public boolean getBoolean(Context context, PartnerConfig partnerConfig, boolean z) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.BOOL) {
            throw new IllegalArgumentException("Not a bool resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Boolean) this.partnerResourceCache.get(partnerConfig)).booleanValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                z = resourceEntryFromKey.getResources().getBoolean(resourceEntryFromKey.getResourceId());
                this.partnerResourceCache.put(partnerConfig, Boolean.valueOf(z));
                return z;
            } catch (NullPointerException unused) {
                return z;
            }
        }
    }

    public float getDimension(Context context, PartnerConfig partnerConfig) {
        return getDimension(context, partnerConfig, 0.0f);
    }

    public float getDimension(Context context, PartnerConfig partnerConfig, float f) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.DIMENSION) {
            throw new IllegalArgumentException("Not a dimension resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return getDimensionFromTypedValue(context, (TypedValue) this.partnerResourceCache.get(partnerConfig));
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                float dimension = resources.getDimension(resourceId);
                this.partnerResourceCache.put(partnerConfig, getTypedValueFromResource(resources, resourceId, 5));
                return getDimensionFromTypedValue(context, (TypedValue) this.partnerResourceCache.get(partnerConfig));
            } catch (NullPointerException unused) {
                return f;
            }
        }
    }

    public float getFraction(Context context, PartnerConfig partnerConfig) {
        return getFraction(context, partnerConfig, 0.0f);
    }

    public float getFraction(Context context, PartnerConfig partnerConfig, float f) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.FRACTION) {
            throw new IllegalArgumentException("Not a fraction resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Float) this.partnerResourceCache.get(partnerConfig)).floatValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                f = resourceEntryFromKey.getResources().getFraction(resourceEntryFromKey.getResourceId(), 1, 1);
                this.partnerResourceCache.put(partnerConfig, Float.valueOf(f));
                return f;
            } catch (NullPointerException unused) {
                return f;
            }
        }
    }

    public int getInteger(Context context, PartnerConfig partnerConfig, int i) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.INTEGER) {
            throw new IllegalArgumentException("Not a integer resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Integer) this.partnerResourceCache.get(partnerConfig)).intValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                i = resourceEntryFromKey.getResources().getInteger(resourceEntryFromKey.getResourceId());
                this.partnerResourceCache.put(partnerConfig, Integer.valueOf(i));
                return i;
            } catch (NullPointerException unused) {
                return i;
            }
        }
    }

    private void getPartnerConfigBundle(Context context) {
        Bundle bundle = this.resultBundle;
        if (bundle == null || bundle.isEmpty()) {
            try {
                this.resultBundle = context.getContentResolver().call(getContentUri(), SUW_GET_PARTNER_CONFIG_METHOD, (String) null, (Bundle) null);
                this.partnerResourceCache.clear();
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "Fail to get config from suw provider");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ResourceEntry getResourceEntryFromKey(Context context, String str) {
        Bundle bundle = this.resultBundle.getBundle(str);
        Bundle bundle2 = this.resultBundle.getBundle(KEY_FALLBACK_CONFIG);
        if (bundle2 != null) {
            bundle.putBundle(KEY_FALLBACK_CONFIG, bundle2.getBundle(str));
        }
        return adjustResourceEntryDayNightMode(context, ResourceEntry.fromBundle(context, bundle));
    }

    private static ResourceEntry adjustResourceEntryDayNightMode(Context context, ResourceEntry resourceEntry) {
        Resources resources = resourceEntry.getResources();
        Configuration configuration = resources.getConfiguration();
        if (!isSetupWizardDayNightEnabled(context) && Util.isNightMode(configuration)) {
            configuration.uiMode = (configuration.uiMode & -49) | 16;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resourceEntry;
    }

    public static synchronized void resetInstance() {
        synchronized (PartnerConfigHelper.class) {
            instance = null;
            suwDayNightEnabledBundle = null;
            applyExtendedPartnerConfigBundle = null;
            applyDynamicColorBundle = null;
        }
    }

    public static boolean isSetupWizardDayNightEnabled(Context context) {
        if (suwDayNightEnabledBundle == null) {
            try {
                suwDayNightEnabledBundle = context.getContentResolver().call(getContentUri(), IS_SUW_DAY_NIGHT_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard DayNight supporting status unknown; return as false.");
                suwDayNightEnabledBundle = null;
                return false;
            }
        }
        Bundle bundle = suwDayNightEnabledBundle;
        if (bundle == null || !bundle.getBoolean(IS_SUW_DAY_NIGHT_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static boolean shouldApplyExtendedPartnerConfig(Context context) {
        if (applyExtendedPartnerConfigBundle == null) {
            try {
                applyExtendedPartnerConfigBundle = context.getContentResolver().call(getContentUri(), IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard extended partner configs supporting status unknown; return as false.");
                applyExtendedPartnerConfigBundle = null;
                return false;
            }
        }
        Bundle bundle = applyExtendedPartnerConfigBundle;
        if (bundle == null || !bundle.getBoolean(IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static boolean isSetupWizardDynamicColorEnabled(Context context) {
        if (applyDynamicColorBundle == null) {
            try {
                applyDynamicColorBundle = context.getContentResolver().call(getContentUri(), IS_DYNAMIC_COLOR_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard dynamic color supporting status unknown; return as false.");
                applyDynamicColorBundle = null;
                return false;
            }
        }
        Bundle bundle = applyDynamicColorBundle;
        if (bundle == null || !bundle.getBoolean(IS_DYNAMIC_COLOR_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    static Uri getContentUri() {
        return new Uri.Builder().scheme("content").authority(SUW_AUTHORITY).build();
    }

    private static TypedValue getTypedValueFromResource(Resources resources, int i, int i2) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(i, typedValue, true);
        if (typedValue.type == i2) {
            return typedValue;
        }
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(i) + " type #0x" + Integer.toHexString(typedValue.type) + " is not valid");
    }

    private static float getDimensionFromTypedValue(Context context, TypedValue typedValue) {
        return typedValue.getDimension(context.getResources().getDisplayMetrics());
    }

    private static void registerContentObserver(Context context) {
        if (isSetupWizardDayNightEnabled(context)) {
            if (contentObserver != null) {
                unregisterContentObserver(context);
            }
            Uri contentUri = getContentUri();
            try {
                contentObserver = new ContentObserver((Handler) null) {
                    public void onChange(boolean z) {
                        super.onChange(z);
                        PartnerConfigHelper.resetInstance();
                    }
                };
                context.getContentResolver().registerContentObserver(contentUri, true, contentObserver);
            } catch (IllegalArgumentException | NullPointerException | SecurityException e) {
                String str = TAG;
                Log.w(str, "Failed to register content observer for " + contentUri + ": " + e);
            }
        }
    }

    private static void unregisterContentObserver(Context context) {
        try {
            context.getContentResolver().unregisterContentObserver(contentObserver);
            contentObserver = null;
        } catch (IllegalArgumentException | NullPointerException | SecurityException e) {
            String str = TAG;
            Log.w(str, "Failed to unregister content observer: " + e);
        }
    }
}
