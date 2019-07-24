package co.djurayev.pincodeview.pincode;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import co.djurayev.pincodeview.R;
import co.djurayev.pincodeview.keyboard.KeyboardContainer;
import co.djurayev.pincodeview.listeners.AnimationsListener;
import co.djurayev.pincodeview.pincode.annotations.PinModes;
import co.djurayev.pincodeview.pincode.annotations.PinSteps;
import java.util.ArrayList;
import java.util.List;

public class PinCodeView extends LinearLayout {
  @PinModes private int pinMode = PinModes.SETUP;
  @PinSteps private int currentStep = PinSteps.ENTER_NEW_PIN;

  private OnPinCodeListener onPinCodeListener;
  private Handler pinsHandler;
  private int pinIndex = 0;
  private boolean isAnimated = false;
  private StringBuilder pinValue = new StringBuilder(4);
  private StringBuilder pinOldValue = new StringBuilder(4);
  private String pinCurrentValue;
  private List<PinItem> pinItems = new ArrayList<>(4);
  private KeyboardContainer keyboardContainer;

  public PinCodeView(@NonNull Context context) {
    super(context);
    initialize();
  }

  public PinCodeView(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public PinCodeView(@NonNull Context context,
      @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) public PinCodeView(@NonNull Context context,
      @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize();
  }

  private void initialize() {
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.CENTER;

    setLayoutParams(params);
    setOrientation(HORIZONTAL);
    setGravity(Gravity.CENTER);

    pinsHandler = new Handler(Looper.getMainLooper());
    inflate(getContext(), R.layout.pin_code_view, this);

    pinItems.add(findViewById(R.id.pint_item_1));
    pinItems.add(findViewById(R.id.pint_item_2));
    pinItems.add(findViewById(R.id.pint_item_3));
    pinItems.add(findViewById(R.id.pint_item_4));
  }

  @MainThread
  public void addDot(int keyCode) {
    if (pinIndex >= pinItems.size()) return;

    PinItem item = pinItems.get(pinIndex);
    item.addDot(isAnimated);
    pinValue.append(keyCode);
    pinIndex++;

    if (pinIndex == pinItems.size()) {
      switch (pinMode) {
        case PinModes.VERIFY:
          onPinCodeListener.onPinInputCompleted(getPinValue(), pinMode);
          break;
        case PinModes.SETUP:
          // first step finished
          if (currentStep == PinSteps.ENTER_NEW_PIN) {
            pinOldValue.append(pinValue.toString());
            onPinCodeListener.onPinStepChange(pinOldValue.toString(), pinMode, currentStep);
            // clear all dots
            clearAll(false, 0);
            currentStep = PinSteps.VERIFY_NEW_PIN;
          } else if (currentStep == PinSteps.VERIFY_NEW_PIN) {
            // second step finished
            if (pinOldValue.toString().equals(pinValue.toString())) {
              onPinCodeListener.onPinInputCompleted(getPinValue(), pinMode);
            } else {
              // show error
              shakeView();
            }
          }
          break;
        case PinModes.RECOVERY:
          if (currentStep == PinSteps.ENTER_CURRENT_PIN) {
            // first step
            if (pinValue.toString().equals(pinCurrentValue)) {
              // clear all dots
              clearAll(false, 0);
              currentStep = PinSteps.ENTER_NEW_PIN;
              onPinCodeListener.onPinStepChange(pinOldValue.toString(), pinMode, currentStep);
            } else {
              // show error
              shakeView();
            }
          } else if (currentStep == PinSteps.ENTER_NEW_PIN) {
            // second step
            pinOldValue.append(pinValue.toString());
            // clear all dots
            clearAll(false, 0);
            currentStep = PinSteps.VERIFY_NEW_PIN;
            onPinCodeListener.onPinStepChange(pinOldValue.toString(), pinMode, currentStep);
          } else if (currentStep == PinSteps.VERIFY_NEW_PIN) {
            // last step
            if (pinValue.toString().equals(pinOldValue.toString())) {
              // reset current step
              currentStep = PinSteps.ENTER_CURRENT_PIN;
              onPinCodeListener.onPinInputCompleted(pinValue.toString(), pinMode);
              pinOldValue.delete(0, pinOldValue.length());
            } else {
              currentStep = PinSteps.ENTER_NEW_PIN;
              // show error
              shakeView();
              pinOldValue.delete(0, pinOldValue.length());
            }
          }
          break;
      }
    }
  }

  @MainThread
  public void removeDot(boolean animated) {
    if (pinIndex <= 0) return;

    pinIndex--;
    PinItem item = pinItems.get(pinIndex);
    item.removeDot(animated);
    pinValue.deleteCharAt(pinIndex);
  }

  public void shakeView() {
    // disable keyboard while animating
    if (keyboardContainer != null) keyboardContainer.setIsEnabled(false);

    Animation shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
    shakeAnimation.setAnimationListener(new AnimationsListener() {
      @Override public void onAnimationStart(Animation animation) {
        setErrorViews();
      }

      @Override public void onAnimationEnd(Animation animation) {
        onPinCodeListener.onPinError(pinOldValue.toString(), pinValue.toString(), pinMode);
        onPinCodeListener.onPinStepChange(pinOldValue.toString(), pinMode, currentStep);
        resetViews();

        // enable keyboard after animating
        if (keyboardContainer != null) keyboardContainer.setIsEnabled(true);
      }
    });
    startAnimation(shakeAnimation);
  }

  private void resetViews() {
    // clear all dots
    clearAll(false, 0);
  }

  private void setErrorViews() {
    // TODO set errors
  }

  public void clearAll(boolean animated, int delay) {
    for (int i = 0; i < pinItems.size(); i++) {
      pinsHandler.postDelayed(() -> removeDot(animated), delay + delay * i);
    }
  }

  public void setAnimated(boolean animated) {
    this.isAnimated = animated;
  }

  @PinModes public int getPinMode() {
    return pinMode;
  }

  public void setPinMode(@PinModes int pinMode) {
    this.pinMode = pinMode;
    setInitialCurrentStep();
  }

  private void setInitialCurrentStep() {
    if (pinMode == PinModes.SETUP) {
      currentStep = PinSteps.ENTER_NEW_PIN;
    } else if (pinMode == PinModes.RECOVERY) {
      currentStep = PinSteps.ENTER_CURRENT_PIN;
    }
  }

  public void setOnPinCodeListener(OnPinCodeListener onPinCodeListener) {
    this.onPinCodeListener = onPinCodeListener;
  }

  public String getPinValue() {
    return pinValue.toString();
  }

  public void setKeyboardContainer(KeyboardContainer keyboardContainer) {
    this.keyboardContainer = keyboardContainer;
    setupKeyBoardListener();
  }

  public void setPinCurrentValue(String value) {
    this.pinCurrentValue = value;
  }

  private void setupKeyBoardListener() {
    if (keyboardContainer != null) {
      keyboardContainer.setOnKeyboardKeyListener(keyCode -> {
        if (keyCode >= 0) {
          addDot(keyCode);
        } else {
          removeDot(isAnimated);
        }
      });
    }
  }
}
