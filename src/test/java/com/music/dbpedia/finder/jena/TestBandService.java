package com.music.dbpedia.finder.jena;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.music.dbpedia.finder.beans.Band;
import com.music.dbpedia.finder.service.IBandService;
import com.music.dbpedia.finder.spring.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class TestBandService {

	private static final Logger logger = Logger
			.getLogger(TestBandService.class.getName());

	@Autowired
	ApplicationContext context;

	@Autowired
	IBandService bandService;

	@Test
	public void testFindByName(){
		
		
		List<Band> bands = bandService.findByName("straits");
		
		
		assertEquals("Band must exists (" + bands.get(0).getName() + ")", 
				bands.size() > 0, true);
	}
	
	@Test
	public void testGetDetails() {
	
		String bandURI = "http://dbpedia.org/resource/Dire_Straits";
		Band bandDetail = bandService.getBandDetails(bandURI);

		logger.info("Name is " + bandDetail.getName());
			
		assertEquals("Band must exists : (" + bandDetail.getName() + ")", 
				bandDetail != null, true);
		
		assertEquals("Must  at least one associated artist. (Found " + bandDetail.getAssociatedArtists().size()
				+ " record.)", bandDetail.getAssociatedArtists().size() > 0, true);
		
		assertEquals("Must  at least one associated band. (Found " + bandDetail.getAssociatedBands().size()
				+ " record.)", bandDetail.getAssociatedBands().size() > 0, true);
		
		assertEquals("Must be at least one genre. (Found " + bandDetail.getGenres().size()
				+ " record.)", bandDetail.getGenres().size() > 0, true);
		
		assertEquals("Must  at least one former band artists. (Found " + bandDetail.getFormerBandMembers().size()
				+ " record.)", bandDetail.getFormerBandMembers().size() > 0, true);
	}
}
