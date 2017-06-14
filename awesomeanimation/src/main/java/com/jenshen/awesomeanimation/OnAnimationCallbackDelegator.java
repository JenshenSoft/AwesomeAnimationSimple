package com.jenshen.awesomeanimation;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.jenshen.awesomeanimation.OnAnimationCallbackDelegator.AnimationState.CANCEL;
import static com.jenshen.awesomeanimation.OnAnimationCallbackDelegator.AnimationState.END;
import static com.jenshen.awesomeanimation.OnAnimationCallbackDelegator.AnimationState.PAUSE;
import static com.jenshen.awesomeanimation.OnAnimationCallbackDelegator.AnimationState.REPEAT;
import static com.jenshen.awesomeanimation.OnAnimationCallbackDelegator.AnimationState.RESUME;
import static com.jenshen.awesomeanimation.OnAnimationCallbackDelegator.AnimationState.START;

public class OnAnimationCallbackDelegator extends AnimatorListenerAdapter {

    @Nullable
    private List<AnimatorListenerAdapter> adapters;
    private List<Integer> animationState;

    public void addAdapter(@NonNull AnimatorListenerAdapter adapter) {
        if (adapters == null) {
            adapters = new ArrayList<>();
        }
        adapters.add(adapter);
        if (animationState != null) {
            for (Integer state : animationState) {
                switch (state) {
                    case START:
                        adapter.onAnimationStart(null);
                        break;
                    case END:
                        adapter.onAnimationEnd(null);
                        break;
                    case CANCEL:
                        adapter.onAnimationStart(null);
                        break;
                    case REPEAT:
                        adapter.onAnimationRepeat(null);
                        break;
                    case PAUSE:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            adapter.onAnimationPause(null);
                        }
                        break;
                    case RESUME:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            adapter.onAnimationResume(null);
                        }
                        break;
                }
            }
        }
    }

    public void removeAdapter(@NonNull AnimatorListenerAdapter adapter) {
        if (adapters == null) {
            return;
        }
        adapters.remove(adapter);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        addState(CANCEL);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationCancel(animation);
            }
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        addState(END);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationEnd(animation);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        addState(REPEAT);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationRepeat(animation);
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        addState(START);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationStart(animation);
            }
        }
    }

    @Override
    public void onAnimationPause(Animator animation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(PAUSE);
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationPause(animation);
            }
        }
    }

    @Override
    public void onAnimationResume(Animator animation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(RESUME);
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationResume(animation);
            }
        }
    }

    private void addState(@AnimationState int state) {
        if (animationState == null) {
            animationState = new ArrayList<>();
        }
        animationState.add(state);
    }

    @IntDef({START, END, CANCEL, REPEAT, PAUSE, RESUME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationState {
        int START = 1; // 0000 0001
        int END = 2; // 0000 0010
        int CANCEL = 4; // 0000 0100
        int REPEAT = 8; // 0000 1000
        int PAUSE = 16; // 0001 0000
        int RESUME = 32; // 0010 0000;
    }
}
