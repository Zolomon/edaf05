package com.zolomon.edaf05;

import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.gson.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String DEFAULT_PATH = "C:/Users/zol/code/lth/edaf05/edaf05/";

    public static void main(String[] args) {
        List<Test> tests = new ArrayList<Test>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("tests.json")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            JsonElement element = new JsonParser().parse(reader);
            JsonObject object = element.getAsJsonObject();
            JsonObject lab1 = object.getAsJsonObject("lab1");
            JsonArray lab1Tests = lab1.getAsJsonArray("tests");

            Algorithm algo1 = new StableMarriageAlgorithm();

            for (JsonElement test : lab1Tests) {
                JsonObject t = test.getAsJsonObject();

                String dataPath = DEFAULT_PATH + "lab1/" + t.get("data").getAsString();
                String inputPath = DEFAULT_PATH +"lab1/" + t.get("in").getAsString();
                String outputPath = DEFAULT_PATH + "lab1/" +t.get("out").getAsString();

                String data = "", input = "", output = "";
                try {
                    if (!t.get("data").getAsString().equals("")) {
                        CharSource dataSource = Files.asCharSource(new File(dataPath), Charset.forName("UTF-8"));
                        data = dataSource.read();
                    }
                    if (!t.get("in").getAsString().equals("")) {
                        CharSource inputSource = Files.asCharSource(new File(inputPath), Charset.forName("UTF-8"));
                        input = inputSource.read();
                    }
                    if (!t.get("out").getAsString().equals("")) {
                        CharSource outputSource = Files.asCharSource(new File(outputPath), Charset.forName("UTF-8"));
                        output = outputSource.read();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tests.add(new Lab1Test(t.get("in").getAsString(), algo1, data, input, output));
            }

            for (Test test : tests) {
                if (!test.passes()) {
                    System.out.println(test.getName() + " failed");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
