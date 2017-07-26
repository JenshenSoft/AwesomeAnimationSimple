package com.jenshen.awesomeanimation.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;

public class AnimatorWrapper {
    private final Animator animator;
    private long currentPlayTime;

    public AnimatorWrapper(Animator animator) {
        this.animator = animator;
    }

    void onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animator.resume();
        } else {
            if (animator instanceof ValueAnimator) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                currentPlayTime = valueAnimator.getCurrentPlayTime();
            }
            animator.cancel();
        }
    }

    void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animator.pause();
        } else {
            if (animator instanceof ValueAnimator) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                valueAnimator.setCurrentPlayTime(currentPlayTime);
                currentPlayTime = 0;
            }
            animator.start();
        }
    }

    void cancel() {
        animator.cancel();
        clear();
    }

    void clear() {
        animator.removeAllListeners();
    }

    Animator getAnimator() {
        return animator;
    }
}