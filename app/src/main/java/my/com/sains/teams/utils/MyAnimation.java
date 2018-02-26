package my.com.sains.teams.utils;

/**
 * Created by User on 9/11/2017.
 */

public class MyAnimation implements android.view.animation.Interpolator{

    private double mAmplitude = 1;
    private double mFrequency = 10;

    public void btnBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
