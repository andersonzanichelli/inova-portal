package com.inova.portal.repo;

public class CityWithOnlyOneNeighborException extends RuntimeException {
	private static final long serialVersionUID = -1496610106391744388L;

	public CityWithOnlyOneNeighborException() {
		super("Only cities with only one neighbor can be removed");
	}
}
