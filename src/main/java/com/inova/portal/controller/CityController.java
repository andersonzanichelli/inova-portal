package com.inova.portal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.inova.portal.model.City;
import com.inova.portal.service.CityServiceImpl;
import com.inova.portal.service.UpdateCityException;
import com.inova.portal.util.Path;

@RestController
@RequestMapping("/api")
public class CityController {
	
	@Autowired
    private CityServiceImpl cityService;
	
	@PostMapping(path = "v1/city", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<City> insertCity(@RequestBody @Valid City city) {
		
		City insertedCity = null;
		try {
			insertedCity = cityService.insertCity(city);
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body(city);
		}
		
		return ResponseEntity.ok().body(insertedCity);
	}

	@GetMapping(path = "v1/city", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<City>> getAllCities() {
		try {
			return ResponseEntity.ok().body(cityService.getAllCities());			
		} catch(Exception ex) {
			List<City> cities = Lists.newArrayList();
			return ResponseEntity.badRequest().body(cities);
		}
	}
	
	@PutMapping(path = "v1/city/{city-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<City> updateCity(@PathVariable("city-id") Long id, @RequestBody @Valid City city) {
		City updatedCity = null;
		
		try {
			updatedCity = cityService.updateCity(id, city);
		} catch(UpdateCityException ex) {
			return ResponseEntity.badRequest().body(city);
		}
		
		return ResponseEntity.ok().body(updatedCity);
	}
	
	@DeleteMapping(path = "v1/city/{city-id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteCity(@PathVariable("city-id") Long id) {
		try {
			cityService.deleteCity(id);
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok().body(null);
	}
	
	@PutMapping(path = "v1/city/{city-id}/neighboor/{neighbor-id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<City> addNeighbor(@PathVariable("city-id") Long id, @PathVariable("neighbor-id") Long neighborId) {
		City city = null;
		
		try {
			city = cityService.addNeighbor(id, neighborId);
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok().body(city);
	}
	
	@DeleteMapping(path = "v1/city/{city-id}/neighboor/{neighbor-id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<City> removeNeighbor(@PathVariable("city-id") Long id, @PathVariable("neighbor-id") Long neighborId) {
		City city = null;
		
		try {
			city = cityService.removeNeighbor(id, neighborId);
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok().body(city);
	}
	
	@RequestMapping(value={"v1/city/shortestpath/{from-city-id}/{to-city-id}"})
	public ResponseEntity<Path> shortestpath(@PathVariable("from-city-id") Long id, @PathVariable("to-city-id") Long toCity) {
		
		Path path = null;
		try {
			path = cityService.shortestpath(id, toCity);
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body(path);
		}
		
		return ResponseEntity.ok().body(path);
	}
}
