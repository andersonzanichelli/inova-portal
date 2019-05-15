package com.inova.portal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Neighborhood {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@SuppressWarnings("unused")
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
	
	@Override
	public String toString() {
		String json = "{" +
							"\"id\":" + neighbor + "," +
							"\"distance\":" + distance +
						"}";
		return json;
	}
}
