package com.inova.portal.service;

public class AdditionNeighborException extends RuntimeException {

	private static final long serialVersionUID = -9086255952834888198L;
	
	public AdditionNeighborException() {
		super("City or neighbor not present");
	}
}
