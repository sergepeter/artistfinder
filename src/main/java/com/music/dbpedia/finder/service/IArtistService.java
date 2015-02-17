package com.music.dbpedia.finder.service;

import java.util.List;

import com.music.dbpedia.finder.beans.Artist;
import com.music.dbpedia.finder.beans.Band;

public interface IArtistService {
	public List<Artist> findByName(String name);
	public Artist getArtistDetails(String resourceURI);
	public List<Band> getAssociatedBands(Artist artist);
	public List<Artist> getAssociatedArtists(Artist artist);
	
}