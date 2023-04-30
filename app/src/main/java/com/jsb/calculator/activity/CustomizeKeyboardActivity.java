package com.jsb.calculator.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jsb.calculator.Classes.Fonts;
import com.jsb.calculator.R;
import com.jsb.calculator.databinding.ActivityCustomizeKeyboardBinding;
import com.jsb.calculator.databinding.DialogEachKeyBinding;

public class CustomizeKeyboardActivity extends AppCompatActivity {

    private ActivityCustomizeKeyboardBinding binding;
    private Activity activity;
    SharedPreferences sharedPreferences;

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

    private boolean isCapes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomizeKeyboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        sharedPreferences = getSharedPreferences("com.jsb.calculator", MODE_PRIVATE);


        //def_abc


        binding.btCCaall.setEnabled(false);
        binding.btTextToSpeech.setEnabled(false);
        binding.btCopy.setEnabled(false);
        binding.btPaste.setEnabled(false);
        binding.btBackspace.setEnabled(false);
        binding.btQ123.setEnabled(false);
        binding.btComa.setEnabled(false);
        binding.btEmoji.setEnabled(false);
        binding.btSpace.setEnabled(false);
        binding.btDot2.setEnabled(false);
        binding.btKeyEnter.setEnabled(false);

        binding.btCaps.setOnClickListener(v -> {
            isCapes = !isCapes;
            setCaps(isCapes, isCapes);
        });


        //a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z=A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z

        setUpKeys(new Fonts().getFont(sharedPreferences.getString("def_abc", getString(R.string.font_1))));


        binding.btKey1.setOnClickListener(onKeyClickListener);
        binding.btKey2.setOnClickListener(onKeyClickListener);
        binding.btKey3.setOnClickListener(onKeyClickListener);
        binding.btKey4.setOnClickListener(onKeyClickListener);
        binding.btKey5.setOnClickListener(onKeyClickListener);
        binding.btKey6.setOnClickListener(onKeyClickListener);
        binding.btKey7.setOnClickListener(onKeyClickListener);
        binding.btKey8.setOnClickListener(onKeyClickListener);
        binding.btKey9.setOnClickListener(onKeyClickListener);
        binding.btKey10.setOnClickListener(onKeyClickListener);
        binding.btKey11.setOnClickListener(onKeyClickListener);
        binding.btKey12.setOnClickListener(onKeyClickListener);
        binding.btKey13.setOnClickListener(onKeyClickListener);
        binding.btKey14.setOnClickListener(onKeyClickListener);
        binding.btKey15.setOnClickListener(onKeyClickListener);
        binding.btKey16.setOnClickListener(onKeyClickListener);
        binding.btKey17.setOnClickListener(onKeyClickListener);
        binding.btKey18.setOnClickListener(onKeyClickListener);
        binding.btKey19.setOnClickListener(onKeyClickListener);
        binding.btKey20.setOnClickListener(onKeyClickListener);
        binding.btKey21.setOnClickListener(onKeyClickListener);
        binding.btKey22.setOnClickListener(onKeyClickListener);
        binding.btKey23.setOnClickListener(onKeyClickListener);
        binding.btKey24.setOnClickListener(onKeyClickListener);
        binding.btKey25.setOnClickListener(onKeyClickListener);
        binding.btKey26.setOnClickListener(onKeyClickListener);



    }

    View.OnClickListener onKeyClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            showDialog((String) textView.getTag(R.string.small), (String) textView.getTag(R.string.big));
        }
    };

    private void showDialog(String oldSmall, String oldBig){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Change Key");
        DialogEachKeyBinding keyBinding = DialogEachKeyBinding.inflate(getLayoutInflater());

        builder.setView(keyBinding.getRoot());

        keyBinding.lket.setText(oldSmall);
        keyBinding.uket.setText(oldBig);

        builder.setPositiveButton("SAVE", (dialog, which) -> {

            String upper = keyBinding.uket.getText().toString();
            String lover = keyBinding.lket.getText().toString();
            if (!lover.isEmpty() && !upper.isEmpty() && !lover.contains(",") && !lover.contains("=") && !upper.contains(",") && !upper.contains("=")){

                String s = sharedPreferences.getString("def_abc", getString(R.string.font_1));
                s = s.replace(oldSmall, lover);
                s = s.replace(oldBig, upper);

                sharedPreferences.edit().putString("def_abc", s).apply();
                setUpKeys(new Fonts().getFont(sharedPreferences.getString("def_abc", getString(R.string.font_1))));

            }else {
                Toast.makeText(activity, "Invalid key", Toast.LENGTH_SHORT).show();
            }


            //m_Text = input.getText().toString();
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void setCaps(boolean caps, boolean a){
        setCaps(caps);
        if (a){
            binding.btCaps.setImageResource(R.drawable.ic_caps2);
        }else {
            binding.btCaps.setImageResource(R.drawable.ic_caps1);
        }
    }

    private void setCaps(boolean caps){
        if (caps){
            binding.btKey1.setText(keyQ);
            binding.btKey2.setText(keyW);
            binding.btKey3.setText(keyE);
            binding.btKey4.setText(keyR);
            binding.btKey5.setText(keyT);
            binding.btKey6.setText(keyY);
            binding.btKey7.setText(keyU);
            binding.btKey8.setText(keyI);
            binding.btKey9.setText(keyO);
            binding.btKey10.setText(keyP);
            binding.btKey11.setText(keyA);
            binding.btKey12.setText(keyS);
            binding.btKey13.setText(keyD);
            binding.btKey14.setText(keyF);
            binding.btKey15.setText(keyG);
            binding.btKey16.setText(keyH);
            binding.btKey17.setText(keyJ);
            binding.btKey18.setText(keyK);
            binding.btKey19.setText(keyL);
            binding.btKey20.setText(keyZ);
            binding.btKey21.setText(keyX);
            binding.btKey22.setText(keyC);
            binding.btKey23.setText(keyV);
            binding.btKey24.setText(keyB);
            binding.btKey25.setText(keyN);
            binding.btKey26.setText(keyM);
        }else {
            binding.btKey1.setText(keyq);
            binding.btKey2.setText(keyw);
            binding.btKey3.setText(keye);
            binding.btKey4.setText(keyr);
            binding.btKey5.setText(keyt);
            binding.btKey6.setText(keyy);
            binding.btKey7.setText(keyu);
            binding.btKey8.setText(keyi);
            binding.btKey9.setText(keyo);
            binding.btKey10.setText(keyp);
            binding.btKey11.setText(keya);
            binding.btKey12.setText(keys);
            binding.btKey13.setText(keyd);
            binding.btKey14.setText(keyf);
            binding.btKey15.setText(keyg);
            binding.btKey16.setText(keyh);
            binding.btKey17.setText(keyj);
            binding.btKey18.setText(keyk);
            binding.btKey19.setText(keyl);
            binding.btKey20.setText(keyz);
            binding.btKey21.setText(keyx);
            binding.btKey22.setText(keyc);
            binding.btKey23.setText(keyv);
            binding.btKey24.setText(keyb);
            binding.btKey25.setText(keyn);
            binding.btKey26.setText(keym);
        }



        binding.btKey1.setTag(R.string.small,keyq);
        binding.btKey2.setTag(R.string.small,keyw);
        binding.btKey3.setTag(R.string.small,keye);
        binding.btKey4.setTag(R.string.small,keyr);
        binding.btKey5.setTag(R.string.small,keyt);
        binding.btKey6.setTag(R.string.small,keyy);
        binding.btKey7.setTag(R.string.small,keyu);
        binding.btKey8.setTag(R.string.small,keyi);
        binding.btKey9.setTag(R.string.small,keyo);
        binding.btKey10.setTag(R.string.small,keyp);
        binding.btKey11.setTag(R.string.small,keya);
        binding.btKey12.setTag(R.string.small,keys);
        binding.btKey13.setTag(R.string.small,keyd);
        binding.btKey14.setTag(R.string.small,keyf);
        binding.btKey15.setTag(R.string.small,keyg);
        binding.btKey16.setTag(R.string.small,keyh);
        binding.btKey17.setTag(R.string.small,keyj);
        binding.btKey18.setTag(R.string.small,keyk);
        binding.btKey19.setTag(R.string.small,keyl);
        binding.btKey20.setTag(R.string.small,keyz);
        binding.btKey21.setTag(R.string.small,keyx);
        binding.btKey22.setTag(R.string.small,keyc);
        binding.btKey23.setTag(R.string.small,keyv);
        binding.btKey24.setTag(R.string.small,keyb);
        binding.btKey25.setTag(R.string.small,keyn);
        binding.btKey26.setTag(R.string.small,keym);

        binding.btKey1.setTag(R.string.big,keyQ);
        binding.btKey2.setTag(R.string.big,keyW);
        binding.btKey3.setTag(R.string.big,keyE);
        binding.btKey4.setTag(R.string.big,keyR);
        binding.btKey5.setTag(R.string.big,keyT);
        binding.btKey6.setTag(R.string.big,keyY);
        binding.btKey7.setTag(R.string.big,keyU);
        binding.btKey8.setTag(R.string.big,keyI);
        binding.btKey9.setTag(R.string.big,keyO);
        binding.btKey10.setTag(R.string.big,keyP);
        binding.btKey11.setTag(R.string.big,keyA);
        binding.btKey12.setTag(R.string.big,keyS);
        binding.btKey13.setTag(R.string.big,keyD);
        binding.btKey14.setTag(R.string.big,keyF);
        binding.btKey15.setTag(R.string.big,keyG);
        binding.btKey16.setTag(R.string.big,keyH);
        binding.btKey17.setTag(R.string.big,keyJ);
        binding.btKey18.setTag(R.string.big,keyK);
        binding.btKey19.setTag(R.string.big,keyL);
        binding.btKey20.setTag(R.string.big,keyZ);
        binding.btKey21.setTag(R.string.big,keyX);
        binding.btKey22.setTag(R.string.big,keyC);
        binding.btKey23.setTag(R.string.big,keyV);
        binding.btKey24.setTag(R.string.big,keyB);
        binding.btKey25.setTag(R.string.big,keyN);
        binding.btKey26.setTag(R.string.big,keyM);
    }

    private void setUpKeys(Fonts.Font font) {
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
        setCaps(isCapes, isCapes);
    }

}