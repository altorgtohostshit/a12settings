package com.google.android.libraries.hats20;

import com.google.android.apps.common.testing.p003ui.espresso.IdlingResource$ResourceCallback;

public class IdleResourceManager {
    private IdlingResource$ResourceCallback espressoIdlingCallback;
    private boolean isMultipleChoiceSelectionAnimating;
    private boolean isThankYouAnimating;

    public void setIsMultipleChoiceSelectionAnimating(boolean z) {
        boolean isIdleNow = isIdleNow();
        this.isMultipleChoiceSelectionAnimating = z;
        if (!isIdleNow && isIdleNow()) {
            transitionToIdle();
        }
    }

    public void setIsThankYouAnimating(boolean z) {
        boolean isIdleNow = isIdleNow();
        this.isThankYouAnimating = z;
        if (!isIdleNow && isIdleNow()) {
            transitionToIdle();
        }
    }

    private void transitionToIdle() {
        IdlingResource$ResourceCallback idlingResource$ResourceCallback = this.espressoIdlingCallback;
        if (idlingResource$ResourceCallback != null) {
            idlingResource$ResourceCallback.onTransitionToIdle();
        }
    }

    public boolean isIdleNow() {
        return !this.isMultipleChoiceSelectionAnimating && !this.isThankYouAnimating;
    }
}
