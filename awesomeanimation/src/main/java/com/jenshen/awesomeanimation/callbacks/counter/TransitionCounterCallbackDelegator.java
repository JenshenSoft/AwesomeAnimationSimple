package com.jenshen.awesomeanimation.callbacks.counter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

import com.jenshen.awesomeanimation.callbacks.TransitionCallbackDelegator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionCounterCallbackDelegator extends TransitionCallbackDelegator {
    private int animationsCount = 0;
    private int countEnd = 0;
    private int countCancel = 0;
    private int countStart = 0;
    private int countPause = 0;
    private int countResume = 0;
    private List<Transition> transitions;

    public void addTransition(@NonNull Transition transition) {
        addTransition(transition, true);
    }

    public void addTransition(@NonNull Transition transition, boolean addListener) {
        animationsCount++;
        if (transitions == null) {
            transitions = new CopyOnWriteArrayList<>();
        }
        transitions.add(transition);
        if (addListener) {
            transition.addListener(this);
        }
    }

    public void removeTransition(@NonNull Transition transition) {
        animationsCount--;
        transition.removeListener(this);
        if (transitions != null) {
            transitions.remove(transition);
        }
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
