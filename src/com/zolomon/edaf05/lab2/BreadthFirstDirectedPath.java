package com.zolomon.edaf05.lab2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zol on 17.8.2014.
 */
public class BreadthFirstDirectedPath {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    public BreadthFirstDirectedPath(Digraph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo =  new int[G.V()];
        for(int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        bfs(G, s);
    }

    private void bfs(Digraph G, int s) {
        LinkedList<Integer> q = new LinkedList<Integer>();
        marked[s] = true;
        distTo[s] = 0;
        q.addLast(s);
        while (!q.isEmpty()) {
            int v = q.pollFirst();
            for(int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.addLast(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int distTo(int v) {
        return distTo[v];
    }
}
