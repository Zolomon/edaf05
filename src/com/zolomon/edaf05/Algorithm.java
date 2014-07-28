package com.zolomon.edaf05;

/**
 * Created by zol on 20.7.2014.
 */
public abstract class Algorithm {
    private String output;
    private String data;
    private String input;

    public abstract void parse();

    public abstract void execute();

    public abstract String getOutput();

    public void setOutput(String output) {
        this.output = output;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }
}
