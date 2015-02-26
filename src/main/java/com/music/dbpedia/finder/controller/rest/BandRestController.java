package com.music.dbpedia.finder.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.dbpedia.finder.beans.Band;
import com.music.dbpedia.finder.service.IBandService;

/**
 * Band main REST controller
 * 
 * @author speter
 *
 */
@RestController
@RequestMapping("/rest")
public class BandRestController {
	
	@Autowired
	private IBandService bandService;

	@RequestMapping("/bands")
	public List<Band> getBands(@RequestParam(value = "name",required = false) String name) {
		List<Band> l = bandService.findByName(name);
		return l;
	}
	
	@RequestMapping("/band")
	public Band getArtist(@RequestParam(value = "uri",required = false) String uri) {
		Band artist = bandService.getBandDetails(uri);
		return artist;
	}
	
	@RequestMapping("/bandfull")
	public Band getArtistFull(@RequestParam(value = "uri",required = false) String uri) {
		Band artist = bandService.getBandFullDetails(uri);
		return artist;
	}
}
