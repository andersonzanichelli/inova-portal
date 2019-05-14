package com.inova.portal.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.inova.portal.model.City;
import com.inova.portal.model.Coordinate;
import com.inova.portal.model.Neighborhood;

public class CityToJsonTest {

	@Test
	public void test() {
		City city = Mockito.mock(City.class);
		Neighborhood contagem = Mockito.mock(Neighborhood.class);
		Neighborhood sabara = Mockito.mock(Neighborhood.class);
		Coordinate coordinate = Mockito.mock(Coordinate.class);
		
		Mockito.when(coordinate.getLat()).thenReturn(-19.9069359);
		Mockito.when(coordinate.getLng()).thenReturn(-43.9758943);
		
		Mockito.when(city.getId()).thenReturn(10l);
		Mockito.when(city.getName()).thenReturn("Belo Horizonte");
		Mockito.when(city.getPopulation()).thenReturn(2523794);
		Mockito.when(city.getFoundationDate()).thenReturn("1897-12-12");
		Mockito.when(city.getCoordinate()).thenReturn(coordinate);
		
		Mockito.when(contagem.toString()).thenReturn("{\"id\":11,\"distance\":12.7}");
		Mockito.when(sabara.toString()).thenReturn("{\"id\":12,\"distance\":11.8}");
		
		
		Mockito.when(city.getNeighboors()).thenReturn(Lists.newArrayList(contagem, sabara));

		String response = new CityToJson(city).generate();
		
		String resp = "{" +
					"\"id\":10," +
					"\"name\":\"Belo Horizonte\"," + 
					"\"population\":2523794," + 
					"\"foundationDate\":\"1897-12-12\","+
					"\"coordinate\":{"+ 
						"\"lat\":-19.9069359," + 
						"\"long\":-43.9758943" +
					"}," + 
					"\"neighbors\":[" +
						"{\"id\":11,\"distance\":12.7}," +
						"{\"id\":12,\"distance\":11.8}" +
					"]" +
				"}";
		
		assertThat(response).isEqualTo(resp);
	}

}
