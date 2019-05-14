package com.inova.portal.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.inova.portal.model.Neighborhood;

public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {

	public List<Neighborhood> findByCity(Long cityId);
}
