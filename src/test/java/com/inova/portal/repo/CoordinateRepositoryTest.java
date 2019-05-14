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

import com.inova.portal.model.Coordinate;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CoordinateRepositoryTest {
	
	@Autowired
	private CoordinateRepository coordinateRepository;

	@Test
	public void findAll() {
		Coordinate coordinate = new Coordinate(-19.9069359, -43.9758943);
		coordinateRepository.save(coordinate);
		
		List<Coordinate> coordinates = coordinateRepository.findAll();
		
		assertThat(coordinates.size()).isEqualTo(1);
		assertThat(coordinates.get(0).getLat()).isEqualTo(-19.9069359);
		assertThat(coordinates.get(0).getLng()).isEqualTo(-43.9758943);
	}
	
	@Test
	public void constraintViolationNullLatitude() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Coordinate coordinate = new Coordinate(null, -43.9758943);
		Set<ConstraintViolation<Coordinate>> violations = validator.validate(coordinate);
		  
	    assertThat(violations.size()).isEqualTo(1);
	    long qtdMessage = violations.stream().filter(violation -> violation.getMessage().equals("Latitude is required")).count();
	    assertThat(qtdMessage).isEqualTo(1);
	}
	
	@Test
	public void constraintViolationNullLongitude() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Coordinate coordinate = new Coordinate(19.9069359, null);
		Set<ConstraintViolation<Coordinate>> violations = validator.validate(coordinate);
		  
	    assertThat(violations.size()).isEqualTo(1);
	    long qtdMessage = violations.stream().filter(violation -> violation.getMessage().equals("Longitude is required")).count();
	    assertThat(qtdMessage).isEqualTo(1);
	}

	@Test
	public void constraintViolationNullCoordinates() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Coordinate coordinate = new Coordinate(null, null);
		Set<ConstraintViolation<Coordinate>> violations = validator.validate(coordinate);
		  
	    assertThat(violations.size()).isEqualTo(2);
	    long qtdMessage = violations.stream().filter(violation -> violation.getMessage().contains("is required")).count();
	    assertThat(qtdMessage).isEqualTo(2);
	}
}
