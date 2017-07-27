package com.jenshen.awesomeanimation.callbacks;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jenshen.awesomeanimation.callbacks.AnimationState.CANCEL;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.END;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.PAUSE;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.REPEAT;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.RESUME;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.START;

public class AnimatorCallbackDelegator extends AnimatorListenerAdapter {

    private static final String TAG = "AwesomeAnimation: " + AnimatorCallbackDelegator.class.getSimpleName();

    @Nullable
    private List<AnimatorListenerAdapter> adapters;
    private List<Integer> animationState;

    public void addListener(@NonNull AnimatorListenerAdapter adapter) {
        if (adapters == null) {
            adapters = new CopyOnWriteArrayList<>();
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

    public void removeListener(@NonNull AnimatorListenerAdapter adapter) {
        if (adapters == null) {
            return;
        }
        adapters.remove(adapter);
    }

    public void clear() {
        if (adapters != null) {
            adapters.clear();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        Log.d(TAG, "onAnimationCancel");
        addState(CANCEL);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationCancel(animation);
            }
            clear();
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d(TAG, "onAnimationEnd");
        addState(END);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationEnd(animation);
            }
            clear();
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        Log.d(TAG, "onAnimationRepeat");
        addState(REPEAT);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationRepeat(animation);
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Log.d(TAG, "onAnimationStart");
        addState(START);
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationStart(animation);
            }
        }
    }

    @Override
    public void onAnimationPause(Animator animation) {
        Log.d(TAG, "onAnimationPause");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(PAUSE);
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationPause(animation);
            }
        }
    }

    @Override
    public void onAnimationResume(Animator animation) {
        Log.d(TAG, "onAnimationResume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(RESUME);
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationResume(animation);
            }
        }
    }

    private void addState(@AnimationState int state) {
        if (animationState == null) {
            animationState = new CopyOnWriteArrayList<>();
        }
        animationState.add(state);
    }
}
