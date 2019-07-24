package co.djurayev.pincodeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import co.djurayev.pincodeview.keyboard.KeyboardContainer;
import co.djurayev.pincodeview.pincode.OnPinCodeListener;
import co.djurayev.pincodeview.pincode.PinCodeView;
import co.djurayev.pincodeview.pincode.annotations.PinModes;
import co.djurayev.pincodeview.pincode.annotations.PinSteps;

public class MainActivity extends AppCompatActivity {
  private TextView pinTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    KeyboardContainer keyboardContainer = findViewById(R.id.keyboard_container);
    pinTitle = findViewById(R.id.pin_title);

    PinCodeView pinCodeView = findViewById(R.id.pin_code_view);
    pinCodeView.setAnimated(true);
    pinCodeView.setPinMode(PinModes.RECOVERY);
    pinCodeView.setPinCurrentValue("1234");
    pinCodeView.setKeyboardContainer(keyboardContainer);
    pinCodeView.setOnPinCodeListener(new OnPinCodeListener() {
      @Override public void onPinInputCompleted(String pin, int mode) {
        // pin success
        pinTitle.setText("Success");
      }

      @Override public void onPinStepChange(String oldPin, int mode, int step) {
        // pin step change
        if (mode == PinModes.RECOVERY) {
          if (step == PinSteps.ENTER_NEW_PIN) {
            pinTitle.setText("Enter new pin");
          } else if (step == PinSteps.VERIFY_NEW_PIN) {
            pinTitle.setText("Verify new pin");
          }
        }
      }

      @Override public void onPinError(String oldPin, String pin, int mode) {
        // pin error
        //pinTitle.setText("Incorrect pin code");
      }
    });
    keyboardContainer.displayKeyboard();
  }
}
