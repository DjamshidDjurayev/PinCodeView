package co.djurayev.pincodeview.pincode.annotations;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({PinSteps.ENTER_CURRENT_PIN, PinSteps.ENTER_NEW_PIN, PinSteps.VERIFY_NEW_PIN})
@Retention(RetentionPolicy.SOURCE)
public @interface PinSteps {
  int ENTER_CURRENT_PIN = 0;
  int ENTER_NEW_PIN = 1;
  int VERIFY_NEW_PIN = 2;
}