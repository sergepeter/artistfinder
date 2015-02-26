package com.music.dbpedia.finder.jena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.logging.Logger;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class TestArtistService {

	private static final Logger logger = Logger
			.getLogger(TestArtistService.class.getName());

	@Autowired
	ApplicationContext context;

	@Autowired
	IArtistService artisteService;

	@Test
	public void testFindArtistByName() {

		List<Artist> artists = artisteService.findByName("van halen");
		assertNotNull(artists);
		assertEquals("Must find at least one record. (Found " + artists.size()
				+ " record.)", artists.size() > 0, true);
	}

	@Test
	public void testGetDetails() {
		Artist artist = null;

		List<Artist> artists = artisteService.findByName("knopfler");
		if (artists != null && artists.size() > 0) {
			artist = artists.get(0);
		}

		if (artist != null && artist.getResource() != null) {
			Artist artistDetail = artisteService.getArtistFullDetails(artist.getArtistURI());
			logger.info("Name is " + artistDetail.getName() + " associated artists found : " + artistDetail.getAssociatedArtists().size());
			logger.info("Name is " + artistDetail.getName() + " associated bands found : " + artistDetail.getAssociatedBands().size());
			
			assertEquals("Name must be the same. (" + artist.getName() + "/" 
					+ artistDetail.getName() + ")", artist.getName().equals(artistDetail.getName()), true);
			
			assertEquals("Must be at least one associated artist. (Found " + artistDetail.getAssociatedArtists().size()
					+ " record.)", artistDetail.getAssociatedArtists().size() > 0, true);
			
			assertEquals("Must be at least one associated band. (Found " + artistDetail.getAssociatedBands().size()
					+ " record.)", artistDetail.getAssociatedBands().size() > 0, true);
			
			assertEquals("Must be at least one genre. (Found " + artistDetail.getGenres().size()
					+ " record.)", artistDetail.getGenres().size() > 0, true);
			
		}

	}

}
