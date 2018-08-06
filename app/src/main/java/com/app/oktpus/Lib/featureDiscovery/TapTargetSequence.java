package com.app.oktpus.Lib.featureDiscovery;

import android.app.Activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by androidspace on 24/4/17.
 */

public class TapTargetSequence {
    private final Activity activity;
    private final Queue<TapTarget> targets;
    private boolean started;

    Listener listener;
    boolean considerOuterCircleCanceled;
    boolean continueOnCancel;

    public interface Listener {
        /** Called when there are no more tap targets to display */
        void onSequenceFinish();

        /**
         * Called when moving onto the next tap target.
         * @param lastTarget The last displayed target
         * @param targetClicked Whether the last displayed target was clicked (this will always be true
         *                      unless you have set {@link #continueOnCancel(boolean)} and the user
         *                      clicks outside of the target
         */
        void onSequenceStep(TapTarget lastTarget, boolean targetClicked);

        /**
         * Called when the user taps outside of the current target, the target is cancelable, and
         * {@link #continueOnCancel(boolean)} is not set.
         * @param lastTarget The last displayed target
         */
        void onSequenceCanceled(TapTarget lastTarget);
    }

    public TapTargetSequence(Activity activity) {
        if (activity == null) throw new IllegalArgumentException("Activity is null");
        this.activity = activity;
        this.targets = new LinkedList<>();
    }

    /** Adds the given targets, in order, to the pending queue of {@link FeatureDiscovery}s */
    public TapTargetSequence targets(List<TapTarget> targets) {
        this.targets.addAll(targets);
        return this;
    }

    /** Adds the given targets, in order, to the pending queue of {@link FeatureDiscovery}s */
    public TapTargetSequence targets(TapTarget... targets) {
        Collections.addAll(this.targets, targets);
        return this;
    }

    /** Adds the given target to the pending queue of {@link FeatureDiscovery}s */
    public TapTargetSequence target(TapTarget target) {
        this.targets.add(target);
        return this;
    }

    /** Whether or not to continue the sequence when a {@link FeatureDiscovery} is canceled **/
    public TapTargetSequence continueOnCancel(boolean status) {
        this.continueOnCancel = status;
        return this;
    }

    /** Whether or not to consider taps on the outer circle as a cancellation **/
    public TapTargetSequence considerOuterCircleCanceled(boolean status) {
        this.considerOuterCircleCanceled = status;
        return this;
    }

    /** Specify the listener for this sequence **/
    public TapTargetSequence listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    /** Immediately starts the sequence and displays the first target from the queue **/
    public void start() {
        if (targets.isEmpty() || started) {
            return;
        }

        started = true;
        showNext();
    }

    void showNext() {
        try {
            TapTargetView.showFor(activity, targets.remove(), tapTargetListener);
        } catch (NoSuchElementException e) {
            // No more targets
            if (listener != null) {
                listener.onSequenceFinish();
            }
        }
    }

    private final TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            if (listener != null) {
                listener.onSequenceStep(view.target, true);
            }
            showNext();
        }

        @Override
        public void onOuterCircleClick(TapTargetView view) {
            if (considerOuterCircleCanceled) {
                onTargetCancel(view);
            }
        }

        @Override
        public void onTargetCancel(TapTargetView view) {
            super.onTargetCancel(view);
            if (continueOnCancel) {
                if (listener != null) {
                    listener.onSequenceStep(view.target, false);
                }
                showNext();
            } else {
                if (listener != null) {
                    listener.onSequenceCanceled(view.target);
                }
            }
        }
    };
}
