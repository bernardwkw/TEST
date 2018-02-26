package my.com.sains.teams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import my.com.sains.teams.R;
import my.com.sains.teams.utils.MyAnimation;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    public void inspectionClick(View v){

        clickAnimation(v);
        Intent i = new Intent(LandingActivity.this, InspectionActivity.class);
        startActivity(i);
    }

    public void enquiryClick(View v){

        clickAnimation(v);
        Intent i = new Intent(LandingActivity.this, EnquiryActivity.class);
        startActivity(i);
    }

    public void syncClick(View v){

        clickAnimation(v);
        Intent i = new Intent(LandingActivity.this, SyncActivity.class);
        startActivity(i);
    }

    public void clickAnimation(View v){

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        MyAnimation myAnimation = new MyAnimation();
        myAnimation.btnBounceInterpolator(0.2, 2);
        myAnim.setInterpolator(myAnimation);

        v.startAnimation(myAnim);
    }
}
