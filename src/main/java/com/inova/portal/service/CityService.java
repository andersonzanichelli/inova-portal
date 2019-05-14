package com.inova.portal.service;

import java.util.List;

import com.inova.portal.model.City;

public interface CityService {

	City insertCity(City city);
	City updateCity(Long id, City city) throws UpdateCityException;
	List<City> getAllCities();
	void deleteCity(Long id);
	City addNeighbor(Long id, Long neighborId);
}
