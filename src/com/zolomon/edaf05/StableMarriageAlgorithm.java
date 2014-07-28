package com.zolomon.edaf05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zol on 20.7.2014.
 */
public class StableMarriageAlgorithm extends Algorithm {

    private List<Integer> men;
    private final ArrayList<Integer> women;
    private int N;

    public StableMarriageAlgorithm() {
        this.men = new ArrayList<Integer>();
        this.women = new ArrayList<Integer>();
    }

    @Override
    public void parse() {
        BufferedReader reader = new BufferedReader(new StringReader(getInput()));
        String line;
        Pattern nCount = Pattern.compile("n=(?<N>\\d+)");
        Pattern person = Pattern.compile("(?<index>\\d+) (?<name>[a-zA-Z])");
        Pattern preferences = Pattern.compile("(?<index>\\d+): (?<list>( \\d+)+)");

        Matcher nCountMatcher;
        Matcher personMatcher;
        Matcher preferenceMatcher;

        try {
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == '#') { // skip comments
                    continue;
                }

                nCountMatcher = nCount.matcher(line);
                if (nCountMatcher.matches()) {
                    this.N = Integer.parseInt(nCountMatcher.group("N"));
                }

                personMatcher=person.matcher(line);
                if (personMatcher.matches()) {
                    int index = Integer.parseInt(personMatcher.group("index"));
                    if (index % 2 == 1) {
                        this.men.add(index);
                    } else {
                        this.women.add(index);
                    }
                }

                preferenceMatcher = preferences.matcher(line);
                if (preferenceMatcher.matches()) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute() {
        PriorityQueue<Integer> freeMen = new PriorityQueue<Integer>();
        //for(int i = 0; i )
    }

    @Override
    public String getOutput() {
        return "";
    }
}
