package co.djurayev.pincodeview.pincode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import co.djurayev.pincodeview.R;

public class PinItem extends FrameLayout {
  private static final int ANIMATION_DURATION = 400;

  private ImageView emptyDot;
  private ImageView fullDot;

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
    inflate(getContext(), R.layout.pin_item, this);

    emptyDot = findViewById(R.id.empty_dot);
    fullDot = findViewById(R.id.full_dot);
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
}
