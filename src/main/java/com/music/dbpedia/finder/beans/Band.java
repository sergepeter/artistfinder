package com.music.dbpedia.finder.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Band bean
 * @author speter
 *
 */
public class Band {

	@JsonIgnore
	private Resource resource;

	private String name;
	private String activeYearsEndYear;
	private String activeYearsStartYear;
	private String abstractStr;
	private String hometown;
	private String country;
	private String description;
	private String imageURL;

	private List<String> genres;
	private List<Artist> associatedArtists;
	private List<Band> associatedBands;
	private List<Artist> formerBandMembers;

	public Band() {
		super();
	}

	@Override
	public String toString() {
		return "Band [resource=" + resource + ", name=" + name + ", genres="
				+ genres + ", associatedArtists=" + associatedArtists
				+ ", associatedBands=" + associatedBands + "]";
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

	public String getActiveYearsEndYear() {
		return activeYearsEndYear;
	}

	public void setActiveYearsEndYear(String activeYearsEndYear) {
		this.activeYearsEndYear = activeYearsEndYear;
	}

	public String getActiveYearsStartYear() {
		return activeYearsStartYear;
	}

	public void setActiveYearsStartYear(String activeYearsStartYear) {
		this.activeYearsStartYear = activeYearsStartYear;
	}

	public String getAbstractStr() {
		return abstractStr;
	}

	public void setAbstractStr(String abstractStr) {
		this.abstractStr = abstractStr;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public List<Artist> getAssociatedArtists() {
		return associatedArtists;
	}

	public void setAssociatedArtists(List<Artist> associatedArtists) {
		this.associatedArtists = associatedArtists;
	}

	public String getBandURI() {
		if (resource != null && resource instanceof Resource){
			return resource.getURI();
		} else {
			return null;
		}
	}


	public String getHometown() {
		return hometown;
	}


	public void setHometown(String hometown) {
		this.hometown = hometown;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}





	public List<Band> getAssociatedBands() {
		return associatedBands;
	}

	public void setAssociatedBands(List<Band> associatedBands) {
		this.associatedBands = associatedBands;
	}

	public List<Artist> getFormerBandMembers() {
		return formerBandMembers;
	}


	public void setFormerBandMembers(List<Artist> formerBandMembers) {
		this.formerBandMembers = formerBandMembers;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
}
