package co.djurayev.pincodeview;

import android.content.Context;
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

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(LocaleHelper.setLocale(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    KeyboardContainer keyboardContainer = findViewById(R.id.keyboard_container);
    pinTitle = findViewById(R.id.pin_title);

    PinCodeView pinCodeView = findViewById(R.id.pin_code_view);
    pinCodeView.setAnimated(true);
    pinCodeView.setPinMode(PinModes.VERIFY);
    pinCodeView.setPinCurrentValue("1234");
    pinCodeView.setKeyboardContainer(keyboardContainer);
    pinCodeView.setOnPinCodeListener(new OnPinCodeListener() {
      @Override public void onPinInputCompleted(String pin, int mode) {
        // pin success
        pinTitle.setText("Success");
      }

      @Override public void onPinStepChange(String oldPin, int mode, int step) {
        // pin step change
        switch (mode) {
          case PinModes.VERIFY:
            break;
          case PinModes.SETUP:
            if (step == PinSteps.VERIFY_NEW_PIN) {
              pinTitle.setText("Verify new pin");
            }
            break;
          case PinModes.RECOVERY:
            if (step == PinSteps.ENTER_NEW_PIN) {
              pinTitle.setText("Enter new pin");
            } else if (step == PinSteps.VERIFY_NEW_PIN) {
              pinTitle.setText("Verify new pin");
            }
            break;
        }
      }

      @Override public void onPinError(String oldPin, String pin, int mode) {
      }
    });

    keyboardContainer.displayKeyboard();

    findViewById(R.id.button).setOnClickListener(v -> {
      LocaleHelper.setLocale(MainActivity.this, LocaleHelper.ENGLISH);


      Prefs.setAppLanguage("en");
      recreate();
    });
  }
}
