package com.inova.portal.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Finder {

	private Graph graph;
	private Long from;
	private Long destiny;
	private List<Path> paths = new ArrayList<Path>();
	
	public Finder(Graph graph, Long from, Long destiny) {
		this.graph = graph;
		this.from = from;
		this.destiny = destiny;
	}
	
	private List<Path> find() {
		LinkedList<Towards> visited = new LinkedList<Towards>();
        visited.add(new Towards(from, 0.0, from));
        
        depthFirst(graph, visited);
        
        return paths;
	}
	
	private void depthFirst(Graph graph, LinkedList<Towards> visited) {
        LinkedList<Towards> nodes = graph.adjacentNodes(visited.getLast());
        
        for (Towards node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.destiny.equals(destiny)) {
                visited.add(node);
                generateResults(visited);
                visited.removeLast();
                break;
            }
        }
        for (Towards node : nodes) {
            if (visited.contains(node) || node.destiny.equals(destiny)) {
                continue;
            }
            visited.addLast(node);
            depthFirst(graph, visited);
            visited.removeLast();
        }
    }

    private void generateResults(LinkedList<Towards> visited) {
    	Path path = new Path();
        for (Towards node : visited) {
            path.cities.add(node.destiny);
            path.minDistance += node.distance;
        }
        
        paths.add(path);
    }

	public Path findShortest() {
		List<Path> allPaths = find();
		
		return Collections.min(allPaths, Comparator.comparing(Path::getMinDistance));
	}
}
