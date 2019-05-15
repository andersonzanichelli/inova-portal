package com.inova.portal.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.inova.portal.model.City;
import com.inova.portal.model.Coordinate;
import com.inova.portal.model.Neighborhood;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CoordinateRepository coordinateRepository;
	
	@Autowired
	private NeighborhoodRepository neighborhoodRepository;

	@Test
	public void findAll() {
		Coordinate c = new Coordinate(-19.9069359, -43.9758943);
		Coordinate coordinate = coordinateRepository.saveAndFlush(c);
		
		City bh = new City("Belo Horizonte", coordinate);
		cityRepository.saveAndFlush(bh);
		
		List<City> cities = cityRepository.findAll();
		
		assertThat(cities.size()).isEqualTo(1);
		assertThat(cities.get(0).getName()).isEqualTo("Belo Horizonte");
		assertThat(cities.get(0).getCoordinate().getLat()).isEqualTo(-19.9069359);
		assertThat(cities.get(0).getCoordinate().getLng()).isEqualTo(-43.9758943);
	}

	@Test
	public void findById() {
		Coordinate coordinate = new Coordinate(-19.9069359, -43.9758943);
		coordinateRepository.saveAndFlush(coordinate);
		
		City bh = new City("Belo Horizonte", coordinate);
		
		City city = cityRepository.saveAndFlush(bh);
		
		assertThat(city).isNotNull();
		assertThat(city.getId()).isNotNull();
		assertThat(city.getName()).isEqualTo("Belo Horizonte");
		assertThat(city.getCoordinate().getLat()).isEqualTo(-19.9069359);
		assertThat(city.getCoordinate().getLng()).isEqualTo(-43.9758943);
	}
	
	@Test
	public void constraintViolationBlankName() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Coordinate coordinate = new Coordinate(-19.9069359, -43.9758943);
		coordinateRepository.saveAndFlush(coordinate);
		
		City city = new City("", coordinate);
		Set<ConstraintViolation<City>> violations = validator.validate(city);
		  
	    assertThat(violations.size()).isEqualTo(1);
	    long qtdMessage = violations.stream().filter(violation -> violation.getMessage().equals("Name is required")).count();
	    assertThat(qtdMessage).isEqualTo(1);
	}
	
	@Test
	public void completeData() {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		coordinateRepository.saveAndFlush(bhCoordinate);
		
		Coordinate contagemCoordinate = new Coordinate(-19.9094429, -44.0972261);
		coordinateRepository.saveAndFlush(contagemCoordinate);
		
		Coordinate sabaraCoordinate = new Coordinate(-19.8873791, -43.8649765);
		coordinateRepository.saveAndFlush(sabaraCoordinate);
		
		City bh = new City("Belo Horizonte", bhCoordinate);
		bh.setPopulation(2523794);
		bh.setfoundationDate("1897-12-12");
		
		City city = cityRepository.saveAndFlush(bh);
		
		assertThat(city).isNotNull();
		assertThat(city.getId()).isNotNull();
		assertThat(city.getName()).isEqualTo("Belo Horizonte");
		assertThat(city.getCoordinate().getLat()).isEqualTo(-19.9069359);
		assertThat(city.getCoordinate().getLng()).isEqualTo(-43.9758943);
		assertThat(city.getPopulation()).isEqualTo(2523794);
		assertThat(city.getFoundationDate()).isEqualTo("1897-12-12");
	}
	
	@Test
	public void updateCity() {
		Coordinate coordinate = new Coordinate(-19.9069359, -43.9758943);
		coordinateRepository.saveAndFlush(coordinate);
		
		City bh = new City("Belo Horizonte", coordinate);
		City city = cityRepository.saveAndFlush(bh);

		assertThat(city).isNotNull();
		assertThat(city.getId()).isNotNull();
		assertThat(city.getName()).isEqualTo("Belo Horizonte");
		assertThat(city.getCoordinate().getLat()).isEqualTo(-19.9069359);
		assertThat(city.getCoordinate().getLng()).isEqualTo(-43.9758943);
		assertThat(city.getPopulation()).isNull();
		
		city.setName("BH");
		city.setPopulation(2523794);
		
		City cityUpdated = cityRepository.findById(city.getId()).get();
		assertThat(cityUpdated).isNotNull();
		assertThat(cityUpdated.getName()).isEqualTo("BH");
		assertThat(cityUpdated.getCoordinate().getLat()).isEqualTo(-19.9069359);
		assertThat(cityUpdated.getCoordinate().getLng()).isEqualTo(-43.9758943);
		assertThat(cityUpdated.getPopulation()).isEqualTo(2523794);
	}
	
	@Test(expected = CityWithOnlyOneNeighborException.class)
	public void throwExceptionWhenTryToDeleteCityWithMoreThanOneNeighbor() {
		Coordinate coordinate = new Coordinate(-19.9069359, -43.9758943);
		coordinate = coordinateRepository.saveAndFlush(coordinate);
		
		City city = new City("Belo Horizonte", coordinate);
		Neighborhood contagem = new Neighborhood(10l, 12l, 12.7);
		Neighborhood sabara = new Neighborhood(10l, 13l, 11.8);
		contagem = neighborhoodRepository.saveAndFlush(contagem);
		sabara = neighborhoodRepository.saveAndFlush(sabara);
		city.setNeighborhood(Lists.newArrayList(contagem, sabara));
		
		cityRepository.saveAndFlush(city);
		
		cityRepository.deleteById(city.getId());
	}
	
	@Test(expected = CityWithOnlyOneNeighborException.class)
	public void throwExceptionWhenTryToDeleteCityWithoutNeighbors() {
		Coordinate c = new Coordinate(-19.9069359, -43.9758943);
		Coordinate coordinate = coordinateRepository.saveAndFlush(c);
		
		City city = new City("Belo Horizonte", coordinate);
		cityRepository.saveAndFlush(city);
		
		cityRepository.deleteById(city.getId());
	}
}
