package com.inova.portal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.inova.portal.model.City;
import com.inova.portal.repo.CityRepository;
import com.inova.portal.repo.CoordinateRepository;
import com.inova.portal.repo.NeighborhoodRepository;

@RunWith(SpringRunner.class)
public class CityServiceImplTest {

	@TestConfiguration
    static class CityServiceImplTestContextConfiguration {
  
        @Bean
        public CityService cityService() {
            return new CityServiceImpl();
        }
    }
	
	@Autowired
    private CityServiceImpl cityService;
 
    @MockBean
    private CityRepository cityRepository;
    
    @MockBean
    private CoordinateRepository coordinateRepository;
    
    @MockBean
    private NeighborhoodRepository neighborRepository;
	
    @Before
    public void setUp() {
		City bh = Mockito.mock(City.class);
		City contagem = Mockito.mock(City.class);
		City sabara = Mockito.mock(City.class);

        Mockito.when(cityRepository.findAll()).thenReturn(Lists.newArrayList(bh, contagem, sabara));
    }
    
	@Test
	public void findAll() {
		List<City> cities = cityService.getAllCities();
		assertThat(cities.size()).isEqualTo(3);
	}

}
