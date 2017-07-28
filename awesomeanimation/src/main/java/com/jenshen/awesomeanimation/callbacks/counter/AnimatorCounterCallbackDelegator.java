package com.jenshen.awesomeanimation.callbacks.counter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jenshen.awesomeanimation.callbacks.AnimatorCallbackDelegator;
import com.jenshen.awesomeanimation.util.animator.AnimatorUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimatorCounterCallbackDelegator extends AnimatorCallbackDelegator {

    private static final String TAG = "AwesomeAnimation: " + AnimatorCounterCallbackDelegator.class.getSimpleName();

    private int animationsCount = 0;
    private int countEnd = 0;
    private int countCancel = 0;
    private int countRepeat = 0;
    private int countStart = 0;
    private int countPause = 0;
    private int countResume = 0;
    private List<Animator> animators;

    public void addAnimator(@NonNull Animator animator) {
        animationsCount++;
        if (animators == null) {
            animators = new CopyOnWriteArrayList<>();
        }
        animators.add(animator);
        animator.addListener(this);
    }

    public void removeAnimator(@NonNull Animator animator) {
        animationsCount--;
        clear(animator);
    }

    public void clear() {
        if (animators != null) {
            for (Animator animator : animators) {
                animator.removeListener(this);
            }
            animators.clear();
        }
    }

    @Override
    public void addListener(@NonNull AnimatorListenerAdapter adapter) {
        animationsCount++;
        super.addListener(adapter);
    }

    @Override
    public void removeListener(@NonNull AnimatorListenerAdapter adapter) {
        animationsCount--;
        super.removeListener(adapter);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        Log.d(TAG, "onAnimationCancel " + countCancel);
        countCancel++;
        if (countCancel == animationsCount) {
            super.onAnimationCancel(animation);
        }
        clear(animation);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d(TAG, "onAnimationEnd " + countEnd);
        countEnd++;
        if (countEnd == animationsCount) {
            super.onAnimationEnd(animation);
        }
        clear(animation);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        Log.d(TAG, "onAnimationRepeat " + countRepeat);
        countRepeat++;
        if (countRepeat == animationsCount) {
            super.onAnimationRepeat(animation);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Log.d(TAG, "onAnimationStart " + countStart);
        countStart++;
        if (countStart == animationsCount) {
            super.onAnimationStart(animation);
        }
    }

    @Override
    public void onAnimationPause(Animator animation) {
        Log.d(TAG, "onAnimationPause " + countPause);
        countPause++;
        if (countPause == animationsCount) {
            super.onAnimationPause(animation);
        }
    }

    @Override
    public void onAnimationResume(Animator animation) {
        Log.d(TAG, "onAnimationResume " + countResume);
        countResume++;
        if (countResume == animationsCount) {
            super.onAnimationResume(animation);
        }
    }

    private void clear(Animator animator) {
        animator.removeListener(this);
        if (animators != null) {
            for (Animator an : animators) {
                if (AnimatorUtil.equalsAnimators(animator, an)) {
                    an.removeListener(this);
                    animators.remove(an);
                }
            }
        }
    }
}
