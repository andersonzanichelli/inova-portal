package com.inova.portal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.apache.commons.math3.util.Precision;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Coordinate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull(message = "Latitude is required")
	@JsonProperty
	private Double lat;
	
	@NotNull(message = "Longitude is required")
	@JsonProperty("long")
	private Double lng;
	
	public Coordinate() {}
	
	public Coordinate(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLng() {
		return lng;
	}

	public Double calculateDistance(City from) {
		double deltaLat = Math.toRadians(from.getCoordinate().getLat() - this.getLat());
	    double deltaLon = Math.toRadians(from.getCoordinate().getLng() - this.getLng());
	    double distance = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) + Math.cos(Math.toRadians(this.getLat())) * Math.cos(Math.toRadians(from.getCoordinate().getLat())) * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
	    distance = 2 * Math.atan2(Math.sqrt(distance), Math.sqrt(1-distance));
	    
	    return Precision.round(6371.0 * distance, 1);
	}
}
