package com.inova.portal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.inova.portal.controller.CityControllerTest;
import com.inova.portal.controller.CityToJsonTest;
import com.inova.portal.repo.CityRepositoryTest;
import com.inova.portal.repo.CoordinateRepositoryTest;
import com.inova.portal.service.CityServiceImplTest;
import com.inova.portal.util.FinderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CityToJsonTest.class,
    CityControllerTest.class,
    CityRepositoryTest.class,
    CityServiceImplTest.class,
    CoordinateRepositoryTest.class,
    FinderTest.class
})
public class TestSuite {

}
