package com.jsb.calculator.KeybordService;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsb.calculator.activity.CalculatorActivity;
import com.jsb.calculator.activity.HistoryActivity;
import com.jsb.calculator.Classes.Fonts;
import com.jsb.calculator.activity.HomeActivity;
import com.jsb.calculator.databinding.KeyboardBinding;
import com.jsb.calculator.R;
import com.jsb.calculator.service.FloatingCalculator;
import com.jsb.calculator.modules.CalculatorHistory;

import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {


    //fix input type

    private int inputTypeee = 0;
    private String beforCursorTextt;
    private KeyboardBinding binding;
    TextView curBt;
    HorizontalScrollView currencyLl;
    String tt = "", ttR = "";

    CountDownTimer cdt;
    int isCapsOn = 0;
    SharedPreferences sharedPreferences;
    boolean lastCapsOnOn = false;
    private String keyboardEtText;
    private int whatSuggestion = 0;
    private int actionId = 0;
    private int onCapsClick = 0;
    private ClipboardManager clipboard;


    private String keya = "a";
    private String keyb = "b";
    private String keyc = "c";
    private String keyd = "d";
    private String keye = "e";
    private String keyf = "f";
    private String keyg = "g";
    private String keyh = "h";
    private String keyi = "i";
    private String keyj = "j";
    private String keyk = "k";
    private String keyl = "l";
    private String keym = "m";
    private String keyn = "n";
    private String keyo = "o";
    private String keyp = "p";
    private String keyq = "q";
    private String keyr = "r";
    private String keys = "s";
    private String keyt = "t";
    private String keyu = "u";
    private String keyv = "v";
    private String keyw = "w";
    private String keyx = "x";
    private String keyy = "y";
    private String keyz = "z";

    private String keyA = "A";
    private String keyB = "B";
    private String keyC = "C";
    private String keyD = "D";
    private String keyE = "E";
    private String keyF = "F";
    private String keyG = "G";
    private String keyH = "H";
    private String keyI = "I";
    private String keyJ = "J";
    private String keyK = "K";
    private String keyL = "L";
    private String keyM = "M";
    private String keyN = "N";
    private String keyO = "O";
    private String keyP = "P";
    private String keyQ = "Q";
    private String keyR = "R";
    private String keyS = "S";
    private String keyT = "T";
    private String keyU = "U";
    private String keyV = "V";
    private String keyW = "W";
    private String keyX = "X";
    private String keyY = "Y";
    private String keyZ = "Z";

    private char currencySymbol = "₹".charAt(0);




    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        actionId = attribute.imeOptions;


        inputTypeee = attribute.inputType;
        switch (inputTypeee & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_PHONE:
                whatSuggestion = 1;
                break;
            case InputType.TYPE_CLASS_DATETIME:
                whatSuggestion = 2;
                break;
            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                whatSuggestion = 0;



        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

        if (isMyServiceRunning(FloatingCalculator.class)){
            binding.btOFCal.setImageResource(R.drawable.ic_floating_cal_off);
        }else {
            binding.btOFCal.setImageResource(R.drawable.ic_floating_cal);
        }

        onKey(342342, new int[0]);

        if (keyboardEtText == null || keyboardEtText.isEmpty()){
            setActionLL();
            isCapsOn = 1;
            binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps2);
            setCaps(true);
        }else {
            setActionLL();
        }

        showCKeyboard();

        binding.numPadKeyboard.btKeyEnterNum.setOnClickListener(view -> onKey(23141, new int[0]));
        binding.normalKeyboard.btKeyEnter.setOnClickListener(view -> onKey(23141, new int[0]));
        int  inputTypeee = (this.inputTypeee & InputType.TYPE_MASK_FLAGS);



        switch (actionId & EditorInfo.IME_MASK_ACTION){
            case EditorInfo.IME_ACTION_SEND:
                binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_send);
                break;
            case EditorInfo.IME_ACTION_DONE:
                if (InputType.TYPE_TEXT_FLAG_MULTI_LINE == inputTypeee){
                    binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_new_line_arrow);
                    binding.normalKeyboard.btKeyEnter.setOnClickListener(view -> onKey(Keyboard.KEYCODE_DONE, new int[0]));
                }else {
                    binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_done);
                }

                break;
            case EditorInfo.IME_ACTION_NEXT:
                if (InputType.TYPE_TEXT_FLAG_MULTI_LINE == inputTypeee){
                    binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_new_line_arrow);
                    binding.normalKeyboard.btKeyEnter.setOnClickListener(view -> onKey(Keyboard.KEYCODE_DONE, new int[0]));
                }else {
                    binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_arrow_right_alt);
                }
                break;
            case EditorInfo.IME_ACTION_GO:
                binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_arrow_forward);
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_search);
                break;
            case EditorInfo.IME_ACTION_PREVIOUS:
                if (InputType.TYPE_TEXT_FLAG_MULTI_LINE == inputTypeee){
                    binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_new_line_arrow);
                    binding.normalKeyboard.btKeyEnter.setOnClickListener(view -> onKey(Keyboard.KEYCODE_DONE, new int[0]));
                }else {
                    binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_arroe_left_alt);
                }
                break;
            /*case EditorInfo.IME_FLAG_NO_ENTER_ACTION:
                binding.normalKeyboard.btKeyEnter.setVisibility(View.INVISIBLE);
                break;
            case EditorInfo.IME_FLAG_NO_ACCESSORY_ACTION:
                binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_done);
                break;*/
            default:
                binding.normalKeyboard.btKeyEnter.setImageResource(R.drawable.ic_new_line_arrow);
        }


        //binding.normalKeyboard.btTest.setText("android ID: "+whatSuggestion+" daa: "+(info.inputType & InputType.TYPE_MASK_FLAGS));



        storeChar("\n"+System.currentTimeMillis()+"\n");

    }
    TextToSpeech tts;
    boolean isTextToSpeech = true;


    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        CharSequence currentText;

        CharSequence beforCursorText;
        CharSequence afterCursorText;
        try {
            currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;

            beforCursorText = ic.getTextBeforeCursor(currentText.length(), 0);
            afterCursorText = ic.getTextAfterCursor(currentText.length(), 0);

            keyboardEtText = beforCursorText.toString()+afterCursorText.toString();
            beforCursorTextt = beforCursorText.toString();
        }catch (Exception e){
            return;
        }


        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    ic.deleteSurroundingText(1, 0);
                } else {
                    ic.commitText("", 1);
                }
                storeChar(" ");
                break;
            case 23141:
                ic.performEditorAction(actionId);
                break;
            case 2213:
                if (ic.getSelectedText(0) != null && !ic.getSelectedText(0).toString().isEmpty()){
                    ClipData clip = ClipData.newPlainText("jsb:"+System.currentTimeMillis(), ic.getSelectedText(0));
                    clipboard.setPrimaryClip(clip);
                    showToast("Text copied");
                }else {
                    if (keyboardEtText.isEmpty()) {
                        showToast("Nothing selected");
                    }else {
                        ic.setSelection(currentText.length(), 0);
                        ClipData clip = ClipData.newPlainText("jsb:"+System.currentTimeMillis(), keyboardEtText);
                        clipboard.setPrimaryClip(clip);
                        showToast("Text copied");
                    }
                }
                break;
            case 342342:
                break;
            case 332211:
                if (isTextToSpeech){
                    if (!keyboardEtText.isEmpty()){
                        tts.speak(keyboardEtText,TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                }
                return;
            case 31232:
                ic.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case 3443:
                ic.commitText(tt,1);
                storeChar(tt);
                break;
            case 34434:
                ic.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                if (keyboardEtText.contains(" ")){
                    keyboardEtText = keyboardEtText.substring(0, keyboardEtText.lastIndexOf(" ")+1)+tt;

                }else {
                    keyboardEtText = tt;
                }
                ic.commitText(keyboardEtText,1);
                storeChar(tt);
                break;
            case 33223:
                ic.commitText(" ",1);
                storeChar(" ");
                break;
            default:
                char code = (char)primaryCode;
                ic.commitText(String.valueOf(code),1);
                storeChar(String.valueOf(code));
        }

        try {
            currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;
            beforCursorText = ic.getTextBeforeCursor(currentText.length(), 0);
            afterCursorText = ic.getTextAfterCursor(currentText.length(), 0);
            keyboardEtText = beforCursorText.toString()+afterCursorText.toString();
            beforCursorTextt = beforCursorText.toString();

        }catch (Exception e){
            return;
        }


        setCapsOff();
    }

    private void setCapsOff() {
        int  inputTypeee = (this.inputTypeee & InputType.TYPE_MASK_FLAGS);
        Log.e("tasting", "setCapsOff: "+beforCursorTextt );
        if (keyboardEtText.isEmpty() || beforCursorTextt.isEmpty()){
            isCapsOn = 1;
            setCaps(true, true);
            return;
        }
        Log.e("tasting", "inputTypeee: "+inputTypeee );
        if (InputType.TYPE_TEXT_FLAG_CAP_WORDS == inputTypeee){
            if (beforCursorTextt.endsWith(" ")){
                isCapsOn = 1;
                setCaps(true, true);
                return;
            }
        }else if (InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS == inputTypeee){
            isCapsOn = 1;
            setCaps(true, true);
            return;
        }else if (InputType.TYPE_TEXT_FLAG_CAP_SENTENCES == inputTypeee){
            if (beforCursorTextt.endsWith(". ")){
                isCapsOn = 1;
                setCaps(true, true);
                return;
            }
        }

        if (isCapsOn == 1){
            setCaps(false, false);
            isCapsOn = 0;
        }

    }

    private void showToast(String s) {
        binding.toast.setVisibility(View.VISIBLE);
        binding.toast.setAlpha(1f);
        binding.toast.setText(s);
        binding.toast.animate().alpha(1.0f).setDuration(2500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                binding.toast.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {}
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        binding.toast.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {}
                    @Override
                    public void onAnimationRepeat(Animator animator) {}
                });
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });


    }


    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public View onCreateInputView() {
        binding = KeyboardBinding.inflate(getLayoutInflater());
        sharedPreferences = getSharedPreferences("com.jsb.calculator", MODE_PRIVATE);
        binding.calculatedTextEt.setKeyListener(null);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Users").child(deviceId).child("status").setValue(true);
        myRef.child("Users").child(deviceId).child("status").onDisconnect().setValue(ServerValue.TIMESTAMP);

        tts = new TextToSpeech(SimpleIME.this, i -> {
            if (i == TextToSpeech.SUCCESS) {
                isTextToSpeech = true;
            }
        });
        tts.setLanguage(Locale.US);


        onClickBtNumber(binding.calculatedTextEt, binding.bt1, "1", view -> {
            fastType(49, binding.bt1, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt2, "2", view -> {
            fastType(50, binding.bt2, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt3, "3", view -> {
            fastType(51, binding.bt3, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt4, "4", view -> {
            fastType(52, binding.bt4, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt5, "5", view -> {
            fastType(53, binding.bt5, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt6, "6", view -> {
            fastType(54, binding.bt6, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt7, "7", view -> {
            fastType(55, binding.bt7, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt8, "8", view -> {
            fastType(56, binding.bt8, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt9, "9", view -> {
            fastType(57, binding.bt9, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt0, "0", view -> {
            fastType(48, binding.bt0, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.bt00, "00", view -> {
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            imeManager.showInputMethodPicker();
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.btMultiply, "×", view -> {
            fastType(215, binding.btMultiply, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.btDivide, "÷", view -> {
            fastType(247, binding.btDivide, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.btDot, ".", view -> {
            fastType(46, binding.btDot, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.btMinus, "-", view -> {
            fastType(45, binding.btMinus, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.btPercentage, "%", view -> {
            fastType(37, binding.btPercentage, 500);
            return true;
        });
        onClickBtNumber(binding.calculatedTextEt, binding.btPlus, "+", view -> {
            fastType(43, binding.btPlus, 500);
            return true;
        });

        onClickBtNumber(binding.calculatedTextEt, binding.btB1, "(", view -> {
            fastType(40, binding.btB1, 500);
            return true;
        });

        onClickBtNumber(binding.calculatedTextEt, binding.btB2, ")", view -> {
            fastType(41, binding.btB2, 500);
            return true;
        });


        binding.btEqualsTo.setOnClickListener(view -> {
            String aa = binding.calculatedTextEt.getText().toString();
            if (!aa.isEmpty()){

                if (aa.endsWith("+") || aa.endsWith("%") || aa.endsWith(".") || aa.endsWith("×") || aa.endsWith("÷") || aa.endsWith("-")){
                    aa = method(aa);
                }
                if (aa.equals("-")){
                    return;
                }
                String valll = aa;
                String string = aa
                        .replaceAll("x","*")
                        .replaceAll("×","*")
                        .replaceAll("÷","/")
                        .replaceAll("%","/100");

                Expression e = new Expression(string);
                double c = e.calculate();
                String calculate = String.valueOf(c);
                if (calculate.endsWith(".0")){
                    calculate = method(calculate);
                    calculate = method(calculate);
                }else if (calculate.endsWith(".00")){
                    calculate = method(calculate);
                    calculate = method(calculate);
                    calculate = method(calculate);
                }
                CalculatorHistory calHis = new CalculatorHistory(System.currentTimeMillis(), c, valll, 1);

                saveCalHisList(calHis);
                binding.calculatedTextEt.setText(calculate);
            }
        });

        binding.btRup.setText(String.valueOf(currencySymbol));
        binding.btRup.setOnClickListener(view -> onKey(currencySymbol, new int[0]));

        binding.btRup.setOnLongClickListener(view -> {
            binding.currencyLl.setVisibility(View.VISIBLE);
            curBt = binding.btRup;
            currencyLl = binding.currencyLl;
            //₿ ¥ £ ₱ € $ ₹
            setCur( binding.btSBit1,  "₿");
            setCur(binding.btSBri1,  "£");
            setCur(binding.btSEuro1,  "€");
            setCur(binding.btSPhi1,  "₱");
            setCur(binding.btSRup1,  "₹");
            setCur(binding.btSUsd1,  "$");
            setCur(binding.btSYen1,  "¥");


            return true;
        });


        binding.btBackspace.setOnClickListener(view -> {
            if (binding.calculatedTextEt.getText().toString().isEmpty()){
                onKey(Keyboard.KEYCODE_DELETE, new int[0]);
            }else {
                binding.calculatedTextEt.setText(method(binding.calculatedTextEt.getText().toString()));
                binding.calculatedTextEt.setSelection(binding.calculatedTextEt.getText().toString().length());
                liveCal();
            }
        });

        binding.btBackspace.setOnLongClickListener(view -> {
            cdt = new CountDownTimer(3000000*500, 50) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("tasting", "Countdown seconds remaining: " + millisUntilFinished / 1000);
                    if (!binding.btBackspace.isPressed()){
                        cdt.cancel();
                    }else {
                        if (binding.calculatedTextEt.getText().toString().isEmpty()){
                            onKey(Keyboard.KEYCODE_DELETE, new int[0]);
                        }else {
                            binding.calculatedTextEt.setText(method(binding.calculatedTextEt.getText().toString()));
                            binding.calculatedTextEt.setSelection(binding.calculatedTextEt.getText().toString().length());
                            liveCal();
                        }
                    }
                }

                @Override
                public void onFinish() {
                    Log.i("tasting", "Timer finished");
                }
            };
            cdt.start();
            return true;
        });


        binding.btMB.setOnClickListener(view -> onKey(Keyboard.KEYCODE_DELETE, new int[0]));

        binding.btMB.setOnLongClickListener(view -> {
            fastType(Keyboard.KEYCODE_DELETE, binding.btMB, 10);
            return true;
        });


        binding.btClear.setOnClickListener(view -> {
            if (binding.calculatedTextEt.getText().toString().isEmpty()){
                onKey(31232, new int[0]);
            }else {
                binding.calculatedTextEt.setText("");
                binding.liveCalculatedText.setText("00");
            }
        });

        binding.btClear.setOnLongClickListener(view -> {
            onKey(31232, new int[0]);
            return true;
        });

        binding.bt3342.setOnClickListener(view -> showToast("Coming soon"));
        binding.btHistory.setOnClickListener(view -> {
            Intent intent = new Intent(SimpleIME.this, HistoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        binding.submitBt.setOnClickListener(view -> {
            String aa = binding.calculatedTextEt.getText().toString();
            if (!aa.isEmpty()){
                if (aa.endsWith("+") || aa.endsWith("%") || aa.endsWith(".") || aa.endsWith("×") || aa.endsWith("÷") || aa.endsWith("-")){
                    aa = method(aa);
                }
                if (aa.equals("-")){
                    return;
                }
                String valll = aa;
                String string = aa
                        .replaceAll("x","*")
                        .replaceAll("×","*")
                        .replaceAll("÷","/")
                        .replaceAll("%","/100");

                Expression e = new Expression(string);
                double c = e.calculate();
                tt = String.valueOf(c);
                if (tt.endsWith(".0")){
                    tt = method(tt);
                    tt = method(tt);
                }else if (tt.endsWith(".00")){
                    tt = method(tt);
                    tt = method(tt);
                    tt = method(tt);
                }
                onKey(3443, new int[0]);
                binding.calculatedTextEt.setText("");
                binding.liveCalculatedText.setText("00");

                CalculatorHistory calHis = new CalculatorHistory(System.currentTimeMillis(), c, valll, 1);

                saveCalHisList(calHis);
            }
        });



        binding.btOCal.setOnClickListener(view -> {
            Intent intent = new Intent(SimpleIME.this, CalculatorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.btOFCal.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(SimpleIME.this)) {
                    Intent intent = new Intent(SimpleIME.this, HomeActivity.class);
                    sharedPreferences.edit().putString("call", "reqPermission").apply();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    startServicee();
                }
            }else{
                startServicee();
            }

        });
        binding.normalKeyboardLL.setVisibility(View.VISIBLE);
        binding.calKeyBoard.setVisibility(View.GONE);


        binding.btABCc.setOnClickListener(view -> {
            showNormalKeyboard();
        });
        binding.bt12233.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumPadKeyboard();
            }
        });

        setNormalKeyBord();
        setNumKeyBorad();

        return binding.getRoot();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setNumKeyBorad() {
        binding.numPadKeyboard.btKeyNum1.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum2.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum3.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum4.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum5.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum6.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum7.setOnClickListener(typeKey);

        binding.numPadKeyboard.btKeyNum8.setOnClickListener(typeKey(" "));

        binding.numPadKeyboard.btKeyNum9.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum10.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum11.setOnClickListener(typeKey);


        binding.numPadKeyboard.btKeyNum12.setOnClickListener(view -> onKey(Keyboard.KEYCODE_DELETE, new int[0]));

        binding.numPadKeyboard.btKeyNum12.setOnLongClickListener(view -> {
            fastType(Keyboard.KEYCODE_DELETE, binding.numPadKeyboard.btKeyNum12, 50);
            return true;
        });

        binding.numPadKeyboard.btNormalKeyFromNum.setOnClickListener(view -> showNormalKeyboard());

        binding.numPadKeyboard.btNumCCaall.setOnClickListener(view -> showCKeyboard());

        binding.numPadKeyboard.btKeyNum13.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum14.setOnClickListener(typeKey);
        binding.numPadKeyboard.btKeyNum15.setOnClickListener(typeKey);




        binding.numPadKeyboard.actionsLlNum2.addView(newText("+", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("-", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("/", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText(":", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("=", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("(", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText(")", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("'", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("\"", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("&", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("%", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("#", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("@", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("!", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("?", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("*", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("^", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("[", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("]", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("{", typeKey));
        binding.numPadKeyboard.actionsLlNum2.addView(newText("}", typeKey));

    }

    private TextView newText(String text, View.OnClickListener onClickListener){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(SimpleIME.this);
        textView.setTextSize(20f);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setPadding(20, 0, 20, 0);
        textView.setBackgroundResource(R.drawable.button_r1);
        textView.setLayoutParams(layoutParams);

        textView.setOnClickListener(onClickListener);

        return textView;
    }

    private void showCKeyboard(){
        binding.normalKeyboardLL.setVisibility(View.GONE);
        binding.numPadKeyboardLL.setVisibility(View.GONE);
        binding.calKeyBoard.setVisibility(View.VISIBLE);
    }

    private void showNormalKeyboard(){
        binding.normalKeyboardLL.setVisibility(View.VISIBLE);
        binding.calKeyBoard.setVisibility(View.GONE);
        binding.numPadKeyboardLL.setVisibility(View.GONE);
    }

    private void showNumPadKeyboard(){
        binding.numPadKeyboard.actionsLlNum.setVisibility(View.VISIBLE);
        if (whatSuggestion == 1){
            binding.numPadKeyboard.btNormalKeyFromNum.setVisibility(View.GONE);
            binding.numPadKeyboard.btKeyNum13.setText(",");
        }else if (whatSuggestion == 2){
            binding.numPadKeyboard.btKeyNum13.setText("/");
            binding.numPadKeyboard.btNormalKeyFromNum.setVisibility(View.GONE);
        }else {
            binding.numPadKeyboard.btKeyNum13.setText(",");
            binding.numPadKeyboard.btNormalKeyFromNum.setVisibility(View.VISIBLE);
        }
        binding.normalKeyboardLL.setVisibility(View.GONE);
        binding.calKeyBoard.setVisibility(View.GONE);
        binding.numPadKeyboardLL.setVisibility(View.VISIBLE);
    }



    private void setActionLL(){
        binding.normalKeyboard.actionsLl.setVisibility(View.VISIBLE);
        binding.normalKeyboard.suggestionLl.setVisibility(View.GONE);

        binding.normalKeyboard.switchBt.setImageResource(R.drawable.ic_keyboard_arrow_left);
    }



    private void setNormalKeys(){



        binding.normalKeyboard.btComa.setText(",");
        binding.normalKeyboard.btDot2.setText(".");

        binding.normalKeyboard.btEmoji.setTextSize(20f);
        binding.normalKeyboard.btEmoji.setText("\uD83D\uDE00");
        binding.normalKeyboard.btEmoji.setOnClickListener(view -> {
            showToast("Coming soon");
        });


        binding.normalKeyboard.btQ123.setText("?123");

        binding.normalKeyboard.btKey27.setVisibility(View.GONE);
        setCaps(isCapsOn == 1 || isCapsOn == 2);

        if (isCapsOn == 0){
            binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps1);
        }else if (isCapsOn == 1){
            binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps2);
        }else if (isCapsOn == 2){
            binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps3);
        }
        onCapsClick = 0;
    }

    private void setQ123Keys(){
        binding.normalKeyboard.btKey1.setText("1");
        binding.normalKeyboard.btKey2.setText("2");
        binding.normalKeyboard.btKey3.setText("3");
        binding.normalKeyboard.btKey4.setText("4");
        binding.normalKeyboard.btKey5.setText("5");
        binding.normalKeyboard.btKey6.setText("6");
        binding.normalKeyboard.btKey7.setText("7");
        binding.normalKeyboard.btKey8.setText("8");
        binding.normalKeyboard.btKey9.setText("9");
        binding.normalKeyboard.btKey10.setText("0");
        binding.normalKeyboard.btKey27.setText("@");
        binding.normalKeyboard.btKey11.setText("#");
        binding.normalKeyboard.btKey12.setText(String.valueOf(currencySymbol));
        binding.normalKeyboard.btKey13.setText("_");
        binding.normalKeyboard.btKey14.setText("&");
        binding.normalKeyboard.btKey15.setText("-");
        binding.normalKeyboard.btKey16.setText("+");
        binding.normalKeyboard.btKey17.setText("(");
        binding.normalKeyboard.btKey18.setText(")");
        binding.normalKeyboard.btKey19.setText("/");
        binding.normalKeyboard.btKey20.setText("*");
        binding.normalKeyboard.btKey21.setText("\"");
        binding.normalKeyboard.btKey22.setText("'");
        binding.normalKeyboard.btKey23.setText(":");
        binding.normalKeyboard.btKey24.setText(";");
        binding.normalKeyboard.btKey25.setText("!");
        binding.normalKeyboard.btKey26.setText("?");

        binding.normalKeyboard.btEmoji.setTextSize(14f);
        binding.normalKeyboard.btEmoji.setText("12\n34");
        binding.normalKeyboard.btEmoji.setOnClickListener(view -> {
            showNumPadKeyboard();
        });


        binding.normalKeyboard.btQ123.setText("ABC");

        binding.normalKeyboard.btComa.setText(",");
        binding.normalKeyboard.btDot2.setText(".");

        binding.normalKeyboard.btKey27.setVisibility(View.VISIBLE);
        binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_text_spc);
        onCapsClick = 1;
    }

    private void setSPCKeys(){
        //₿ ¥ £ ₱ € $ ₹
        binding.normalKeyboard.btKey1.setText("~");
        binding.normalKeyboard.btKey2.setText("`");
        binding.normalKeyboard.btKey3.setText("|");
        binding.normalKeyboard.btKey4.setText("•");
        binding.normalKeyboard.btKey5.setText("√");
        binding.normalKeyboard.btKey6.setText("π");
        binding.normalKeyboard.btKey7.setText("÷");
        binding.normalKeyboard.btKey8.setText("×");
        binding.normalKeyboard.btKey9.setText("=");
        binding.normalKeyboard.btKey10.setText("^");
        binding.normalKeyboard.btKey27.setText("$");
        binding.normalKeyboard.btKey11.setText("₹");
        binding.normalKeyboard.btKey12.setText("€");
        binding.normalKeyboard.btKey13.setText("¥");
        binding.normalKeyboard.btKey14.setText("£");
        binding.normalKeyboard.btKey15.setText("₿");
        binding.normalKeyboard.btKey16.setText("°");
        binding.normalKeyboard.btKey17.setText("{");
        binding.normalKeyboard.btKey18.setText("}");
        binding.normalKeyboard.btKey19.setText("\\");
        binding.normalKeyboard.btKey20.setText("%");
        binding.normalKeyboard.btKey21.setText("©");
        binding.normalKeyboard.btKey22.setText("®");
        binding.normalKeyboard.btKey23.setText("™");
        binding.normalKeyboard.btKey24.setText("✓");
        binding.normalKeyboard.btKey25.setText("[");
        binding.normalKeyboard.btKey26.setText("]");

        binding.normalKeyboard.btComa.setText("<");
        binding.normalKeyboard.btDot2.setText(">");

        binding.normalKeyboard.btEmoji.setTextSize(14f);
        binding.normalKeyboard.btEmoji.setText("12\n34");
        binding.normalKeyboard.btEmoji.setOnClickListener(view -> {
            showNumPadKeyboard();
        });

        binding.normalKeyboard.btKey27.setVisibility(View.VISIBLE);
        binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_text_q123);
        onCapsClick = 2;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setNormalKeyBord() {

        setNormalKeys();

        binding.normalKeyboard.suggestionTv1.setOnClickListener(view -> {
            tt = binding.normalKeyboard.suggestionTv1.getText().toString();
            onKey(34434, new int[0]);
        });

        binding.normalKeyboard.suggestionTv2.setOnClickListener(view -> {
            tt = binding.normalKeyboard.suggestionTv2.getText().toString();
            onKey(34434, new int[0]);
        });

        binding.normalKeyboard.suggestionTv3.setOnClickListener(view -> {
            tt = binding.normalKeyboard.suggestionTv3.getText().toString();
            onKey(34434, new int[0]);
        });

        binding.normalKeyboard.btCCaall.setOnClickListener(view -> {
            showCKeyboard();
        });

        binding.normalKeyboard.btBackspace.setOnClickListener(view -> onKey(Keyboard.KEYCODE_DELETE, new int[0]));

        binding.normalKeyboard.btBackspace.setOnLongClickListener(view -> {
            fastType(Keyboard.KEYCODE_DELETE, binding.normalKeyboard.btBackspace, 10);
            return true;
        });

        binding.normalKeyboard.btTextToSpeech.setOnClickListener(view -> {
            onKey(332211, new int[0]);

        });



        binding.normalKeyboard.btCopy.setOnClickListener(view -> {
            onKey(2213, new int[0]);
        });

        binding.normalKeyboard.btPaste.setOnClickListener(view -> {
            try {
                ClipData.Item d = clipboard.getPrimaryClip().getItemAt(0);
                if (d != null && d.getText() != null){
                    tt = d.getText().toString();
                    onKey(3443, new int[0]);
                }else {
                    showToast("Nothing in clipboard");
                }
            }catch (Exception e){
                showToast(e.getMessage());
            }
        });



        binding.normalKeyboard.btQ123.setOnClickListener(view -> {
            String b = binding.normalKeyboard.btQ123.getText().toString();
            if (b == "ABC"){
                setNormalKeys();
            }else {
                setQ123Keys();
            }
        });


        binding.normalKeyboard.btSpace.setOnClickListener(view -> onKey(33223 ,new int[0]));

        binding.normalKeyboard.btSpace.setOnLongClickListener(view -> {
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            imeManager.showInputMethodPicker();
            return true;
        });



        binding.normalKeyboard.btComa.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btDot2.setOnTouchListener(typeWithPreview);

        binding.normalKeyboard.btKey27.setOnTouchListener(typeWithPreview);


        binding.normalKeyboard.btKey1.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey2.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey3.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey4.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey5.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey6.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey7.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey8.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey9.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey10.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey11.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey12.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey13.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey14.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey15.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey16.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey17.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey18.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey19.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey20.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey21.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey22.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey23.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey24.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey25.setOnTouchListener(typeWithPreview);
        binding.normalKeyboard.btKey26.setOnTouchListener(typeWithPreview);


        binding.normalKeyboard.btCaps.setOnClickListener(view -> {
            if (onCapsClick == 0) {
                if (isCapsOn == 0) {
                    isCapsOn = 1;
                    binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps2);
                    setCaps(true);
                    lastCapsOnOn = true;
                    new Handler().postDelayed(() -> lastCapsOnOn = false, 500);
                } else if (isCapsOn == 1) {
                    if (lastCapsOnOn) {
                        setCaps(true);
                        isCapsOn = 2;
                        binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps3);
                    } else {
                        setCaps(false);
                        isCapsOn = 0;
                        binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps1);
                    }
                } else if (isCapsOn == 2) {
                    isCapsOn = 0;
                    setCaps(false);
                    binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps1);
                }
            }else if (onCapsClick == 1){
                setSPCKeys();
            }else if (onCapsClick == 2){
                setQ123Keys();
            }
        });



        addFontBt(R.string.font_1);
        addFontBt(R.string.font_2);
        addFontBt(R.string.font_3);
        addFontBt(R.string.font_4);
        addFontBt(R.string.font_5);
        addFontBt(R.string.font_6);
        addFontBt(R.string.font_7);
        addFontBt(R.string.font_8);
        addFontBt(R.string.font_9);
        addFontBt(R.string.font_10);


        chengeFont(new Fonts().getFont(sharedPreferences.getString("def_abc", getString(R.string.font_1))));

    }

    private void addFontBt(@StringRes  int string){
        Fonts.Font font;
        if (string == R.string.font_1){
            font = new Fonts().getFont(sharedPreferences.getString("def_abc", getString(string)));
        }else {
            font = new Fonts().getFont(getString(string));
        }
        binding.normalKeyboard.actionsLl2.addView(newText(font.A+font.B+font.C+font.D, view -> {
            chengeFont(font);
        }));
    }

    private void chengeFont(Fonts.Font font) {
        keya = font.a;
        keyb = font.b;
        keyc = font.c;
        keyd = font.d;
        keye = font.e;
        keyf = font.f;
        keyg = font.g;
        keyh = font.h;
        keyi = font.i;
        keyj = font.j;
        keyk = font.k;
        keyl = font.l;
        keym = font.m;
        keyn = font.n;
        keyo = font.o;
        keyp = font.p;
        keyq = font.q;
        keyr = font.r;
        keys = font.s;
        keyt = font.t;
        keyu = font.u;
        keyv = font.v;
        keyw = font.w;
        keyx = font.x;
        keyy = font.y;
        keyz = font.z;

        keyA = font.A;
        keyB = font.B;
        keyC = font.C;
        keyD = font.D;
        keyE = font.E;
        keyF = font.F;
        keyG = font.G;
        keyH = font.H;
        keyI = font.I;
        keyJ = font.J;
        keyK = font.K;
        keyL = font.L;
        keyM = font.M;
        keyN = font.N;
        keyO = font.O;
        keyP = font.P;
        keyQ = font.Q;
        keyR = font.R;
        keyS = font.S;
        keyT = font.T;
        keyU = font.U;
        keyV = font.V;
        keyW = font.W;
        keyX = font.X;
        keyY = font.Y;
        keyZ = font.Z;
        setNormalKeys();
    }

    private void setCaps(boolean caps){
        if (caps){
            binding.normalKeyboard.btKey1.setText(keyQ);
            binding.normalKeyboard.btKey2.setText(keyW);
            binding.normalKeyboard.btKey3.setText(keyE);
            binding.normalKeyboard.btKey4.setText(keyR);
            binding.normalKeyboard.btKey5.setText(keyT);
            binding.normalKeyboard.btKey6.setText(keyY);
            binding.normalKeyboard.btKey7.setText(keyU);
            binding.normalKeyboard.btKey8.setText(keyI);
            binding.normalKeyboard.btKey9.setText(keyO);
            binding.normalKeyboard.btKey10.setText(keyP);
            binding.normalKeyboard.btKey11.setText(keyA);
            binding.normalKeyboard.btKey12.setText(keyS);
            binding.normalKeyboard.btKey13.setText(keyD);
            binding.normalKeyboard.btKey14.setText(keyF);
            binding.normalKeyboard.btKey15.setText(keyG);
            binding.normalKeyboard.btKey16.setText(keyH);
            binding.normalKeyboard.btKey17.setText(keyJ);
            binding.normalKeyboard.btKey18.setText(keyK);
            binding.normalKeyboard.btKey19.setText(keyL);
            binding.normalKeyboard.btKey20.setText(keyZ);
            binding.normalKeyboard.btKey21.setText(keyX);
            binding.normalKeyboard.btKey22.setText(keyC);
            binding.normalKeyboard.btKey23.setText(keyV);
            binding.normalKeyboard.btKey24.setText(keyB);
            binding.normalKeyboard.btKey25.setText(keyN);
            binding.normalKeyboard.btKey26.setText(keyM);
        }else {
            binding.normalKeyboard.btKey1.setText(keyq);
            binding.normalKeyboard.btKey2.setText(keyw);
            binding.normalKeyboard.btKey3.setText(keye);
            binding.normalKeyboard.btKey4.setText(keyr);
            binding.normalKeyboard.btKey5.setText(keyt);
            binding.normalKeyboard.btKey6.setText(keyy);
            binding.normalKeyboard.btKey7.setText(keyu);
            binding.normalKeyboard.btKey8.setText(keyi);
            binding.normalKeyboard.btKey9.setText(keyo);
            binding.normalKeyboard.btKey10.setText(keyp);
            binding.normalKeyboard.btKey11.setText(keya);
            binding.normalKeyboard.btKey12.setText(keys);
            binding.normalKeyboard.btKey13.setText(keyd);
            binding.normalKeyboard.btKey14.setText(keyf);
            binding.normalKeyboard.btKey15.setText(keyg);
            binding.normalKeyboard.btKey16.setText(keyh);
            binding.normalKeyboard.btKey17.setText(keyj);
            binding.normalKeyboard.btKey18.setText(keyk);
            binding.normalKeyboard.btKey19.setText(keyl);
            binding.normalKeyboard.btKey20.setText(keyz);
            binding.normalKeyboard.btKey21.setText(keyx);
            binding.normalKeyboard.btKey22.setText(keyc);
            binding.normalKeyboard.btKey23.setText(keyv);
            binding.normalKeyboard.btKey24.setText(keyb);
            binding.normalKeyboard.btKey25.setText(keyn);
            binding.normalKeyboard.btKey26.setText(keym);
        }
    }

    private void setCaps(boolean caps, boolean a){
        if (binding.normalKeyboard.btKey27.getVisibility() != View.VISIBLE){
            setCaps(caps);
            if (a){
                binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps2);
            }else {
                binding.normalKeyboard.btCaps.setImageResource(R.drawable.ic_caps1);
            }
        }
    }


    private View.OnClickListener typeKey(String key){
        return view -> {
            char c = key.charAt(0);
            onKey(c ,new int[0]);
        };
    }

    private View.OnClickListener typeKey = view -> {
        TextView t = (TextView) view;
        char c = t.getText().toString().charAt(0);
        onKey(c ,new int[0]);
    };

    View.OnTouchListener typeWithPreview = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            TextView t = (TextView) view;
            tt = t.getText().toString();

            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                onKey(3443 ,new int[0]);
                binding.previewRoot.setVisibility(View.GONE);
            }else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(view.getWidth(), view.getHeight()*2);
                binding.previewRoot.setLayoutParams(lp);
                binding.previewTv.setText(t.getText().toString());
                binding.previewRoot.setVisibility(View.VISIBLE);
                binding.previewRoot.setY(((View)view.getParent()).getY()-view.getHeight());
                binding.previewRoot.setX(view.getX());
            }else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                binding.previewRoot.setVisibility(View.GONE);
            }

            return true;
        }
    };




    private void startServicee() {
        if (isMyServiceRunning(FloatingCalculator.class)){
            stopService(new Intent(SimpleIME.this, FloatingCalculator.class));
            binding.btOFCal.setImageResource(R.drawable.ic_floating_cal);
        }else {
            startService(new Intent(SimpleIME.this, FloatingCalculator.class));
            binding.btOFCal.setImageResource(R.drawable.ic_floating_cal_off);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void liveCal() {
        String aa = binding.calculatedTextEt.getText().toString();
        if (!aa.isEmpty()){
            if (aa.equals("-")){
                binding.liveCalculatedText.setText("00");
                return;
            }
            if (aa.endsWith("+") || aa.endsWith("%") || aa.endsWith(".") || aa.endsWith("×") || aa.endsWith("÷") || aa.endsWith("-")){
                aa = method(aa);
            }
            if (aa.equals("-")){
                return;
            }
            String string231 = aa
                    .replaceAll("x","*")
                    .replaceAll("×","*")
                    .replaceAll("÷","/")
                    .replaceAll("%","/100");

            Expression e = new Expression(string231);
            String tt = String.valueOf(e.calculate());
            if (tt.endsWith(".0")){
                tt = method(tt);
                tt = method(tt);
            }else if (tt.endsWith(".00")){
                tt = method(tt);
                tt = method(tt);
                tt = method(tt);
            }
            binding.liveCalculatedText.setText(tt);
        }else {
            binding.liveCalculatedText.setText("00");
        }
    }

    private void setCur(TextView button, String sym){
        button.setOnClickListener(view1 -> {
            currencySymbol = sym.charAt(0);
            curBt.setText(sym);
            currencyLl.setVisibility(View.GONE);
        });

    }

    private void fastType(int keyEvent, TextView textView, int interval){
        cdt = new CountDownTimer(3000000*500, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!textView.isPressed()){
                    cdt.cancel();
                }else {
                    onKey(keyEvent, new int[0]);
                }
            }

            @Override
            public void onFinish() {
                Log.i("tasting", "Timer finished");
            }
        };
        cdt.start();
    }


    private void onClickBtNumber(TextView editText, TextView button, String textt, View.OnLongClickListener longClick){
        button.setOnClickListener(view -> {
            String text = textt;
            String string = editText.getText().toString();
            String center = "";

            if (string.isEmpty()){
                if (text.equals("+")){
                    return;
                }else if (text.equals("%")){
                    return;
                }else if (text.equals(".")){
                    return;
                }else if (text.equals("×")){
                    return;
                }else if (text.equals("÷")){
                    return;
                }else if (text.equals(")")){
                    return;
                }
            }else {
                if (text.equals("+")){
                    if (string.endsWith("+")){
                        return;
                    }else if (string.endsWith("%")){
                        string = method(string);
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("×")){
                        string = method(string);
                    }else if (string.endsWith("÷")){
                        string = method(string);
                    }else if (string.endsWith("(")) {
                        string = method(string);
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                        string = method(string);
                    }
                }else if (text.equals("%")){
                    if (string.endsWith("+")){
                        string = method(string);
                    }else if (string.endsWith("%")){
                        return;
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("×")){
                        string = method(string);
                    }else if (string.endsWith("÷")){
                        string = method(string);
                    }else if (string.endsWith("(")) {
                        string = method(string);
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                        string = method(string);
                    }else if (isNumeric(string)){
                        Expression e = new Expression(string+"/100");
                        String tt = String.valueOf(e.calculate());
                        if (tt.endsWith(".0")){
                            tt = method(tt);
                            tt = method(tt);
                        }else if (tt.endsWith(".00")){
                            tt = method(tt);
                            tt = method(tt);
                            tt = method(tt);
                        }
                        string = tt;
                        text = "";

                    }else{
                        String[] parts = string.split("(?=\\d+$)", 2);

                        Expression e = new Expression(parts[1]+"/100");
                        String tt = String.valueOf(e.calculate());
                        if (tt.endsWith(".0")){
                            tt = method(tt);
                            tt = method(tt);
                        }else if (tt.endsWith(".00")){
                            tt = method(tt);
                            tt = method(tt);
                            tt = method(tt);
                        }

                        string = parts[0] + tt;
                        text = "";
                    }
                }else if (text.equals(".")){
                    if (string.endsWith("+")){
                        center = "0";
                    }else if (string.endsWith("%")){
                        center = "x0";
                    }else if (string.endsWith(".")){
                        return;
                    }else if (string.endsWith("×")){
                        center = "0";
                    }else if (string.endsWith("÷")){
                        center = "0";
                    }else if (string.endsWith("(")) {
                        center = "0";
                    }else if (string.endsWith(")")) {
                        return;
                    }else if (string.endsWith("-")){
                        center = "0";
                    }
                }else if (text.equals("×")){
                    if (string.endsWith("+")){
                        string = method(string);
                    }else if (string.endsWith("%")){
                        string = method(string);
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("×")){
                        return;
                    }else if (string.endsWith("÷")){
                        string = method(string);
                    }else if (string.endsWith("(")) {
                        string = method(string);
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                        string = method(string);
                    }
                }else if (text.equals("÷")){
                    if (string.endsWith("+")){
                        string = method(string);
                    }else if (string.endsWith("%")){
                        string = method(string);
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("×")){
                        string = method(string);
                    }else if (string.endsWith("÷")){
                        return;
                    }else if (string.endsWith("(")) {
                        string = method(string);
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                        string = method(string);
                    }
                }else if (text.equals("-")){
                    if (string.endsWith("+")){
                        string = method(string);
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                        return;
                    }
                }else if (text.equals("(")){
                    if (Character.isDigit(string.charAt(string.length()-1))){
                        center = "+";
                    }else if (string.endsWith("%")){
                        center = "×";
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                    }
                }else if (text.equals(")")){
                    if (string.endsWith("+")){
                        string = method(string);
                    }else if (string.endsWith("%")){
                        string = method(string);
                    }else if (string.endsWith(".")){
                        center = "0";
                    }else if (string.endsWith("×")){
                        string = method(string);
                    }else if (string.endsWith("÷")){
                        string = method(string);
                    }else if (string.endsWith("(")) {
                        return;
                    }else if (string.endsWith("-")){
                        if (string.equals("-")){
                            return;
                        }
                        string = method(string);
                    }
                }else {
                    if (string.endsWith("%")){
                        center = "×";
                    }
                }
            }


            editText.setText(string+center+text);
            binding.calculatedTextEt.setSelection(binding.calculatedTextEt.getText().toString().length());
            liveCal();


        });

        button.setOnLongClickListener(longClick);

    }

    public String method(String str) {
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }


    private void storeChar(String s) {
        String k = getCharKeyboardHis();
        sharedPreferences.edit().putString("KeyboardHis", k+s).apply();
    }

    private String getCharKeyboardHis() {
        return sharedPreferences.getString("KeyboardHis", "Start: ");
    }

    public void saveCalHisList(CalculatorHistory calHis){
        try {
            List<CalculatorHistory> calHisList = getCalHisList();
            if (calHisList == null){
                calHisList = new ArrayList<>();
            }
            calHisList.add(calHis);
            String json = gson.toJson(calHisList);
            sharedPreferences.edit().putString("CalHis", json).apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    Gson gson = new Gson();
    public List<CalculatorHistory> getCalHisList(){
        String json = sharedPreferences.getString("CalHis", null);
        Type type = new TypeToken<List<CalculatorHistory>>() {}.getType();
        return gson.fromJson(json, type);
    }


    boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}