package com.jsb.calculator.Classes;


public class Fonts {


    public class Font{
        public String a = "a";
        public String b = "b";
        public String c = "c";
        public String d = "d";
        public String e = "e";
        public String f = "f";
        public String g = "g";
        public String h = "h";
        public String i = "i";
        public String j = "j";
        public String k = "k";
        public String l = "l";
        public String m = "m";
        public String n = "n";
        public String o = "o";
        public String p = "p";
        public String q = "q";
        public String r = "r";
        public String s = "s";
        public String t = "t";
        public String u = "u";
        public String v = "v";
        public String w = "w";
        public String x = "x";
        public String y = "y";
        public String z = "z";

        public String A = "A";
        public String B = "B";
        public String C = "C";
        public String D = "D";
        public String E = "E";
        public String F = "F";
        public String G = "G";
        public String H = "H";
        public String I = "I";
        public String J = "J";
        public String K = "K";
        public String L = "L";
        public String M = "M";
        public String N = "N";
        public String O = "O";
        public String P = "P";
        public String Q = "Q";
        public String R = "R";
        public String S = "S";
        public String T = "T";
        public String U = "U";
        public String V = "V";
        public String W = "W";
        public String X = "X";
        public String Y = "Y";
        public String Z = "Z";
        public int id = 0;
    }

    public Font getFont(String font){

//        if (font.length() != 26){
//            return null;
//        }

        String[] allKeys = font.split("=");
        String[] keysCaps = allKeys[1].split(",");
        String[] keys = allKeys[0].split(",");

        Font font_o = new Font();
        font_o.a = keys[0];
        font_o.b = keys[1];
        font_o.c = keys[2];
        font_o.d = keys[3];
        font_o.e = keys[4];
        font_o.f = keys[5];
        font_o.g = keys[6];
        font_o.h = keys[7];
        font_o.i = keys[8];
        font_o.j = keys[9];
        font_o.k = keys[10];
        font_o.l = keys[11];
        font_o.m = keys[12];
        font_o.n = keys[13];
        font_o.o = keys[14];
        font_o.p = keys[15];
        font_o.q = keys[16];
        font_o.r = keys[17];
        font_o.s = keys[18];
        font_o.t = keys[19];
        font_o.u = keys[20];
        font_o.v = keys[21];
        font_o.w = keys[22];
        font_o.x = keys[23];
        font_o.y = keys[24];
        font_o.z = keys[25];

        font_o.A = keysCaps[0];
        font_o.B = keysCaps[1];
        font_o.C = keysCaps[2];
        font_o.D = keysCaps[3];
        font_o.E = keysCaps[4];
        font_o.F = keysCaps[5];
        font_o.G = keysCaps[6];
        font_o.H = keysCaps[7];
        font_o.I = keysCaps[8];
        font_o.J = keysCaps[9];
        font_o.K = keysCaps[10];
        font_o.L = keysCaps[11];
        font_o.M = keysCaps[12];
        font_o.N = keysCaps[13];
        font_o.O = keysCaps[14];
        font_o.P = keysCaps[15];
        font_o.Q = keysCaps[16];
        font_o.R = keysCaps[17];
        font_o.S = keysCaps[18];
        font_o.T = keysCaps[19];
        font_o.U = keysCaps[20];
        font_o.V = keysCaps[21];
        font_o.W = keysCaps[22];
        font_o.X = keysCaps[23];
        font_o.Y = keysCaps[24];
        font_o.Z = keysCaps[25];


        return font_o;
    }

}
