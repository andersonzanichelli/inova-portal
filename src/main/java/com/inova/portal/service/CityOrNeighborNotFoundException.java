package com.inova.portal.service;

public class CityOrNeighborNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -9086255952834888198L;
	
	public CityOrNeighborNotFoundException() {
		super("City or neighbor not present");
	}
}
