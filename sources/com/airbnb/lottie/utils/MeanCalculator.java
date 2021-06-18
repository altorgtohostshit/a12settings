package com.airbnb.lottie.utils;

public class MeanCalculator {

    /* renamed from: n */
    private int f52n;
    private float sum;

    public void add(float f) {
        float f2 = this.sum + f;
        this.sum = f2;
        int i = this.f52n + 1;
        this.f52n = i;
        if (i == Integer.MAX_VALUE) {
            this.sum = f2 / 2.0f;
            this.f52n = i / 2;
        }
    }
}
