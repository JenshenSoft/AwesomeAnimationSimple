package com.jenshen.awesomeanimation.util.transition;


import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;

import java.util.List;

public class TransitionUtil {

    public static boolean equalsTransitions(Transition transition1, Transition transition2) {
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
        boolean hasProperties1 = transitionProperties1 != null && transitionProperties1.length != 0;
        boolean hasProperties2 = transitionProperties2 != null && transitionProperties2.length != 0;
        if (hasProperties1 && hasProperties2) {
            if (transitionProperties1.length != transitionProperties2.length) {
                return false;
            }
            for (int i = 0; i < transitionProperties2.length; i++) {
                if (!transitionProperties1[i].equals(transitionProperties2[i])) {
                    return false;
                }
            }
        } else if (hasProperties1 || hasProperties2) {
            return false;
        }

        List<Integer> targetIds1 = transition1.getTargetIds();
        List<Integer> targetIds2 = transition2.getTargetIds();
        boolean hasTargetId1 = targetIds1 != null && !targetIds1.isEmpty();
        boolean hasTargetId2 = targetIds2 != null && !targetIds2.isEmpty();
        if (hasTargetId1 && hasTargetId2) {
            if (targetIds1.size() != targetIds2.size()) {
                return false;
            }
            for (int i = 0; i < targetIds2.size(); i++) {
                if (!targetIds1.get(i).equals(targetIds2.get(i))) {
                    return false;
                }
            }
        } else if (hasTargetId1 || hasTargetId2) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isTransitionSet1 = transition1 instanceof TransitionSet;
            boolean isTransitionSet2 = transition2 instanceof TransitionSet;
            if (isTransitionSet1 && isTransitionSet2) {
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
            } else if (isTransitionSet1 || isTransitionSet2) {
                return false;
            }

            List<String> targetNames1 = transition1.getTargetNames();
            List<String> targetNames2 = transition2.getTargetNames();
            boolean hasTargetNames1 = targetNames1 != null && !targetNames1.isEmpty();
            boolean hasTargetNames2 = targetNames2 != null && !targetNames2.isEmpty();
            if (hasTargetNames1 && hasTargetNames2) {
                if (targetNames1.size() != targetNames2.size()) {
                    return false;
                }
                for (int i = 0; i < targetNames2.size(); i++) {
                    if (!targetNames1.get(i).equals(targetNames2.get(i))) {
                        return false;
                    }
                }
            } else if (hasTargetNames1 || hasTargetNames2) {
                return false;
            }

            List<Class> targetTypes1 = transition1.getTargetTypes();
            List<Class> targetTypes2 = transition2.getTargetTypes();
            boolean hasTargetTypes1 = targetTypes1 != null && !targetTypes1.isEmpty();
            boolean hasTargetTypes2 = targetTypes2 != null && !targetTypes2.isEmpty();
            if (hasTargetTypes1 && hasTargetTypes2) {
                if (targetTypes1.size() != targetTypes2.size()) {
                    return false;
                }
                for (int i = 0; i < targetTypes2.size(); i++) {
                    if (!targetTypes1.get(i).equals(targetTypes2.get(i))) {
                        return false;
                    }
                }
            } else if (hasTargetTypes1 || hasTargetTypes2) {
                return false;
            }

            List<View> targets1 = transition1.getTargets();
            List<View> targets2 = transition2.getTargets();
            boolean hasTarget1 = targets1 != null && !targets1.isEmpty();
            boolean hasTarget2 = targets2 != null && !targets2.isEmpty();
            if (hasTarget1 && hasTarget2) {
                if (targets1.size() != targets2.size()) {
                    return false;
                }
                for (int i = 0; i < targets2.size(); i++) {
                    if (!targets1.get(i).equals(targets2.get(i))) {
                        return false;
                    }
                }
            } else if (hasTarget1 || hasTarget2) {
                return false;
            }
        }
        return true;
    }
}
