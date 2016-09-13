package com.example.qjm3662.newproject.Tool;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

/**
 * Created by qjm3662 on 2016/9/11 0011.
 */
public class ActivityAnim {

    public static final int ANIM_MODE_EXPLODE = 0;
    public static final int ANIM_MODE_SLIDE = 1;
    public static final int ANIM_MODE_FADE = 2;
    public static final int ANIM_MODE_SHARE = 3;

    /**
     * 带动画的Activity跳转
     *
     * @param context
     * @param mode
     */
    public static void startActivityWithAnim(Context context, Intent intent, int mode) {
        Activity activity = ((Activity) context);
        switch (mode) {
            case ANIM_MODE_EXPLODE:
                intent.putExtra("transition", "explode");
                //将原先的跳转改成如下方式
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                break;
            case ANIM_MODE_FADE:
                intent.putExtra("transition", "fade");
                //将原先的跳转改成如下方式
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                break;
            case ANIM_MODE_SLIDE:
                intent.putExtra("transition", "slide");
                //将原先的跳转改成如下方式
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                break;
            case ANIM_MODE_SHARE:
                //共享元素
//                Button share = (Button) activity.findViewById(R.id.share);
                intent.putExtra("transition", "share");
                //将原先的跳转改成如下方式，注意这里面的第三个参数决定了ActivityTwo 布局中的android:transitionName的值，它们要保持一致
//                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, share, "shareTransition").toBundle());
                break;
        }
    }

    /**
     * 带动画的Activity跳转
     *
     * @param context
     * @param mode
     */
    public static void startActivityWithAnim(Context context, Intent intent, int mode, View view) {
        Activity activity = ((Activity) context);
        switch (mode) {
            case ANIM_MODE_EXPLODE:
                intent.putExtra("transition", "explode");
                //将原先的跳转改成如下方式
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                break;
            case ANIM_MODE_FADE:
                intent.putExtra("transition", "fade");
                //将原先的跳转改成如下方式
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                break;
            case ANIM_MODE_SLIDE:
                intent.putExtra("transition", "slide");
                //将原先的跳转改成如下方式
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                break;
            case ANIM_MODE_SHARE:
                //共享元素
//                Button share = (Button) activity.findViewById(R.id.share);
                intent.putExtra("transition", "share");
                //将原先的跳转改成如下方式，注意这里面的第三个参数决定了ActivityTwo 布局中的android:transitionName的值，它们要保持一致
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, view, "shareTransition").toBundle());
                break;
        }


    }

    /**
     * 设置进入时动画
     *
     * @param context
     */
    public static void ChangAnim(Context context) {
        // 允许使用transitions
        ((Activity) context).getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        String transition = ((Activity) context).getIntent().getStringExtra("transition");
        switch (transition) {
            case "explode":
                // 设置进入时进入动画
                Explode explode = new Explode();
                explode.setDuration(500);
                ((Activity) context).getWindow().setEnterTransition(explode);

                break;

            case "slide":
                Slide slide = new Slide();
                slide.setDuration(500);
                ((Activity) context).getWindow().setEnterTransition(slide);

                break;

            case "fade":
                Fade fade = new Fade();
                fade.setDuration(500);
                ((Activity) context).getWindow().setEnterTransition(fade);
                break;
            case "share":
                break;
        }
    }
}
