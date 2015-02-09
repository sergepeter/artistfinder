package com.music.dbpedia.finder.beans;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hp.hpl.jena.rdf.model.Resource;

public class Artist {

	@JsonIgnore
	private Resource artist;

	private String name;
	private String birthDate;
	
	@JsonIgnore
	private Resource birthPlace;
	
	private String shortDescription;
	private String abstractStr;
	private String yearsActive;
	private URL website;

	public Artist() {
		super();
	}

	public Artist(Resource artist, String name, String birthDate,
			Resource birthPlace, String shortDescription, String abstractStr,
			String yearsActive, URL website) {
		super();
		this.artist = artist;
		this.name = name;
		this.birthDate = birthDate;
		this.birthPlace = birthPlace;
		this.shortDescription = shortDescription;
		this.abstractStr = abstractStr;
		this.yearsActive = yearsActive;
		this.website = website;
	}

	@Override
	public String toString() {
		return "Artist [artist=" + artist + ", name=" + name + ", birthDate="
				+ birthDate + ", birthPlace=" + birthPlace + ", yearsActive="
				+ yearsActive + ", website=" + website + "]";
	}

	public Resource getArtist() {
		return artist;
	}

	public void setArtist(Resource artist) {
		this.artist = artist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Resource getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(Resource birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getAbstractStr() {
		return abstractStr;
	}

	public void setAbstractStr(String abstractStr) {
		this.abstractStr = abstractStr;
	}

	public URL getWebsite() {
		return website;
	}

	public void setWebsite(URL website) {
		this.website = website;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getYearsActive() {
		return yearsActive;
	}

	public void setYearsActive(String yearsActive) {
		this.yearsActive = yearsActive;
	}

	
	public String getBirthPlaceURI() {
		if (birthPlace != null && birthPlace instanceof Resource){
			return birthPlace.getURI();
		} else {
			return null;
		}
	}
	
	public String getArtistURI() {
		if (artist != null && artist instanceof Resource){
		return artist.getURI();
		} else {
			return null;
		}
	}
	
	
}
