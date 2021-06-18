package com.android.settings.applications.intentpicker;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;

public class SupportedLinkViewModel extends AndroidViewModel {
    private List<SupportedLinkWrapper> mSupportedLinkWrapperList;

    public SupportedLinkViewModel(Application application) {
        super(application);
    }

    public void setSupportedLinkWrapperList(List<SupportedLinkWrapper> list) {
        this.mSupportedLinkWrapperList = list;
    }

    public List<SupportedLinkWrapper> getSupportedLinkWrapperList() {
        return this.mSupportedLinkWrapperList;
    }
}
