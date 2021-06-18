package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitMatrix;

public final class AztecCode {
    private int codeWords;
    private boolean compact;
    private int layers;
    private BitMatrix matrix;
    private int size;

    public void setCompact(boolean z) {
        this.compact = z;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public void setLayers(int i) {
        this.layers = i;
    }

    public void setCodeWords(int i) {
        this.codeWords = i;
    }

    public BitMatrix getMatrix() {
        return this.matrix;
    }

    public void setMatrix(BitMatrix bitMatrix) {
        this.matrix = bitMatrix;
    }
}
