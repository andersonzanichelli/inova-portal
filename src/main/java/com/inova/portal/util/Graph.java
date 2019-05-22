package com.inova.portal.util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

public class Graph {

	private Map<Long, LinkedHashSet<Towards>> map = new HashMap<Long, LinkedHashSet<Towards>>();

    public void addEdge(Long from, Towards to) {
        LinkedHashSet<Towards> adjacent = map.get(from);
        
        if(adjacent==null) {
            adjacent = new LinkedHashSet<Towards>();
            map.put(from, adjacent);
        }
        adjacent.add(to);
    }

    public LinkedList<Towards> adjacentNodes(Towards last) {
        LinkedHashSet<Towards> adjacent = map.get(last.destiny);
        if(adjacent==null) {
            return new LinkedList<Towards>();
        }
        return new LinkedList<Towards>(adjacent);
    }
}
