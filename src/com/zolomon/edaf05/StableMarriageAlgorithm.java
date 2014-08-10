package com.zolomon.edaf05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zol on 20.7.2014.
 */
public class StableMarriageAlgorithm extends Algorithm {

    private HashMap<Integer, String> menMap;
    private HashMap<Integer, String> womenMap;
    private HashMap<Integer, Integer> menIdLookup;
    private HashMap<Integer, Integer> menReverseIdLookup;

    private PriorityQueue<Integer> freeMen;

    private int[] wife;
    private int[] husband;

    private int[] menNextWomanPointerList;
    private int[][] preferenceList;
    private int[][] inversePreferenceList;

    private int N;
    private HashMap<Integer, Integer> womenIdLookup;
    private HashMap<Integer, Integer> womenReverseIdLookup;
    private HashMap<Integer, Integer> idLookup;

    public StableMarriageAlgorithm() {

    }

    @Override
    public void parse() {
        BufferedReader reader = new BufferedReader(new StringReader(getInput()));
        String line;
        Pattern nCount = Pattern.compile("n=(?<N>\\d+)");
        Pattern person = Pattern.compile("(?<index>\\d+) (?<name>[a-zA-Z0-9-']+)");
        Pattern preferences = Pattern.compile("(?<index>\\d+): (?<list>([0-9 ])+)");

        Matcher nCountMatcher;
        Matcher personMatcher;
        Matcher preferenceMatcher;

        int menIdx = 1;
        int womenIdx = 1;

        try {
            // Parse each line
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    if (line.charAt(0) == '#') { // skip comments
                        continue;
                    }

                    // Matches n=XXX
                    nCountMatcher = nCount.matcher(line);
                    if (nCountMatcher.matches()) {
                        this.menMap = new HashMap<Integer, String>();
                        this.menIdLookup = new HashMap<Integer, Integer>();
                        this.menReverseIdLookup = new HashMap<Integer, Integer>();
                        this.womenMap = new HashMap<Integer, String>();
                        this.womenIdLookup = new HashMap<Integer, Integer>();
                        this.womenReverseIdLookup = new HashMap<Integer, Integer>();
                        this.idLookup = new HashMap<Integer, Integer>();
                        this.freeMen = new PriorityQueue<Integer>();

                        this.N = Integer.parseInt(nCountMatcher.group("N"));

                        // Initialize buffers for men and women
                        this.wife = new int[this.N + 1];
                        this.husband = new int[this.N + 1];

                        this.menNextWomanPointerList = new int[this.N + 1];
                        Arrays.fill(this.menNextWomanPointerList, 1);

                        this.preferenceList = new int[2 * this.N + 1][this.N + 1];
                        this.inversePreferenceList = new int[2 * this.N + 1][this.N + 1];

                        continue;
                    }

                    // Matches X <name>
                    personMatcher = person.matcher(line);
                    if (personMatcher.matches()) {

                        int index = Integer.parseInt(personMatcher.group("index"));
                        boolean isMan = index % 2 == 1;
                        if (isMan) {
                            this.menMap.put(index, personMatcher.group("name"));
                            this.menReverseIdLookup.put(menIdx, index);
                            this.idLookup.put(index, menIdx);
                            this.menIdLookup.put(index, menIdx++);

                        } else {
                            this.womenMap.put(index, personMatcher.group("name"));
                            this.womenReverseIdLookup.put(womenIdx, index);
                            this.idLookup.put(index, womenIdx);
                            this.womenIdLookup.put(index, womenIdx++);
                        }

                        continue;
                    }

                    // Matches X: 1 2 ... n
                    preferenceMatcher = preferences.matcher(line);
                    if (preferenceMatcher.matches()) {

                        String[] prefStr = preferenceMatcher.group("list").split(" ");
                        int personId = Integer.parseInt(preferenceMatcher.group("index"));

                        for (int personIndex = 1; personIndex <= prefStr.length; personIndex++) {
                            int id = Integer.parseInt(prefStr[personIndex - 1]);
                            int personIdToPersonIndex = idLookup.get(id);

                            preferenceList[personId][personIndex] = personIdToPersonIndex;
                            inversePreferenceList[personId][preferenceList[personId][personIndex]] = personIndex;
                        }
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute() {

        // Initially, all m € M and w € W are free
        for (int man = 1; man <= N; man++) {
            freeMan(man);
        }

        while (thereIsAFreeManWithWomenLeftToProposeTo()) {
            int freeMan = getNextFreeManWithAvailableWomen();

            int woman = getHighestRankedWomenInFreeMansPreferenceListToWhomHeHasNotYetProposedTo(freeMan);

            // If w is free then
            if (isWomanFree(woman)) {
                engageManAndWoman(freeMan, woman);

                menNextWomanPointerList[freeMan]++;

                freeMen.remove(freeMan);
            } else {
                int manWomanIsCurrentlyEngagedTo = getCurrentlyEngagedMan(woman);
                assertThis (manWomanIsCurrentlyEngagedTo > 0 && manWomanIsCurrentlyEngagedTo <= this.N);
                if (womanPrefersThisManToThatMan(woman, manWomanIsCurrentlyEngagedTo, freeMan)) {
                    freeMan(freeMan);
                } else {
                    engageManAndWoman(freeMan, woman);
                    freeMan(manWomanIsCurrentlyEngagedTo);
                }

                // This man will look at the next woman...
                menNextWomanPointerList[freeMan]++;
            }
        }
    }

    private void freeMan(int man) {
        assertThis(man > 0 && man <= this.N);

        wife[man] = 0;
        freeMen.add(man);
    }

    private void engageManAndWoman(int man, int woman) {
        assertThis (man > 0 && man <= this.N);
        assertThis (woman > 0 && woman <= this.N);

        wife[man] = woman;
        husband[woman] = man;
    }

    private String getWomanName(int woman) {
        assertThis (woman > 0 && woman <= this.N);
        String womanName = womenMap.get(womenReverseIdLookup.get(woman));
        assertThis (womanName != null);
        return womanName;
    }

    private String getManName(int man) {
        assertThis (man > 0 && man <= this.N);
        String manName = menMap.get(menReverseIdLookup.get(man));
        assertThis (manName != null);
        return manName;
    }

    private boolean womanPrefersThisManToThatMan(int woman, int manWomanIsCurrentlyEngagedTo, int freeMan) {
        assertThis (woman > 0 && woman <= this.N);
        assertThis (manWomanIsCurrentlyEngagedTo > 0 && manWomanIsCurrentlyEngagedTo <= this.N);
        assertThis (freeMan > 0 && freeMan <= this.N);

        int womanId = womenReverseIdLookup.get(woman);
        return getWomansRatingOfMan(womanId, manWomanIsCurrentlyEngagedTo) < getWomansRatingOfMan(womanId, freeMan);
    }

    private int getWomansRatingOfMan(int woman, int man) {
        assertThis (woman > 0 && woman <= 2 * this.N);
        assertThis (man > 0 && man <= this.N);
        return inversePreferenceList[woman][man];
    }

    private int getCurrentlyEngagedMan(int woman) {
        assertThis (woman > 0 && woman <= this.N);
        return husband[woman];
    }

    private boolean isWomanFree(int woman) {
        assertThis (woman > 0 && woman <= this.N);
        return husband[woman] == 0;
    }

    private int getHighestRankedWomenInFreeMansPreferenceListToWhomHeHasNotYetProposedTo(int freeMan) {
        assertThis (freeMan > 0 && freeMan <= this.N);
        int personId = menReverseIdLookup.get(freeMan);
        return preferenceList[personId][menNextWomanPointerList[freeMan]];
    }

    private int getNextFreeManWithAvailableWomen() {
        for (int man : freeMen) {
            if (menNextWomanPointerList[man] <= this.N) {
                freeMen.remove(man);
                return man;
            }
        }
        return -1;
    }

    private boolean thereIsAFreeManWithWomenLeftToProposeTo() {
        boolean freeMenExists = freeMen.size() > 0;
        boolean manStillHaveWomenToProposeTo = false;
        //menNextWomanPointerList[freeMen.peek()] < this.N;
        for (int man : freeMen) {
            if (menNextWomanPointerList[man] <= this.N) {
                manStillHaveWomenToProposeTo = true;
                break;
            }
        }

        return freeMenExists && manStillHaveWomenToProposeTo;
    }

    @Override
    public String getOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= N; i++) {
            stringBuilder.append(
                    String.format(
                            "%s -- %s\n",
                            getManName(i),
                            getWomanName(wife[i])
                    )
            );
        }
        return stringBuilder.toString();
    }

    public void assertThis(boolean cond) {
        if (cond == false) {
            throw new RuntimeException();
        }
    }
}
