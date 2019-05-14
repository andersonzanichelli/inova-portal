package com.inova.portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inova.portal.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
	
	default void deleteById(Long id) {
		Optional<City> city = findById(id);
		
		if (city.isPresent() && city.get().getNeighboors().size() != 1)
			throw new CityWithOnlyOneNeighborException();
		
		if (city.isPresent())
			delete(city.get());
	}
}
