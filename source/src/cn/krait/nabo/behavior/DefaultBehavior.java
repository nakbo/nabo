package cn.krait.nabo.behavior;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @author 权那他(Kraity)
 * @date 2019/8/1.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class DefaultBehavior extends FloatingActionButton.Behavior {

    private boolean isAnimateIng = false;   // 是否正在动画
    private boolean isShow = true;  // 是否已经显示

    public DefaultBehavior(Context context, AttributeSet attrs) {
        super();
    }
    //只有当返回值为true才会执行下面的方法,例如onNestedScroll
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
                ||axes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    //滑动时调用该方法
    //dxConsumed: 表示view消费了x方向的距离长度
    //dyConsumed: 表示view消费了y方向的距离长度
    //消费的距离是指实际滚动的距离
    /*
     *  coordinatorLayout - the CoordinatorLayout parent of the view this Behavior is associated with
        child - the child view of the CoordinatorLayout this Behavior is associated with
        target - the descendant view of the CoordinatorLayout performing the nested scroll
        dxConsumed - horizontal pixels consumed by the target's own scrolling operation
        dyConsumed - vertical pixels consumed by the target's own scrolling operation
        dxUnconsumed - horizontal pixels not consumed by the target's own scrolling operation, but requested by the user
        dyUnconsumed - vertical pixels not consumed by the target's own scrolling operation, but requested by the user
        type - the type of input which cause this scroll event
    */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //界面向下滑动,fab动画结束,且正在显示
        //隐藏Fab
        if ((dyConsumed>0||dyUnconsumed>0)&&!isAnimateIng&&isShow){
            AnimatorUtil.hideFab(child,new AnimateListener());
        }
        //界面向上滑动,fab动画结束,且隐藏
        //显示Fab
        else if ((dyConsumed<0||dyUnconsumed<0)&&!isAnimateIng&&!isShow){
            AnimatorUtil.showFab(child,new AnimateListener());

        }

    }
    public class AnimateListener implements  Animator.AnimatorListener {


        @Override
        public void onAnimationStart(Animator animation) {
            isAnimateIng=true;
            isShow=!isShow;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimateIng=false;

        }

        @Override
        public void onAnimationCancel(Animator animation) {


        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }

    public static class AnimatorUtil {
        private static AccelerateDecelerateInterpolator LINEAR_INTERRPLATOR =new AccelerateDecelerateInterpolator();
        static void showFab(View view, DefaultBehavior.AnimateListener... listener){
            if (listener.length!=0){
                view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(300)
                        .setInterpolator(LINEAR_INTERRPLATOR)
                        .setListener(listener[0])
                        .start();
            }else {
                view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(300)
                        .setInterpolator(LINEAR_INTERRPLATOR)
                        .start();
            }

        }
        static void hideFab(View view, DefaultBehavior.AnimateListener listener){
            view.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .setDuration(300)
                    .setInterpolator(LINEAR_INTERRPLATOR)
                    .setListener(listener)
                    .start();
        }
    }
}