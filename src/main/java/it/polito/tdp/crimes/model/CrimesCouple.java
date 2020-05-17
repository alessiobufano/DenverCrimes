package it.polito.tdp.crimes.model;

import java.util.*;

public class CrimesCouple implements Comparable<CrimesCouple>{
	
	private String crime1;
	private String crime2;
	private int neighborhoodNumber;
	
	public CrimesCouple(String crime1, String crime2, int neighborhoodNumber) {
		super();
		this.crime1 = crime1;
		this.crime2 = crime2;
		this.neighborhoodNumber = neighborhoodNumber;
	}

	public String getCrime1() {
		return crime1;
	}

	public void setCrime1(String crime1) {
		this.crime1 = crime1;
	}

	public String getCrime2() {
		return crime2;
	}

	public void setCrime2(String crime2) {
		this.crime2 = crime2;
	}

	public int getNeighborhoodNumber() {
		return neighborhoodNumber;
	}

	public void setNeighborhoodNumber(int neighborhoodNumber) {
		this.neighborhoodNumber = neighborhoodNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crime1 == null) ? 0 : crime1.hashCode());
		result = prime * result + ((crime2 == null) ? 0 : crime2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrimesCouple other = (CrimesCouple) obj;
		if (crime1 == null) {
			if (other.crime1 != null)
				return false;
		} else if (!crime1.equals(other.crime1))
			return false;
		if (crime2 == null) {
			if (other.crime2 != null)
				return false;
		} else if (!crime2.equals(other.crime2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + crime1 + ", " + crime2 + "]";
	}

	@Override
	public int compareTo(CrimesCouple cc2) {
		return this.neighborhoodNumber - cc2.neighborhoodNumber;
	}

}
