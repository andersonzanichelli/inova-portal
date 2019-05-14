package com.inova.portal.controller;

import com.google.common.base.Joiner;
import com.inova.portal.model.City;

public class CityToJson {

	private City city;

	public CityToJson(City city) {
		this.city = city;
	}

	public String generate() {
		StringBuilder json = new StringBuilder();
		json.append("{")
		.append("\"id\":").append(city.getId()).append(",")
		.append("\"name\":\"").append(city.getName()).append("\",")
		.append("\"population\":").append(city.getPopulation()).append(",")
		.append("\"foundationDate\":\"").append(city.getFoundationDate()).append("\",")
		.append("\"coordinate\":{")
		.append("\"lat\":").append(city.getCoordinate().getLat()).append(",")
		.append("\"long\":").append(city.getCoordinate().getLng())
		.append("},")
		.append("\"neighbors\":[")
		.append(addNeighbors())
		.append("]")
		.append("}");
		
		return json.toString();
	}

	private String addNeighbors() {
		return Joiner.on(",").join(city.getNeighboors());
	}

}
