package com.jenshen.awesomeanimation.util.transition;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionWrapper {
    private final Transition transition;
    @Nullable
    private Transition.TransitionListener transitionListener;

    TransitionWrapper(Transition transition, @NonNull Transition.TransitionListener transitionListener) {
        this.transition = transition;
        this.transitionListener = transitionListener;
    }

    public TransitionWrapper(Transition transition) {
        this.transition = transition;
    }

    Transition getTransition() {
        return transition;
    }

    void onStart() {
        if (transitionListener != null) {
            transitionListener.onTransitionStart(transition);
        }
    }

    void onResume() {
        if (transitionListener != null) {
            transitionListener.onTransitionResume(transition);
        }
    }

    void onPause() {
        if (transitionListener != null) {
            transitionListener.onTransitionPause(transition);
        }
    }

    void onEnd() {
        if (transitionListener != null) {
            transitionListener.onTransitionEnd(transition);
        }
    }

    void cancel(Transition.TransitionListener transitionListener) {
        if (transitionListener != null) {
            transitionListener.onTransitionCancel(transition);
        }
        clear(transitionListener);
    }

    void clear(Transition.TransitionListener transitionListener) {
        transition.removeListener(transitionListener);
        this.transitionListener = null;
    }
}