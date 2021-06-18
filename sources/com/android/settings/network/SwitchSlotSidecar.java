package com.android.settings.network;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.AsyncTaskSidecar;
import com.android.settings.SidecarFragment;

public class SwitchSlotSidecar extends AsyncTaskSidecar<Param, Result> {
    private Exception mException;

    static class Param {
        int command;
        int slotId;

        Param() {
        }
    }

    static class Result {
        Exception exception;

        Result() {
        }
    }

    public static SwitchSlotSidecar get(FragmentManager fragmentManager) {
        return (SwitchSlotSidecar) SidecarFragment.get(fragmentManager, "SwitchSlotSidecar", SwitchSlotSidecar.class, (Bundle) null);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void runSwitchToRemovableSlot(int i) {
        Param param = new Param();
        param.command = 0;
        param.slotId = i;
        super.run(param);
    }

    /* access modifiers changed from: protected */
    public Result doInBackground(Param param) {
        Result result = new Result();
        if (param == null) {
            result.exception = new UiccSlotsException("Null param");
            return result;
        }
        try {
            if (param.command != 0) {
                Log.e("SwitchSlotSidecar", "Wrong command.");
            } else {
                UiccSlotUtil.switchToRemovableSlot(param.slotId, getContext());
            }
        } catch (UiccSlotsException e) {
            result.exception = e;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
        Exception exc = result.exception;
        if (exc == null) {
            setState(2, 0);
            return;
        }
        this.mException = exc;
        setState(3, 0);
    }
}
