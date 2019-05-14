package com.inova.portal.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class City {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotBlank(message = "Name is required")
	@JsonProperty
	private String name;
	
	@JsonProperty
	private Integer population;
	@JsonProperty
	private LocalDate foundationDate;
	
	@OneToOne
    @JoinColumn(name = "coordinate_id")
	private Coordinate coordinate;
	
	@OneToMany
	@JsonProperty("neighboors")
	private List<Neighborhood> neighborhood = new ArrayList<Neighborhood>();
	
	public City() {}
	
	public City(String name, Coordinate coordinate) {
		this.name = name;
		this.coordinate = coordinate;
	}

	public City(String name, Coordinate coordinate, int population, String foundationDate) {
		this.name = name;
		this.coordinate = coordinate;
		this.population = population;
		this.foundationDate = LocalDate.parse(foundationDate);
	}

	public String getName() {
		return name;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public void setfoundationDate(String fondationDate) {
		this.foundationDate = LocalDate.parse(fondationDate);
	}

	public List<Neighborhood> getNeighboors() {
		return neighborhood;
	}

	public Integer getPopulation() {
		return this.population;
	}

	public String getFoundationDate() {
		return this.foundationDate != null ? this.foundationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void removeNeighbor(City neighbor) {
		Neighborhood toRemove = null;
		for (Neighborhood n : neighborhood) {
			if (n.getNeighbor().equals(neighbor.getId()))
				toRemove = n;
		}
		
		neighborhood.remove(toRemove);
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinates(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
