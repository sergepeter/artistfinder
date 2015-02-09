package com.music.dbpedia.finder.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.dbpedia.finder.beans.Artist;
import com.music.dbpedia.finder.service.IArtistService;

@RestController
@RequestMapping("/rest")
public class ArtistRestController {
	
	@Autowired
	private IArtistService artistService;
	
	@RequestMapping("/artists")
	public List<Artist> getArtists(@RequestParam(value = "name",required = false) String name) {
		List<Artist> l = artistService.findByName(name);
		return l;
	}
	
}