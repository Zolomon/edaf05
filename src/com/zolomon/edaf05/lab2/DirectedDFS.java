package com.zolomon.edaf05.lab2;

import java.util.*;

/**
 * Created by zol on 16.8.2014.
 */
public class DirectedDFS {
    private final int s;
    private int[] edgeTo;
    private boolean[] marked;

    public DirectedDFS(Digraph G, int s) {
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        dfs(G, s);

        /*for (int v = 0; v < G.V(); v++)
            if (marked[v])
                System.out.print(v + " ");
        System.out.println();*/
    }

    private void dfs(Digraph G, int v) {
        /*nodes_to_visit.add(s);
        int currentNode;
        while (nodes_to_visit.size() != 0) {
            currentNode = nodes_to_visit.pollFirst();
            if (!marked[currentNode]) {
                marked[currentNode] = true;
                for(int v : G.adj(currentNode)) {
                    edgeTo[currentNode] = v;
                    nodes_to_visit.addLast(v);
                }
            }
        }*/
/*
        // to be able to iterate over each adjacency list, keeping track of which
        // vertex in each adjacency list needs to be explored next
        Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.V()];
        for (int v = 0; v < G.V(); v++)
            adj[v] = G.adj(v).iterator();

        // depth-first search using an explicit stack
        Stack<Integer> stack = new Stack<Integer>();
        marked[s] = true;
        stack.push(s);
        while (!stack.isEmpty()) {
            int v = stack.peek();
            if (adj[v].hasNext()) {
                int w = adj[v].next();
                if (!marked[w]) {
                    // discovered vertex w for the first time
                    marked[w] = true;
                    edgeTo[v] = w;
                    //edgeTo[w] = v;
                    stack.push(w);
                }
            }
            else {
                // v's adjacency list is exhausted
                stack.pop();
            }
        }*/

        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }

        /*marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }*/

    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Stack<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        //path.push(s);
        return path;
    }
}