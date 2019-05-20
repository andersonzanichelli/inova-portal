package com.inova.portal.service;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public City updateCity(Long id, City newData) throws UpdateCityException {
		Optional<City> city = cityRepository.findById(id);
		
		if(!city.isPresent())
			throw new UpdateCityException("City not found!");
		
		City toUpdate = city.get();
		
		if(!isNullOrEmpty(newData.getName()))
			toUpdate.setName(newData.getName());
		
		if(!isNullOrEmpty(newData.getFoundationDate()))
			toUpdate.setFoundationDate(LocalDate.parse(newData.getFoundationDate()));
		
		if(newData.getPopulation() != null)
			toUpdate.setPopulation(newData.getPopulation());
		
		if(newData.getCoordinate() != null) {
			if(newData.getCoordinate().getLat() != null && newData.getCoordinate().getLng() != null)
				toUpdate.setCoordinate(coordinateRepository.saveAndFlush(newData.getCoordinate()));
		}
		
		if(!newData.getNeighboors().isEmpty())
			for (Neighborhood neighbor : newData.getNeighboors())
				toUpdate.addNeighbor(neighborhoodRepository.saveAndFlush(neighbor));
		
		return cityRepository.saveAndFlush(toUpdate);
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
			
			city.addNeighbor(neighborhood);
			
			return cityRepository.saveAndFlush(city);
		}
		
		throw new CityOrNeighborNotFoundException();
	}

	public City removeNeighbor(Long id, Long neighborId) {
		Optional<City> c = cityRepository.findById(id);
		
		if(c.isPresent()) {
			City city = c.get();

			Neighborhood toRemove = null;
			for (Neighborhood neigh : city.getNeighboors()) {
				if (neigh.getNeighbor() == neighborId) {
					toRemove = neigh;
					break;
				}
			}
			
			city.getNeighboors().remove(toRemove);
	
			neighborhoodRepository.delete(toRemove);
			
			return cityRepository.saveAndFlush(city);
		}
		
		throw new CityOrNeighborNotFoundException();
	}
}
