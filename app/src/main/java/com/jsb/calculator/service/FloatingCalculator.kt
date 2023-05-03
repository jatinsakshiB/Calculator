package com.jsb.calculator.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsb.calculator.databinding.FloatingCalculatorBinding;
import com.jsb.calculator.modules.CalculatorHistory;

import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FloatingCalculator extends Service {

    WindowManager wm;
    LayoutInflater inflate;
    FloatingCalculatorBinding binding;
    boolean dsahhs = false;
    CountDownTimer cdt;
    SharedPreferences sharedPreferences;


    private long oldTime;

    public FloatingCalculator() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("com.jsb.calculator", MODE_PRIVATE);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        int FLAG  = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_TOAST;

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP;
        parameters.x = 0;
        parameters.y = 0;


        inflate = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = FloatingCalculatorBinding.inflate(inflate);

        binding.closeBt.setOnClickListener(view -> {
            stopSelf();
        });

        binding.calLL.setVisibility(View.VISIBLE);
        binding.showCal.setVisibility(View.GONE);
        binding.calculatedTextEt.setKeyListener(null);


        binding.showCal.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            double x;
            double y;
            double pressedX;
            double pressedY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updatedParameters.x;
                        y = updatedParameters.y;
                        pressedX = motionEvent.getRawX();
                        pressedY = motionEvent.getRawY();
                        oldTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dsahhs = true;
                        updatedParameters.y = (int) (y + (motionEvent.getRawY() - pressedY));
                        updatedParameters.x = (int) (x + (motionEvent.getRawX() - pressedX));

                        wm.updateViewLayout(binding.getRoot(), updatedParameters);
                        break;
                    case MotionEvent.ACTION_UP:

                        if (oldTime + 300 > System.currentTimeMillis()) {
                            binding.calLL.setVisibility(View.VISIBLE);
                            binding.showCal.setVisibility(View.GONE);
                            wm.updateViewLayout(binding.getRoot(), parameters);
                            if (!dsahhs){

                            }else {
                                dsahhs = false;
                            }
                        }


                    default:
                        break;
                }
                return true;
            }
        });

        binding.minimizeBt.setOnClickListener(view -> {
            binding.calLL.setVisibility(View.GONE);
            binding.showCal.setVisibility(View.VISIBLE);
            wm.updateViewLayout(binding.getRoot(), parameters);
        });


        binding.dragLL.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            double x;
            double y;
            double pressedX;
            double pressedY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = motionEvent.getRawX();
                        pressedY = motionEvent.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updatedParameters.y = (int) (y + (motionEvent.getRawY() - pressedY));
                        updatedParameters.x = (int) (x + (motionEvent.getRawX() - pressedX));


                        wm.updateViewLayout(binding.getRoot(), updatedParameters);

                    default:
                        break;
                }

                return true;
            }
        });

        setUpCal();


        try {
            wm.addView(binding.getRoot(),parameters);
        }catch (Exception e){
            Toast.makeText(this, "Your device not supporting floating calculator.", Toast.LENGTH_SHORT).show();
        }

    }


    private void setUpCal(){
        binding.calculatedTextEt.setText("");
        binding.liveCalculatedText.setText("00");

        binding.btClear.setOnClickListener(view -> {
            binding.calculatedTextEt.setText("");
            binding.liveCalculatedText.setText("00");
        });


        binding.btBackspace.setOnClickListener(view -> {
            if (!binding.calculatedTextEt.getText().toString().isEmpty()){
                binding.calculatedTextEt.setText(method(binding.calculatedTextEt.getText().toString()));
                binding.calculatedTextEt.setSelection(binding.calculatedTextEt.getText().toString().length());
                liveCal();
            }
        });

        binding.btBackspace.setOnLongClickListener(view -> {
            cdt = new CountDownTimer(3000000*500, 50) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (!binding.btBackspace.isPressed()){
                        cdt.cancel();
                    }else {
                        if (!binding.calculatedTextEt.getText().toString().isEmpty()){
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

        onClickBtNumber(binding.bt1, "1");
        onClickBtNumber(binding.bt2, "2");
        onClickBtNumber(binding.bt3, "3");
        onClickBtNumber(binding.bt4, "4");
        onClickBtNumber(binding.bt5, "5");
        onClickBtNumber(binding.bt6, "6");
        onClickBtNumber(binding.bt7, "7");
        onClickBtNumber(binding.bt8, "8");
        onClickBtNumber(binding.bt9, "9");
        onClickBtNumber(binding.bt0, "0");
        onClickBtNumber(binding.bt00, "00");
        onClickBtNumber(binding.btMultiply, "×");
        onClickBtNumber(binding.btDivide, "÷");
        onClickBtNumber(binding.btDot, ".");
        onClickBtNumber(binding.btMinus, "-");
        onClickBtNumber(binding.btPercentage, "%");
        onClickBtNumber(binding.btPlus, "+");


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
                CalculatorHistory calHis = new CalculatorHistory(System.currentTimeMillis(), c, valll, 3);
                calHis.setTime(System.currentTimeMillis());

                saveCalHisList(calHis);
                binding.calculatedTextEt.setText(calculate);
            }
        });
    }

    private void onClickBtNumber(TextView button, String text){
        button.setOnClickListener(view -> {
            String string = binding.calculatedTextEt.getText().toString();
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

            binding.calculatedTextEt.setText(string+center+text);
            binding.calculatedTextEt.setSelection(binding.calculatedTextEt.getText().toString().length());
            liveCal();

        });
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


    public String method(String str) {
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void saveCalHisList(CalculatorHistory calHis){
        List<CalculatorHistory> calHisList = getCalHisList();
        if (calHisList == null){
            calHisList = new ArrayList<>();
        }
        calHisList.add(calHis);
        String json = gson.toJson(calHisList);
        sharedPreferences.edit().putString("CalHis", json).apply();

    }

    Gson gson = new Gson();
    public List<CalculatorHistory> getCalHisList(){
        String json = sharedPreferences.getString("CalHis", null);
        Type type = new TypeToken<List<CalculatorHistory>>() {}.getType();
        return gson.fromJson(json, type);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(binding.getRoot());
        stopSelf();
    }
}