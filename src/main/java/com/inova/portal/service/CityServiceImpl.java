package com.inova.portal.service;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inova.portal.model.City;
import com.inova.portal.model.Neighborhood;
import com.inova.portal.repo.CityRepository;
import com.inova.portal.repo.CoordinateRepository;
import com.inova.portal.repo.NeighborhoodRepository;
import com.inova.portal.util.Finder;
import com.inova.portal.util.Graph;
import com.inova.portal.util.Path;
import com.inova.portal.util.Towards;

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

	public Path shortestpath(Long id, Long toCity) {
		Path path = new Path();
		Optional<City> c = cityRepository.findById(id);
		Optional<City> to = cityRepository.findById(toCity);
		
		if(c.isPresent() && to.isPresent()) {
			City city = c.get();
			City destiny = to.get();
			
			if(city.equals(destiny))
				return path;

			if(city.getNeighboors().isEmpty())
				return path;
			
			List<Neighborhood> neighborhood = neighborhoodRepository.findAll();
			List<Towards> towards = new ArrayList<Towards>();
			
			for (Neighborhood neighbor : neighborhood) {
				towards.add(new Towards(neighbor.getCity(), neighbor.getDistance(), neighbor.getNeighbor()));
			}
			
			Graph graph = new Graph();
			
			for (Towards toward : towards) {
				graph.addEdge(toward.from, toward);
			}
			
			return new Finder(graph, id, toCity).findShortest();
		}
		
		
		return path;
	}
}
