package com.music.dbpedia.finder.service;

import java.util.List;

import com.music.dbpedia.finder.beans.Artist;
import com.music.dbpedia.finder.beans.Band;

public interface IBandService {
	public Band getBandDetails(String resourceURI);
	public List<Band> getAssociatedBands(Band band);
	public List<Artist> getAssociatedArtists(Band band);
	public List<String> getGenres(Band band);
	public List<Artist> getFormerMembers(Band band);
	public Band getBandFullDetails(String resourceURI);
	public List<Band> findByName(String name);
	
}