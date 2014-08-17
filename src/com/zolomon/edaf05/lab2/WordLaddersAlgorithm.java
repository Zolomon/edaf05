package com.zolomon.edaf05.lab2;

import com.zolomon.edaf05.Algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by zol on 16.8.2014.
 */
public class WordLaddersAlgorithm extends Algorithm {

    public static final int WORD_LENGTH = 5;
    private final HashMap<String, Integer> words;
    private Digraph G;
    private int V;
    private int[] output;
    private ArrayList<String[]> wordPairs;

    public WordLaddersAlgorithm() {
        words = new HashMap<String, Integer>();
        wordPairs = new ArrayList<String[]>();

        assertThis(charsExistInWord("other", "there"));
        assertThis(charsExistInWord("other", "their"));
        assertThis(charsExistInWord("could", "would"));
        assertThis(charsExistInWord("would", "could"));
        assertThis(!charsExistInWord("there", "other"));
        assertThis(!charsExistInWord("about", "there"));

        assertThis(charsExistInWord("where", "ether"));
        assertThis(!charsExistInWord("where", "retch"));

        assertThis(charsExistInWord("climb", "blimp"));
        assertThis(charsExistInWord("blimp", "limps"));
        assertThis(charsExistInWord("limps", "pismo"));
        assertThis(charsExistInWord("pismo", "moist"));
    }

    private void assertThis(boolean b) {
        if (!b) throw new IllegalArgumentException("False");
    }

    @Override
    public void parse() {
        parseData();
        parseInput();
    }

    private void parseData() {
        BufferedReader reader = new BufferedReader(new StringReader(getData()));
        String line;
        int index = 0;
        try {
            while ((line = reader.readLine()) != null)
                words.put(line, index++);
        } catch (IOException e) {
            e.printStackTrace();
        }

        V = words.size();
        G = new Digraph(V);

        buildGraph();
    }

    private void buildGraph() {
        String lhs;
        String rhs;
        Set<String> mySet = words.keySet();
        String[] ws = mySet.toArray(new String[mySet.size()]);
        for (int i = 0; i < V; i++) {
            lhs = ws[i];
            for (int j = i+1; j < V; j++) {
                rhs = ws[j];
                if (lhs.equals(rhs)) {
                    continue;
                }
                if (charsExistInWord(lhs, rhs)) {
                    G.addEdge(words.get(lhs), words.get(rhs));
                }
                if (charsExistInWord(rhs, lhs)) {
                    G.addEdge(words.get(rhs), words.get(lhs));
                }
            }
        }
    }

    private boolean charsExistInWord(String word, String otherWord) {
        char[] l = word.substring(1).toCharArray();
        int[] histogram = new int[26];
        char[] r = otherWord.toCharArray();

        for (int i = 0; i < l.length; i++) {
            histogram[l[i]-97]--;
        }

        for (int i = 0; i < r.length; i++) {
            histogram[r[i]-97]++;
        }

        for(int i=0; i<histogram.length; i++) {
            if (histogram[i] < 0) {
                return false;
            }
        }
        return true;
    }

    private void parseInput() {
        BufferedReader reader = new BufferedReader(new StringReader(getInput()));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] path = line.split(" ");
                wordPairs.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        output = new int[wordPairs.size()];
    }

    @Override
    public void execute() {
        for (int i = 0; i < wordPairs.size(); i++) {
            String[] path = wordPairs.get(i);
            BreadthFirstDirectedPath bfs = new BreadthFirstDirectedPath(G, words.get(path[0]));
            boolean hasPath = bfs.hasPathTo(words.get(path[1]));
            output[i] = hasPath ? bfs.distTo(words.get(path[1])) : -1;
        }
    }

    @Override
    public String getOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < output.length; i++) {
            stringBuilder.append(
                    String.format(
                            "%d\n",
                            output[i]
                    )
            );
        }
        return stringBuilder.toString();
    }
}
