package com.music.dbpedia.finder.service;

import java.util.List;

import com.music.dbpedia.finder.beans.Artist;

public interface IArtistService {

	public abstract List<Artist> findByName(String name);

}