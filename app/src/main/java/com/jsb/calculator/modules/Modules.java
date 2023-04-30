package com.jsb.calculator.modules;

public class Modules {

    public static class CalHis{

        private long time;
        private double cal;
        private String value;
        private int type;


        public CalHis() {
        }

        public CalHis(long time, double cal, String value, int type) {
            this.time = time;
            this.cal = cal;
            this.value = value;
            this.type = type;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public double getCal() {
            return cal;
        }

        public void setCal(double cal) {
            this.cal = cal;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
