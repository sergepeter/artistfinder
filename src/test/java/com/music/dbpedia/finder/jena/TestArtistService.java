package com.music.dbpedia.finder.jena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.music.dbpedia.finder.beans.Artist;
import com.music.dbpedia.finder.service.IArtistService;
import com.music.dbpedia.finder.spring.AppConfig;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class TestArtistService {
	
	//private static final Logger logger = Logger.getLogger(TestArtistRepository.class.getName());
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	IArtistService artisteService;
	
	@Test
	public void testFindArtistByName(){

		List<Artist> artists = artisteService.findByName("mark knopfler");
		assertNotNull(artists);
		assertEquals("Must fine at least one record. (Found " + artists.size() + " record.)", artists.size()>0, true);
		
	}

	
}
