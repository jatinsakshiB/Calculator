package com.jsb.calculator.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsb.calculator.modules.Modules;
import com.jsb.calculator.databinding.ActivityCalculatorBinding;

import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private ActivityCalculatorBinding binding;
    private Activity activity;
    CountDownTimer cdt;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        sharedPreferences = getSharedPreferences("com.jsb.calculator", MODE_PRIVATE);

        binding.et1.setKeyListener(null);

        binding.et1.setText("");
        binding.liveCalculator.setText("00");

        binding.btC.setOnClickListener(view -> {
            binding.et1.setText("");
            binding.liveCalculator.setText("00");
        });


        binding.btB.setOnClickListener(view -> {
            if (!binding.et1.getText().toString().isEmpty()){
                binding.et1.setText(method(binding.et1.getText().toString()));
                binding.et1.setSelection(binding.et1.getText().toString().length());
                liveCal();
            }
        });

        binding.btB.setOnLongClickListener(view -> {
            cdt = new CountDownTimer(3000000*500, 50) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (!binding.btB.isPressed()){
                        cdt.cancel();
                    }else {
                        if (!binding.et1.getText().toString().isEmpty()){
                            binding.et1.setText(method(binding.et1.getText().toString()));
                            binding.et1.setSelection(binding.et1.getText().toString().length());
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
        onClickBtNumber(binding.btI, "×");
        onClickBtNumber(binding.btD, "÷");
        onClickBtNumber(binding.btDot, ".");
        onClickBtNumber(binding.btM, "-");
        onClickBtNumber(binding.btP, "%");
        onClickBtNumber(binding.btPlus, "+");


        binding.btE.setOnClickListener(view -> {
            String aa = binding.et1.getText().toString();
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
                Modules.CalHis calHis = new Modules.CalHis();
                calHis.setTime(System.currentTimeMillis());
                calHis.setType(0);
                calHis.setCal(c);
                calHis.setValue(valll);

                saveCalHisList(calHis);
                binding.et1.setText(calculate);
            }
        });


    }


    private void onClickBtNumber(TextView button, String text){
        button.setOnClickListener(view -> {
            String string = binding.et1.getText().toString();
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

            binding.et1.setText(string+center+text);
            binding.et1.setSelection(binding.et1.getText().toString().length());
            liveCal();

        });
    }

    private void liveCal() {
        String aa = binding.et1.getText().toString();
        if (!aa.isEmpty()){
            if (aa.equals("-")){
                binding.liveCalculator.setText("00");
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
            binding.liveCalculator.setText(tt);
        }else {
            binding.liveCalculator.setText("00");
        }
    }


    public String method(String str) {
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void saveCalHisList(Modules.CalHis calHis){
        List<Modules.CalHis> calHisList = getCalHisList();
        if (calHisList == null){
            calHisList = new ArrayList<>();
        }
        calHisList.add(calHis);
        String json = gson.toJson(calHisList);
        sharedPreferences.edit().putString("CalHis", json).apply();

    }

    Gson gson = new Gson();
    public List<Modules.CalHis> getCalHisList(){
        String json = sharedPreferences.getString("CalHis", null);
        Type type = new TypeToken<List<Modules.CalHis>>() {}.getType();
        return gson.fromJson(json, type);
    }
}