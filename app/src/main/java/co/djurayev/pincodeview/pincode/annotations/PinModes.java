package co.djurayev.pincodeview.pincode.annotations;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({PinModes.VERIFY, PinModes.SETUP, PinModes.RECOVERY})
@Retention(RetentionPolicy.SOURCE)
public @interface PinModes {
  int VERIFY = 0;
  int SETUP = 1;
  int RECOVERY = 2;
}
