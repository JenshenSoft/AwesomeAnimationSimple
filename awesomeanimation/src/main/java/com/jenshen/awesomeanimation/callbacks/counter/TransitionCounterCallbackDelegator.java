package com.jenshen.awesomeanimation.callbacks.counter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.util.Log;

import com.jenshen.awesomeanimation.callbacks.TransitionCallbackDelegator;
import com.jenshen.awesomeanimation.util.transition.TransitionUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionCounterCallbackDelegator extends TransitionCallbackDelegator {

    private static final String TAG = "AwesomeAnimation: " + TransitionCounterCallbackDelegator.class.getSimpleName();

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
        clear(transition);
    }

    public void clear() {
        if (transitions != null) {
            for (Transition transition : transitions) {
                transition.removeListener(this);
            }
            transitions.clear();
        }
    }

    @Override
    public void onTransitionStart(Transition transition) {
        Log.d(TAG, "onTransitionStart " + countStart);
        countStart++;
        if (countStart == animationsCount) {
            super.onTransitionStart(transition);
        }
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        Log.d(TAG, "onTransitionStart " + countEnd);
        countEnd++;
        if (countEnd == animationsCount) {
            super.onTransitionEnd(transition);
        }
        clear(transition);
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        Log.d(TAG, "countCancel " + countEnd);
        countCancel++;
        if (countCancel == animationsCount) {
            super.onTransitionCancel(transition);
        }
        clear(transition);
    }

    @Override
    public void onTransitionPause(Transition transition) {
        Log.d(TAG, "onTransitionPause " + countEnd);
        countPause++;
        if (countPause == animationsCount) {
            super.onTransitionPause(transition);
        }
    }

    @Override
    public void onTransitionResume(Transition transition) {
        Log.d(TAG, "onTransitionResume " + countEnd);
        countResume++;
        if (countResume == animationsCount) {
            super.onTransitionResume(transition);
        }
    }

    private void clear(Transition transition) {
        transition.removeListener(this);
        if (transitions != null) {
            for (Transition tr : transitions) {
                if (TransitionUtil.equalsTransitions(transition, tr)) {
                    tr.removeListener(this);
                    transitions.remove(tr);
                    break;
                }
            }
        }
    }
}
