package com.jenshen.awesomeanimation.util.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class TransitionHandler {

    private static final String TAG = "AwesomeAnimation: " + TransitionHandler.class.getSimpleName();

    private List<TransitionWrapper> transitionList;
    private boolean onPause;

    private Transition.TransitionListener transitionListener;

    public void addTransition(Transition transition, Transition.TransitionListener transitionListener) {
        final List<TransitionWrapper> transitions = createListTransitionsInNeeded();
        transitions.add(new TransitionWrapper(transition, transitionListener));
        transition.addListener(this.transitionListener);
    }

    public void removeTransition(Transition transitionToRemove) {
        if (transitionList != null) {
            for (TransitionWrapper transitionWrapper : transitionList) {
                if (equalsTransitions(transitionWrapper.getTransition(), transitionToRemove)) {
                    transitionWrapper.clear(transitionListener);
                    transitionList.remove(transitionWrapper);
                    return;
                }
            }
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            onResume();
        } else {
            onPause();
        }
    }

    public void onVisibilityChanged(int visibility) {
        if (visibility == View.VISIBLE) {
            onResume();
        } else {
            onPause();
        }
    }

    public boolean isOnPause() {
        return onPause;
    }

    public void onResume() {
        if (!onPause) {
            return;
        }
        onPause = false;
        Log.d(TAG, "onResume");
        if (transitionList != null) {
            for (TransitionWrapper transitionWrapper : transitionList) {
                transitionWrapper.onResume();
            }
        }
    }

    public void onPause() {
        if (onPause) {
            return;
        }
        onPause = true;
        Log.d(TAG, "onPause");
        if (transitionList != null) {
            for (TransitionWrapper transitionWrapper : transitionList) {
                transitionWrapper.onPause();
            }
        }
    }

    public void cancel() {
        Log.d(TAG, "cancel");
        if (this.transitionList != null) {
            for (TransitionWrapper transition : transitionList) {
                transition.cancel(transitionListener);
            }
            this.transitionList.clear();
            this.transitionList = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<TransitionWrapper> createListTransitionsInNeeded() {
        if (this.transitionList == null) {
            this.transitionList = new CopyOnWriteArrayList<>();
            this.transitionListener = new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    if (transitionList != null) {
                        for (TransitionWrapper transitionWrapper : transitionList) {
                            if (equalsTransitions(transitionWrapper.getTransition(), transition)) {
                                transitionWrapper.onStart();
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (transitionList != null) {
                        for (TransitionWrapper transitionWrapper : transitionList) {
                            if (equalsTransitions(transitionWrapper.getTransition(), transition)) {
                                transitionWrapper.onEnd();
                                return;
                            }
                        }
                        removeTransition(transition);
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    removeTransition(transition);
                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            };
        }
        return this.transitionList;
    }


    private boolean equalsTransitions(Transition transition1, Transition transition2) {
        if (transition1.equals(transition2)) {
            return true;
        }
        if (transition1.getDuration() != transition2.getDuration()) {
            return false;
        }
        if (transition1.getStartDelay() != transition2.getStartDelay()) {
            return false;
        }
        if (!transition1.getName().equals(transition2.getName())) {
            return false;
        }
        String[] transitionProperties1 = transition1.getTransitionProperties();
        String[] transitionProperties2 = transition2.getTransitionProperties();
        if (transitionProperties1 != null || transitionProperties2 != null) {
            if (transitionProperties1 != null && transitionProperties2 == null || transitionProperties1 == null) {
                return false;
            }
            if (transitionProperties1.length != transitionProperties2.length) {
                return false;
            }
            for (int i = 0; i < transitionProperties2.length; i++) {
                if (!transitionProperties1[i].equals(transitionProperties2[i])) {
                    return false;
                }
            }
        }
        List<Integer> targetIds1 = transition1.getTargetIds();
        List<Integer> targetIds2 = transition2.getTargetIds();
        if (targetIds1 != null || targetIds2 != null) {
            if (targetIds1 != null && targetIds2 == null || targetIds1 == null) {
                return false;
            }
            if (targetIds1.size() != targetIds2.size()) {
                return false;
            }
            for (int i = 0; i < targetIds1.size(); i++) {
                if (!targetIds1.get(i).equals(targetIds2.get(i))) {
                    return false;
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isTransitionSet1 = transition1 instanceof TransitionSet;
            boolean isTransitionSet2 = transition2 instanceof TransitionSet;
            if (isTransitionSet1 || isTransitionSet2) {
                if (!(isTransitionSet1) || !(isTransitionSet2)) {
                    return false;
                }
                TransitionSet transitionSet1 = (TransitionSet) transition1;
                TransitionSet transitionSet2 = (TransitionSet) transition2;
                if (transitionSet1.getTransitionCount() != transitionSet2.getTransitionCount()) {
                    return false;
                }
                for (int i = 0; i < transitionSet1.getTransitionCount(); i++) {
                    Transition transitionAt1 = transitionSet1.getTransitionAt(i);
                    Transition transitionAt2 = transitionSet2.getTransitionAt(i);
                    if (!equalsTransitions(transitionAt1, transitionAt2)) {
                        return false;
                    }
                }
            }
            List<String> targetNames1 = transition1.getTargetNames();
            List<String> targetNames2 = transition2.getTargetNames();
            if (targetNames1 != null || targetNames2 != null) {
                if (targetNames1 != null && targetNames2 == null || targetNames1 == null) {
                    return false;
                }
                if (targetNames1.size() != targetNames2.size()) {
                    return false;
                }
                for (int i = 0; i < targetNames2.size(); i++) {
                    if (!targetNames1.get(i).equals(targetNames2.get(i))) {
                        return false;
                    }
                }
            }
            List<Class> targetTypes1 = transition1.getTargetTypes();
            List<Class> targetTypes2 = transition2.getTargetTypes();
            if (targetTypes1 != null || targetTypes2 != null) {
                if (targetTypes1 != null && targetTypes2 == null || targetTypes1 == null) {
                    return false;
                }
                if (targetTypes1.size() != targetTypes2.size()) {
                    return false;
                }
                for (int i = 0; i < targetTypes2.size(); i++) {
                    if (!targetTypes1.get(i).equals(targetTypes2.get(i))) {
                        return false;
                    }
                }
            }
            List<View> targets1 = transition1.getTargets();
            List<View> targets2 = transition2.getTargets();
            if (targets1 != null || targets2 != null) {
                if (targets1 != null && targets2 == null || targets1 == null) {
                    return false;
                }
                if (targets1.size() != targets2.size()) {
                    return false;
                }
                for (int i = 0; i < targets2.size(); i++) {
                    if (!targets1.get(i).equals(targets2.get(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
