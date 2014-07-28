package com.zolomon.edaf05;

/**
 * Created by zol on 20.7.2014.
 */
public abstract class Test {
    private String name;
    private Algorithm algorithm;
    private String data;
    private String input;
    private String output;

    public Test(String name, Algorithm algorithm, String data, String input, String output) {
        this.name = name;

        this.algorithm = algorithm;
        this.algorithm.setData(data);
        this.algorithm.setInput(input);

        this.data = data;
        this.input = input;
        this.output = output;
    }

    public boolean run() {
        algorithm.execute();
        String output = algorithm.getOutput();
        return this.output.equals(output);
    }

    @Override
    public String toString() {
        return getName() + ", " + getData() + ", " + getInput() + ", " + getOutput();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public boolean passes() {
        return run();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
