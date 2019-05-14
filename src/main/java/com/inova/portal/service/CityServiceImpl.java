package com.inova.portal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.portal.model.City;
import com.inova.portal.model.Neighborhood;
import com.inova.portal.repo.CityRepository;
import com.inova.portal.repo.CoordinateRepository;
import com.inova.portal.repo.NeighborhoodRepository;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
    private CityRepository cityRepository;
	
	@Autowired
	private CoordinateRepository coordinateRepository;
	
	@Autowired
	private NeighborhoodRepository neighborhoodRepository;
	
	public City insertCity(City city) {
		city.setCoordinates(coordinateRepository.saveAndFlush(city.getCoordinate()));
		return cityRepository.saveAndFlush(city);
	}
	
	@Override
	public List<City> getAllCities() {
		return cityRepository.findAll();
	}

	public City updateCity(Long id, City source) throws UpdateCityException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		City deepCopy = null;
		
		try {
			deepCopy = objectMapper.readValue(objectMapper.writeValueAsString(source), City.class);
			deepCopy.setId(id);
		} catch(Exception ex) {
			throw new UpdateCityException(ex.getMessage());
		}
		
		deepCopy.setCoordinates(coordinateRepository.saveAndFlush(deepCopy.getCoordinate()));
		
		return cityRepository.saveAndFlush(deepCopy);
	}

	public void deleteCity(Long id) {
		cityRepository.deleteById(id);
	}

	public City addNeighbor(Long id, Long neighborId) {
		Optional<City> c = cityRepository.findById(id);
		Optional<City> n = cityRepository.findById(neighborId);
		
		if(c.isPresent() && n.isPresent()) {
			City city = c.get();
			City neighbor = n.get();

			Double distance = neighbor.getCoordinate().calculateDistance(city);
			Neighborhood neighborhood = new Neighborhood(city.getId(), neighbor.getId(), distance);
	
			neighborhoodRepository.saveAndFlush(neighborhood);
			
			return city;
		}
		
		throw new AdditionNeighborException();
	}
}
