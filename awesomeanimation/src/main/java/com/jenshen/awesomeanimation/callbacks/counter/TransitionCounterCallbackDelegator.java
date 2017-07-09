package com.jenshen.awesomeanimation.callbacks.counter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

import com.jenshen.awesomeanimation.TransitionCallbackDelegator;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionCounterCallbackDelegator extends TransitionCallbackDelegator {
    private int animationsCount = 0;
    private int countEnd = 0;
    private int countCancel = 0;
    private int countStart = 0;
    private int countPause = 0;
    private int countResume = 0;

    @Override
    public void addListener(@NonNull  Transition.TransitionListener adapter) {
        animationsCount++;
        super.addListener(adapter);
    }

    @Override
    public void removeListener(@NonNull  Transition.TransitionListener adapter) {
        animationsCount--;
        super.removeListener(adapter);
    }

    @Override
    public void onTransitionStart(Transition transition) {
        countStart++;
        if (countStart == animationsCount) {
            super.onTransitionStart(transition);
        }
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        countEnd++;
        if (countEnd == animationsCount) {
            super.onTransitionEnd(transition);
        }
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        countCancel++;
        if (countCancel == animationsCount) {
            super.onTransitionCancel(transition);
        }
    }

    @Override
    public void onTransitionPause(Transition transition) {
        countPause++;
        if (countPause == animationsCount) {
            super.onTransitionPause(transition);
        }
    }

    @Override
    public void onTransitionResume(Transition transition) {
        countResume++;
        if (countResume == animationsCount) {
            super.onTransitionResume(transition);
        }
    }
}
