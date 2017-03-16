package com.jenshen.awesomeanimation;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OnAnimationCallbackDelegator extends AnimatorListenerAdapter {

    @Nullable
    private List<AnimatorListenerAdapter> adapters;

    public void addAdapter(@NonNull AnimatorListenerAdapter adapter) {
        createListIfNonCreated().add(adapter);
    }

    public void removeAdapter(@NonNull AnimatorListenerAdapter adapter) {
        createListIfNonCreated().remove(adapter);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationCancel(animation);
            }
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationEnd(animation);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationRepeat(animation);
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationStart(animation);
            }
        }
    }

    @Override
    public void onAnimationPause(Animator animation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationPause(animation);
            }
        }
    }

    @Override
    public void onAnimationResume(Animator animation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            for (AnimatorListenerAdapter adapter : adapters) {
                adapter.onAnimationResume(animation);
            }
        }
    }

    private List<AnimatorListenerAdapter> createListIfNonCreated() {
        if (adapters == null) {
            adapters = new ArrayList<>();
        }
        return adapters;
    }
}
