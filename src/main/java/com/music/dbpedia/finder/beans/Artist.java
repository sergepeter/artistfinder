package com.music.dbpedia.finder.beans;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Artist bean
 * @author speter
 *
 */
public class Artist {

	@JsonIgnore
	private Resource resource;

	private String name;
	private String birthDate;
	private String deathDate;
	private String comment;
	
	private String imageURL;
	
	@JsonIgnore
	private Resource birthPlace;
	
	private String birthPlaceStr;
	private String country;
	
	@JsonIgnore
	private Resource deathPlace;
	
	private String deathPlaceStr;
	
	private String shortDescription;
	private String abstractStr;
	private String yearsActive;
	private URL website;
	
	private List<String> instruments;	
	private List<Artist> associatedArtists;
	private List<Band> associatedBands;
	private List<String> genres;

	public Artist() {
		super();
	}

	public Artist(Resource artist, String name, String birthDate,
			Resource birthPlace, String shortDescription, String abstractStr,
			String yearsActive, URL website) {
		super();
		this.resource = artist;
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
		return "Artist [resource=" + resource + ", name=" + name
				+ ", associatedArtists=" + associatedArtists
				+ ", associatedBands=" + associatedBands + ", genres=" + genres
				+ "]";
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
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
	
	public List<Artist> getAssociatedArtists() {
		return associatedArtists;
	}

	public void setAssociatedArtists(List<Artist> associatedArtists) {
		this.associatedArtists = associatedArtists;
	}

	public String getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Resource getDeathPlace() {
		return deathPlace;
	}

	public void setDeathPlace(Resource deathPlace) {
		this.deathPlace = deathPlace;
	}

	public List<String> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<String> instruments) {
		this.instruments = instruments;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	
	public List<Band> getAssociatedBands() {
		return associatedBands;
	}

	public void setAssociatedBands(List<Band> associatedBands) {
		this.associatedBands = associatedBands;
	}

	public String getBirthPlaceURI() {
		if (birthPlace != null && birthPlace instanceof Resource){
			return birthPlace.getURI();
		} else {
			return null;
		}
	}
	
	public String getArtistURI() {
		if (resource != null && resource instanceof Resource){
		return resource.getURI();
		} else {
			return null;
		}
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getBirthPlaceStr() {
		return birthPlaceStr;
	}

	public void setBirthPlaceStr(String birthPlaceStr) {
		this.birthPlaceStr = birthPlaceStr;
	}

	public String getDeathPlaceStr() {
		return deathPlaceStr;
	}

	public void setDeathPlaceStr(String deathPlaceStr) {
		this.deathPlaceStr = deathPlaceStr;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	
	
}

