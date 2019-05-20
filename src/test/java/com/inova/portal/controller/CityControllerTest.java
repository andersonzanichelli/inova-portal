package com.inova.portal.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.inova.portal.model.City;
import com.inova.portal.model.Coordinate;
import com.inova.portal.model.Neighborhood;
import com.inova.portal.service.CityOrNeighborNotFoundException;
import com.inova.portal.service.CityServiceImpl;
import com.inova.portal.service.UpdateCityException;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private CityServiceImpl cityService;
	
	private JacksonTester<City> jacksonCity;
	ObjectMapper mapper = new ObjectMapper();
	
	@Before
    public void setup() {
        JacksonTester.initFields(this, mapper);
    }
	
	@Test
	@SuppressWarnings("deprecation")
	public void insertCity() throws IOException, Exception {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		City city = new City("Belo Horizonte", bhCoordinate, 2523794, "1897-12-12");
		
		Mockito.when(cityService.insertCity(org.mockito.Matchers.any(City.class))).thenReturn(city);
		String jsonCity = new CityToJson(city).generate();
		
		MockHttpServletResponse response = mvc.perform(post("/api/v1/city")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(jsonCity))
				.andExpect(status().isOk())
	            .andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(jacksonCity.write(city).getJson());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void errorOnInsert() throws Exception {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		City city = new City("Belo Horizonte", bhCoordinate, 2523794, "1897-12-12");
		
		Mockito.when(cityService.insertCity(org.mockito.Matchers.any(City.class))).thenThrow(RuntimeException.class);
		String jsonCity = new CityToJson(city).generate();
		
		mvc.perform(post("/api/v1/city")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(jsonCity))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void updateCity() throws Exception {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		City city = new City("BH", bhCoordinate, 2523800, "1897-12-12");
		city.setId(10l);
		
		Mockito.when(cityService.updateCity(org.mockito.Matchers.any(Long.class), org.mockito.Matchers.any(City.class))).thenReturn(city);
		String jsonCity = new CityToJson(city).generate();
		
		MockHttpServletResponse response = mvc.perform(put("/api/v1/city/10")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(jsonCity))
				.andExpect(status().isOk())
	            .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(jacksonCity.write(city).getJson());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void errorOnUpdate() throws Exception {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		City city = new City("BH", bhCoordinate, 2523800, "1897-12-12");
		city.setId(10l);
		
		Mockito.when(cityService.updateCity(org.mockito.Matchers.any(Long.class), org.mockito.Matchers.any(City.class))).thenThrow(UpdateCityException.class);
		
		String jsonCity = new CityToJson(city).generate();
		
		mvc.perform(put("/api/v1/city/10")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(jsonCity))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void findAllCities() throws Exception {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		City bh = new City("Belo Horizonte", bhCoordinate, 2523794, "1897-12-12");
		
		Coordinate contagemCoordinate = new Coordinate(-19.9094429, -44.0972261);
		City contagem = new City("Contagem", contagemCoordinate);
		
		Coordinate sabaraCoordinate = new Coordinate(-19.8873791, -43.8649765);
		City sabara = new City("Sabará", sabaraCoordinate);

		Mockito.when(cityService.getAllCities()).thenReturn(Lists.newArrayList(bh, contagem, sabara));
		
		MockHttpServletResponse response = mvc.perform(get("/api/v1/city")
			      .contentType(MediaType.APPLICATION_JSON_VALUE))
			      .andExpect(jsonPath("$").exists())
			      .andExpect(jsonPath("$", Matchers.hasSize(3)))
			      .andReturn().getResponse();
		
		String resp = "[" +
						"{" +
							"\"id\":null," +
							"\"name\":\"Belo Horizonte\"," + 
							"\"population\":2523794," + 
							"\"foundationDate\":\"1897-12-12\","+
							"\"coordinate\":{"+ 
								"\"lat\":-19.9069359," + 
								"\"long\":-43.9758943" +
							"}," + 
							"\"neighboors\":[]" +
						"},{"+
							"\"id\":null,"+ 
							"\"name\":\"Contagem\"," +
							"\"population\":null," +
							"\"foundationDate\":null," +
							"\"coordinate\":{" +
								"\"lat\":-19.9094429," +
								"\"long\":-44.0972261" +
							"}," +
							"\"neighboors\":[]" +
						"},{" +
							"\"id\":null," + 
							"\"name\":\"Sabará\"," + 
							"\"population\":null," + 
							"\"foundationDate\":null," + 
							"\"coordinate\":{" + 
								"\"lat\":-19.8873791," + 
								"\"long\":-43.8649765" + 
							"}," +
							"\"neighboors\":[]" + 
						"}" +
					"]";
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(resp);
	}

	@Test
	public void errorOnList() throws Exception {
		Mockito.when(cityService.getAllCities()).thenThrow(RuntimeException.class);
		
		mvc.perform(get("/api/v1/city")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteCity() throws Exception {
		mvc.perform(delete("/api/v1/city/10")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void errorOnDelete() throws Exception {
		Mockito.doThrow(new RuntimeException()).doNothing().when(cityService).deleteCity(org.mockito.Matchers.any(Long.class));
		
		mvc.perform(delete("/api/v1/city/10")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void addNeighbor() throws Exception {
		Coordinate bhCoordinate = new Coordinate(-19.9069359, -43.9758943);
		City bh = new City("Belo Horizonte", bhCoordinate, 2523794, "1897-12-12");
		bh.setId(10l);
		Neighborhood neighbor = new Neighborhood(10l, 12l, 12.7);
		bh.setNeighborhood(Lists.newArrayList(neighbor));
		
		Mockito.when(cityService.addNeighbor(10l, 12l)).thenReturn(bh);
		
		MockHttpServletResponse response = mvc.perform(put("/api/v1/city/10/neighboor/12")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
	            .andReturn().getResponse();
		
		String resp = "{" +
					"\"id\":10," +
					"\"name\":\"Belo Horizonte\"," + 
					"\"population\":2523794," + 
					"\"foundationDate\":\"1897-12-12\","+
					"\"coordinate\":{"+ 
						"\"lat\":-19.9069359," + 
						"\"long\":-43.9758943" +
					"}," + 
					"\"neighboors\":[" +
						"{\"id\":12,\"distance\":12.7}" +
					"]" +
				"}";
		
		assertThat(response.getContentAsString()).isEqualTo(resp);
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void errorOnAddNeighbor() throws Exception {
		Mockito.when(cityService.addNeighbor(org.mockito.Matchers.any(Long.class), org.mockito.Matchers.any(Long.class))).thenThrow(CityOrNeighborNotFoundException.class);
		
		mvc.perform(put("/api/v1/city/10/neighboor/12")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
