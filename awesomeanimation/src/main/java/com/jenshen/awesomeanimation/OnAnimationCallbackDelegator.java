package com.jenshen.awesomeanimation;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.annotation.Nullable;

public class OnAnimationCallbackDelegator extends AnimatorListenerAdapter {

    @Nullable
    private AnimatorListenerAdapter adapter;

    public void setAdapter(@Nullable AnimatorListenerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        if (adapter != null) {
            adapter.onAnimationCancel(animation);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (adapter != null) {
            adapter.onAnimationEnd(animation);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if (adapter != null) {
            adapter.onAnimationRepeat(animation);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (adapter != null) {
            adapter.onAnimationStart(animation);
        }
    }

    @Override
    public void onAnimationPause(Animator animation) {
        if (adapter != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapter.onAnimationPause(animation);
        }
    }

    @Override
    public void onAnimationResume(Animator animation) {
        if (adapter != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapter.onAnimationResume(animation);
        }
    }
}
