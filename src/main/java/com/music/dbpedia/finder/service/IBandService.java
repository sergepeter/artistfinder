package com.music.dbpedia.finder.service;

import java.util.List;

import com.music.dbpedia.finder.beans.Artist;
import com.music.dbpedia.finder.beans.Band;

public interface IBandService {

	public List<Band> findByName(String name);
	public Band getBandDetails(String resourceURI);
	public List<Band> getAssociatedBands(Band band);
	public List<Artist> getAssociatedArtists(Band band);
}