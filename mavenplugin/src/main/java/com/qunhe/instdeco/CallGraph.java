package com.qunhe.instdeco;

import java.util.HashMap;

/**
 * @author shengxun
 * @date 12/24/18 10:37 PM
 */
public class CallGraph {

    private static final int SIZE = 16;

    private HashMap<String, Integer> mHash = new HashMap<>();
    private Integer count = 0;
    private Node[] edges = new Node[SIZE];
    private static final Node NULL = new Node("", 0);

    private static class Node {
        private String pattern;
        private Integer vertex;
        private Node next = null;

        Node(String pattern, Integer vertex) {
            this.pattern = pattern;
            this.vertex = vertex;
            this.next = this;
        }

        Node(String pattern, Integer vertex, Node next) {
            this.pattern = pattern;
            this.vertex = vertex;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return pattern.hashCode();
        }
    }

    private Node start = NULL;

    public CallGraph(String initialPattern) {
        start = new Node(initialPattern, getId(initialPattern), NULL);
    }


    public void add(String from, String to) {
        Integer u = getId(from);
        Integer v = getId(to);
        Node node = new Node(to, v, edges[u].next);
        edges[u].next = node;
    }

    public Integer getId(String pattern) {
        if (mHash.containsKey(pattern)) {
            return mHash.get(pattern);
        }
        ++count;
        int size = edges.length;
        if (size >= count) {
            Node[] newEdges = new Node[size << 1];
            System.arraycopy(edges, 0, newEdges, 0, size);
            edges = newEdges;
        }
        edges[count] = new Node(pattern, count, NULL);
        mHash.put(pattern, count);
        return count;
    }

}
