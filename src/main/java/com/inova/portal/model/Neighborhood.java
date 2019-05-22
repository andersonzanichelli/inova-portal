package com.inova.portal.model;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Neighborhood implements Comparator<Neighborhood> {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	private Long city;

	@JsonProperty("id")
	private Long neighbor;
	
	@JsonProperty("distance")
	private Double distance;
	
	public Neighborhood() {}
	
	public Neighborhood(Long city, Long neighbor, Double distance) {
		this.city = city;
		this.neighbor = neighbor;
		this.distance = distance;
	}

	public Double getDistance() {
		return distance;
	}

	public Long getNeighbor() {
		return neighbor;
	}
	
	public Long getCity() {
		return city;
	}
	
	@Override
	public String toString() {
		String json = "{" +
							"\"id\":" + neighbor + "," +
							"\"distance\":" + distance +
						"}";
		return json;
	}

	@Override
	public int compare(Neighborhood n1, Neighborhood n2) {
		
		if (n1.distance < n2.distance) 
            return -1; 
        if (n1.distance > n2.distance) 
            return 1; 
        
        return 0;
	}
}
