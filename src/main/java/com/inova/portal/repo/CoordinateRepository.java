package com.inova.portal.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inova.portal.model.Coordinate;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

}
