package com.inova.portal.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import com.inova.portal.model.Neighborhood;
import com.inova.portal.util.Finder;
import com.inova.portal.util.Graph;
import com.inova.portal.util.Path;
import com.inova.portal.util.Towards;

public class FinderTest {

	private List<Neighborhood> neighborhood = new ArrayList<Neighborhood>();
	private Graph graph;
	
	@Before
	public void before() {
		neighborhood.add(new Neighborhood(1l, 2l, 100.0));
		neighborhood.add(new Neighborhood(1l, 4l, 500.0));
		neighborhood.add(new Neighborhood(1l, 3l, 200.0));
		neighborhood.add(new Neighborhood(2l, 4l, 500.0));
		neighborhood.add(new Neighborhood(2l, 8l, 100.0));
		neighborhood.add(new Neighborhood(2l, 9l, 500.0));
		neighborhood.add(new Neighborhood(3l, 9l, 100.0));
		neighborhood.add(new Neighborhood(3l, 7l, 400.0));
		neighborhood.add(new Neighborhood(4l, 5l, 600.0));
		neighborhood.add(new Neighborhood(4l, 7l, 800.0));
		
		graph = new Graph();
		
		for (Neighborhood neighbor : neighborhood) {
			graph.addEdge(neighbor.getCity(), new Towards(neighbor.getCity(), neighbor.getDistance(), neighbor.getNeighbor()));
		}
	}
	
	@Test
	public void shortestpathFrom1To2() {
		Path shortest = new Finder(graph, 1l, 2l).findShortest();
		assertThat(shortest.minDistance).isEqualTo(100.0);
		assertThat(shortest.cities, CoreMatchers.hasItems(1l,2l));
	}
	
	@Test
	public void shortestpathFrom1To8() {
		Path shortest = new Finder(graph, 1l, 8l).findShortest();
		assertThat(shortest.minDistance).isEqualTo(200.0);
		assertThat(shortest.cities, CoreMatchers.hasItems(1l, 2l, 8l));
	}
	
	@Test
	public void shortestpathFrom1To7() {
		Path shortest = new Finder(graph, 1l, 7l).findShortest();
		assertThat(shortest.minDistance).isEqualTo(600.0);
		assertThat(shortest.cities, CoreMatchers.hasItems(1l, 3l, 7l));
	}

	@Test(expected = NoSuchElementException.class)
	public void notFound() {
		new Finder(graph, 1l, 1000l).findShortest();
	}
}
