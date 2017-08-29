package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * For the first challenge, add the ability to reverse the sunset after it is completed,
 * so your user can press for a sunset, and then press a second time to get a sunrise.
 * You will need to build another AnimatorSet to do this – AnimatorSets cannot be run in reverse.
 For a second challenge, add a continuing animation to the sun. Make it pulsate with heat,
 or give it a spinning halo of rays.
 (You can use the setRepeatCount(int) method on ObjectAnimator to make your animation repeat itself.)
 Another good challenge would be to have a reflection of the sun in the water.
 Your final challenge is to add the ability to press to reverse the sunset scene while it is still happening.
 So if your user presses the scene while the sun is halfway down, it will go right back up again seamlessly.
 Likewise, if your user presses the scene while transitioning to night,
 it will smoothly transition right back to a sunrise.
 */
public class SunsetFragment extends Fragment {
    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private View mOceanView;
    private View mSunReflectionView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private int mOceanColor;
    private int mSunColor;

    private boolean mIsSunSet;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);
        mOceanView = view.findViewById(R.id.ocean);
        mSunReflectionView = view.findViewById(R.id.sunReflection);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mOceanColor = resources.getColor(R.color.sea);
        mSunColor = resources.getColor(R.color.bright_sun);

        mSceneView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mIsSunSet) {
                    startSunRiseAnimation();
                } else {
                    startSunSetAnimation();
                }
            }
        });

        return view;
    }

    private void startSunSetAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();
        float sunReflectionYStart = mSunReflectionView.getTop();
        float sunReflectionYEnd = mSkyView.getTranslationY();

        //Sunset animation
        ObjectAnimator fallAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        fallAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator =
                ObjectAnimator.ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator =
                ObjectAnimator.ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunReflectionAnimator =
                ObjectAnimator.ofFloat(mSunReflectionView, "y", sunReflectionYStart, sunReflectionYEnd)
                .setDuration(3000);
        sunReflectionAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunReflection2Animator =
                ObjectAnimator.ofInt(mSunReflectionView, "backgroundColor", mSunColor, mOceanColor)
                        .setDuration(100);
        sunReflection2Animator.setEvaluator(new ArgbEvaluator());
        //“Play heightAnimator with sunsetSkyAnimator;
        // also, play heightAnimator before nightSkyAnimator.”
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fallAnimator)
                .with(sunsetSkyAnimator)
                .with(sunReflectionAnimator)
                .with(sunReflection2Animator)
                .before(nightSkyAnimator);
        animatorSet.start();
        mIsSunSet = true;
        mSunReflectionView.setDrawingCacheBackgroundColor(mOceanColor);

    }

    private void startSunRiseAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();
        float sunReflectionYStart = mSunReflectionView.getTop();
        float sunReflectionYEnd = mSkyView.getTranslationY();
        //Sunrise animation
        ObjectAnimator riseAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYEnd, sunYStart)
                .setDuration(3000);
        riseAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunriseSkyAnimator =
                ObjectAnimator.ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                        .setDuration(3000);
        sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator blueSkyAnimator =
                ObjectAnimator.ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                        .setDuration(1500);
        blueSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunReflectionShow1Animator =
                ObjectAnimator.ofFloat(mSunReflectionView, "y", sunReflectionYEnd, sunReflectionYStart)
                        .setDuration(3000);
        sunReflectionShow1Animator.setInterpolator(new AccelerateInterpolator());

        AnimatorSet sunRiseAnimatorSet = new AnimatorSet();
        sunRiseAnimatorSet.play(riseAnimator)
                .with(sunriseSkyAnimator)
                .with(sunReflectionShow1Animator)
                .before(blueSkyAnimator);
        sunRiseAnimatorSet.start();
        mIsSunSet = false;
        mSunReflectionView.setVisibility(View.VISIBLE);
    }
}
