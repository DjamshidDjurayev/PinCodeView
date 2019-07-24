package co.djurayev.pincodeview.pincode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import co.djurayev.pincodeview.R;

public class PinItem extends RelativeLayout {
  private static final int ANIMATION_DURATION = 400;

  private View emptyDot;
  private View fullDot;

  private Drawable fullDotDrawable = null;
  private Drawable emptyDotDrawable = null;
  private Drawable errorDotDrawable = null;

  public PinItem(@NonNull Context context) {
    super(context);
    initialize();
  }

  public PinItem(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public PinItem(@NonNull Context context,
      @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) public PinItem(@NonNull Context context,
      @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize();
  }

  private void initialize() {
    RelativeLayout.LayoutParams params =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setLayoutParams(params);
    setGravity(Gravity.CENTER);

    inflate(getContext(), R.layout.pin_item, this);

    emptyDot = findViewById(R.id.empty_dot);
    fullDot = findViewById(R.id.full_dot);

    fullDotDrawable = ContextCompat.getDrawable(getContext(), R.drawable.full_dot);
    emptyDotDrawable = ContextCompat.getDrawable(getContext(), R.drawable.empty_dot);
    errorDotDrawable = ContextCompat.getDrawable(getContext(), R.drawable.error_dot);
  }

  public void addDot(boolean animated) {
    fullDot.setTag("added");

    if (animated) {
      AnimatorSet animatorSet = new AnimatorSet();

      ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fullDot, "alpha", 0, 1)
          .setDuration(200);
      alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

      ObjectAnimator scaleXAnimator =
          ObjectAnimator.ofFloat(fullDot, "scaleX", 0.0f, 1.0f)
              .setDuration(ANIMATION_DURATION);
      scaleXAnimator.setInterpolator(new OvershootInterpolator());

      ObjectAnimator scaleYAnimator =
          ObjectAnimator.ofFloat(fullDot, "scaleY", 0.0f, 1.0f)
              .setDuration(ANIMATION_DURATION);
      scaleYAnimator.setInterpolator(new OvershootInterpolator());

      animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);

      fullDot.clearAnimation();
      animatorSet.start();
    }
    fullDot.setVisibility(VISIBLE);
  }

  public void removeDot(boolean animated) {
    if (animated) {
      AnimatorSet animatorSet = new AnimatorSet();

      ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fullDot, "alpha", 1, 0)
          .setDuration(300);
      alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

      ObjectAnimator scaleXAnimator =
          ObjectAnimator.ofFloat(fullDot, "scaleX", 1.0f, 0.0f)
              .setDuration(ANIMATION_DURATION);
      scaleXAnimator.setInterpolator(new AnticipateOvershootInterpolator());

      ObjectAnimator scaleYAnimator =
          ObjectAnimator.ofFloat(fullDot, "scaleY", 1.0f, 0.0f)
              .setDuration(ANIMATION_DURATION);
      scaleYAnimator.setInterpolator(new AnticipateOvershootInterpolator());
      scaleYAnimator.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          if (TextUtils.isEmpty(fullDot.getTag().toString())) {
            fullDot.setVisibility(GONE);
          }
        }
      });

      animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);

      fullDot.clearAnimation();
      animatorSet.start();
    }
    fullDot.setVisibility(GONE);
  }

  public void setPinSize(int pinSize) {
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullDot.getLayoutParams();
    params.width = pinSize;
    params.height = pinSize;
    fullDot.setLayoutParams(params);
    emptyDot.setLayoutParams(params);
  }

  public void setPinMargins(int... margins) {
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullDot.getLayoutParams();
    params.setMargins(margins[0], margins[1], margins[2], margins[3]);
    fullDot.setLayoutParams(params);
    emptyDot.setLayoutParams(params);
  }

  public void setDrawables(Drawable... drawables) {
    if (drawables == null) return;

    if (drawables[0] != null) {
      emptyDotDrawable = drawables[0];
      emptyDot.setBackground(emptyDotDrawable);
    }

    if (drawables[1] != null) {
      fullDotDrawable = drawables[1];
      fullDot.setBackground(fullDotDrawable);
    }

    if (drawables[2] != null) {
      errorDotDrawable = drawables[2];
    }
  }

  public void setErrorDot() {
    fullDot.setBackground(errorDotDrawable);
  }

  public void clearDot() {
    fullDot.setBackground(fullDotDrawable);
  }

  public void dispose() {
    fullDotDrawable = null;
    emptyDotDrawable = null;
    errorDotDrawable = null;
    emptyDot = null;
    fullDot = null;
  }
}
