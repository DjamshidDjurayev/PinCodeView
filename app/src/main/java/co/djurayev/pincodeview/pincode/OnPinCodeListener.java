package co.djurayev.pincodeview.pincode;

import co.djurayev.pincodeview.pincode.annotations.PinModes;
import co.djurayev.pincodeview.pincode.annotations.PinSteps;

public interface OnPinCodeListener {
  void onPinInputCompleted(String pin, @PinModes int mode);

  void onPinStepChange(String oldPin, @PinModes int mode, @PinSteps int step);

  void onPinError(String oldPin, String pin, @PinModes int mode);
}
