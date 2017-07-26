package com.jenshen.awesomeanimation.util;

import android.os.Build;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.ViewGroup;

public class TransitionWrapper {
    private Transition transition;

    public TransitionWrapper(Transition transition) {
        this.transition = transition;
    }

    public Transition getTransition() {
        return transition;
    }

    void onResume() {

    }

    void onPause() {
    }

    void cancel(@Nullable ViewGroup viewGroup, Transition.TransitionListener transitionListener) {
        if (viewGroup != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TransitionManager.endTransitions(viewGroup);
        }
        clear(transitionListener);
    }

    public void clear(Transition.TransitionListener transitionListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            transition.removeListener(transitionListener);
        }
        transition = null;
    }
}