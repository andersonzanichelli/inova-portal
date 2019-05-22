package com.inova.portal.util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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

    public boolean isConnected(Long from, Towards to) {
        Set<Towards> adjacent = map.get(from);
        if(adjacent==null) {
            return false;
        }
        return adjacent.contains(to);
    }

    public LinkedList<Towards> adjacentNodes(Towards last) {
        LinkedHashSet<Towards> adjacent = map.get(last.destiny);
        if(adjacent==null) {
            return new LinkedList<Towards>();
        }
        return new LinkedList<Towards>(adjacent);
    }
}
