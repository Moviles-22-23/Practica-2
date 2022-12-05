package es.ucm.stalos.androidengine;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Screen extends ConstraintLayout {
    public Screen(@NonNull Context context, boolean isMainMenu) {
        super(context);

        // Set attributes for Constraint Layout
        setId(View.generateViewId());
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setBackgroundColor(0xFFFFFFFF);

        // Set child views
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        // Set all views up
        if (isMainMenu)
            generateMainScreen(context, constraintSet);
        else
            generateGameScreen(context, constraintSet);

        constraintSet.applyTo(this);
    }

    /**
     * Generates a layout with the game view and a banner ad in the bottom of the screen
     *
     * @param context       current activity
     * @param constraintSet constraint set to constrain the views
     */
    private void generateMainScreen(Context context, ConstraintSet constraintSet) {
        _surfaceView = new SurfaceView(context);
        _surfaceView.setId(View.generateViewId());
        addView(_surfaceView);

        _adView = new AdView(context);
        _adView.setId(View.generateViewId());
        _adView.setAdSize(AdSize.BANNER);
        _adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        addView(_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        _adView.loadAd(adRequest);

        // Constraint Surface View
        constraintSet.constrainWidth(_surfaceView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(_surfaceView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.BOTTOM, _adView.getId(), ConstraintSet.TOP);

        // Constraint Ad View
        constraintSet.constrainWidth(_adView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(_adView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(_adView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(_adView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(_adView.getId(), ConstraintSet.TOP, _surfaceView.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(_adView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

    }

    private void generateGameScreen(Context context, ConstraintSet constraintSet) {
        _surfaceView = new SurfaceView(context);
        _surfaceView.setId(View.generateViewId());
        addView(_surfaceView);

        // Constraint Surface View
        constraintSet.constrainWidth(_surfaceView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(_surfaceView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(_surfaceView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

    }

    public SurfaceView getSurfaceView() {
        return _surfaceView;
    }

    private SurfaceView _surfaceView;
    private AdView _adView;
}
