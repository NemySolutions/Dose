package sg.dose;

/**
 * Created by Nvcwk on 26/7/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Fx {



    public static void slide_down(Context ctx, View v){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);

            }
        }
    }

    public static void slide_up(Context ctx, View v){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);

        if(a != null){

            a.reset();
            if(v != null){

                v.clearAnimation();

                v.startAnimation(a);
            }
        }
    }


}