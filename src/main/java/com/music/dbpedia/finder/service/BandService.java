package com.music.dbpedia.finder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;
import com.music.dbpedia.finder.beans.Artist;
import com.music.dbpedia.finder.beans.Band;
import com.music.dbpedia.finder.utils.ParseUtils;

@Component
public class BandService implements IBandService {

	private String serviceURL = "http://dbpedia.org/sparql";
	private String rowLimit = "100";

	private static final Logger logger = Logger.getLogger(BandService.class.getName());

	
	/**
	 * Find artist by name (firstname then last)
	 */
	@Override
	public List<Band> findByName(String name) {

		List<Band> bands = new ArrayList<Band>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix foaf: <http://xmlns.com/foaf/0.1/name/> \n"
				
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct *\n" 
				+ " where {\n" 
				
				+ " ?band a owl:Organisation .\n"
				+ " ?band rdfs:label ?name .\n"
				+ " ?band owl:background ?background .\n"
				
				+ " OPTIONAL { ?band prop:yearsActive ?yearsActive }\n"
				+ " OPTIONAL { ?band rdfs:comment ?comment }\n" 
				+ " OPTIONAL { ?band foaf:isPrimaryTopicOf ?website  }\n"
				
				+ " FILTER (?background = 'group_or_band') \n" 
				+ " FILTER (regex(?name, '.*" + name + ".*', 'i')\n"
				+ " && langMatches(lang(?comment), 'en')\n"
				+ " && langMatches(lang(?name), 'en'))\n"
				+ "	} LIMIT " + rowLimit + "\n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			Band band = new Band();

			while (res.hasNext()) {

				QuerySolution row = res.next();

				band = new Band();
				band.setResource(ParseUtils.parseXmlResource(row.get("band").asResource()));
				band.setName(ParseUtils.parseXMLString(row.get("name")));
				band.setActiveYearsStartYear(ParseUtils.parseXMLString(row.get("yearsActive")));
				band.setDescription(ParseUtils.parseXMLString(row.get("comment")));

				bands.add(band);
				logger.log(Level.INFO, "Artist found : " + band.toString());

			}

			return bands;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
			e.printStackTrace();
		} finally {
			qe.close();
		}
		return bands;
	}
	
	@Override
	public Band getBandDetails(String resourceURI) {

		Band band = new Band();
			
			String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
					+ " prefix owl: <http://dbpedia.org/ontology/>\n"
					+ " prefix res: <http://dbpedia.org/resource/> \n" 
					+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
					+ " select *\n"
					+ " where {\n"
					+ " ?band owl:sameAs? <" + resourceURI + "> .\n"
					+ " ?band rdfs:label ?name .\n"
					+ " OPTIONAL { ?band prop:description ?description }\n"
					+ " OPTIONAL { ?band owl:hometown ?hometown }\n"
					+ " OPTIONAL { ?band owl:thumbnail ?image }\n"
					+ " OPTIONAL { ?hometown rdfs:label ?hometownname }\n"
					+ " OPTIONAL { ?hometown prop:country ?country }\n"
					+ " OPTIONAL { ?band owl:activeYearsEndYear ?activeYearsEndYear}\n"
					+ " OPTIONAL { ?band owl:activeYearsStartYear ?activeYearsStartYear}\n"
					+ " OPTIONAL { ?band owl:abstract ?abstract }\n"
					+ " FILTER (langMatches(lang(?name), 'en')\n" 
					+ " && langMatches(lang(?abstract), 'en') \n"
					+ " && langMatches(lang(?hometownname), 'en'))\n"
					+ "	} LIMIT 1\n";

			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

			try {

				ResultSet res = qe.execSelect();

				// should have only one row
				while (res.hasNext()) {
					
					band = new Band();
					
					QuerySolution row = res.next();

					band.setName(ParseUtils.parseXMLString(row.get("name")));
					band.setResource(ParseUtils.parseXmlResource(row.get("band").asResource()));
					band.setAbstractStr(ParseUtils.parseXMLString(row.get("abstract")));
					band.setDescription(ParseUtils.parseXMLString(row.get("description")));
					band.setHometown(ParseUtils.parseXMLString(row.get("hometownname")));
					
					if (row.get("image") != null) {
						band.setImageURL(ParseUtils.parseXmlURL(row.get("image")).toString());
					}
					band.setCountry(ParseUtils.parseXMLString(row.get("country")));
					
					band.setActiveYearsEndYear(ParseUtils.parseXmlDate(row.get("activeYearsEndYear")));
					band.setActiveYearsStartYear(ParseUtils.parseXmlDate(row.get("activeYearsStartYear")));
					logger.log(Level.FINE, "Band found : " + band.toString());

				}

				return band;

			} catch (QueryExceptionHTTP e) {
				logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
			} catch (Exception e) {
				logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
			} finally {
				qe.close();
			}
			return band;
		
		
	}
	
	/**
	 * Get all band details (more longer process)
	 */
	@Override
	public Band getBandFullDetails(String resourceURI) {
		
		
		Band band = getBandDetails(resourceURI);
		
		List<Band> associatedBands = getAssociatedBands(band);
		band.setAssociatedBands(associatedBands);
		
		List<Artist> associatedArtists = getAssociatedArtists(band);
		band.setAssociatedArtists(associatedArtists);
		
		List<String> genres = getGenres(band);
		band.setGenres(genres);
		
		List<Artist> formerMembers = getFormerMembers(band);
		band.setFormerBandMembers(formerMembers);
		
		return band;
		
		
	}

	@Override
	public List<Band> getAssociatedBands(Band band) {
		List<Band> bands = new ArrayList<Band>();
		
		String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct * "
				+ " where {"
				+ " ?band owl:sameAs? <" + band.getBandURI() + "> .\n"
				+ " ?band rdfs:label ?name ."
				+ " ?band owl:associatedBand ?associatedBand. "
				+ " ?associatedBand rdfs:label ?assName . "
				+ " ?associatedBand owl:background ?background. \n"					
				+ " FILTER (?background = 'group_or_band')\n"

				+ " FILTER (langMatches(lang(?name), 'fr') && langMatches(lang(?assName), 'fr'))\n" 
				+ "	} LIMIT " + rowLimit + "\n";
		
		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			

			while (res.hasNext()) {

				QuerySolution row = res.next();
				
				Band associatedBand = new Band();

				associatedBand.setResource(ParseUtils.parseXmlResource(row.get("associatedBand").asResource()));
				associatedBand.setName(ParseUtils.parseXMLString(row.get("assName")));
				bands.add(associatedBand);
				logger.log(Level.FINE, "Band found : " + associatedBand);
			}

			return bands;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
		} finally {
			qe.close();
		}
		return bands;
	}

	@Override
	public List<Artist> getAssociatedArtists(Band band) {
		List<Artist> artists = new ArrayList<Artist>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct ?artist ?name ?associatedArtist ?assName "
				+ " where {"
				+ " ?band owl:sameAs? <" + band.getBandURI() + "> .\n"
				+ " ?band rdfs:label ?name ."
				+ " ?band owl:associatedMusicalArtist  ?associatedArtist. "

				+ " ?associatedArtist owl:background ?background. \n"					
				+ " FILTER (?background != 'group_or_band')\n"
				
				+ " ?associatedArtist rdfs:label ?assName . "
				+ " FILTER (langMatches(lang(?name), 'fr') && langMatches(lang(?assName), 'fr'))\n" 
				+ "	} LIMIT " + rowLimit + "\n"; 
				

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			

			while (res.hasNext()) {

				QuerySolution row = res.next();
				
				Artist associatedArtist = new Artist();

				associatedArtist.setResource(ParseUtils.parseXmlResource(row.get("associatedArtist").asResource()));
				associatedArtist.setName(ParseUtils.parseXMLString(row.get("assName")));
				
				artists.add(associatedArtist);
				logger.log(Level.FINE, "Artist found : " + associatedArtist);
			}

			return artists;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
		} finally {
			qe.close();
		}
		return artists;
	}
	
	/**
	 * Get list of genres
	 * @param artist
	 * @return
	 */
	@Override
	public List<String> getGenres(Band band) {

		List<String> genres = new ArrayList<String>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n"
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n"
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "select *\n"
				+ "where {\n"
				+ " ?band owl:sameAs? <" + band.getBandURI() + "> .\n"
				+ "?band rdfs:label ?name .\n"
				+ "OPTIONAL { ?band owl:genre ?genre}\n"
				+ "OPTIONAL { ?band rdfs:label ?genrelabel}\n"
				+ "FILTER (langMatches(lang(?name), 'en') && langMatches(lang(?genrelabel), 'en') )\n"
				+ "} LIMIT 10 \n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL,
				query);

		try {

			ResultSet res = qe.execSelect();
			while (res.hasNext()) {

				QuerySolution row = res.next();
				String genre = (ParseUtils.parseXMLString(row.get("genrelabel")));
				genres.add(genre);
				logger.log(Level.FINE, "Genre found : " + genre);
			}

			return genres;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
		} finally {
			qe.close();
		}
		return genres;
	}

	@Override
	public List<Artist> getFormerMembers(Band band) {
		List<Artist> artists = new ArrayList<Artist>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct ?artist ?name ?formerBandMember ?assName "
				+ " where {"
				+ " ?band owl:sameAs? <" + band.getBandURI() + "> .\n"
				+ " ?band rdfs:label ?name ."
				+ " ?band owl:formerBandMember  ?formerBandMember. "
				
				+ " ?formerBandMember owl:background ?background. \n"					
				+ " FILTER (?background != 'group_or_band')\n"

				
				+ " ?formerBandMember rdfs:label ?assName . "
				+ " FILTER (langMatches(lang(?name), 'fr') && langMatches(lang(?assName), 'fr'))\n" 
				+ "	} LIMIT " + rowLimit + "\n"; 
				

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			while (res.hasNext()) {
				
				QuerySolution row = res.next();

				Artist artist = new Artist();
				
				artist.setResource(ParseUtils.parseXmlResource(row.get("formerBandMember").asResource()));
				artist.setName(ParseUtils.parseXMLString(row.get("assName")));
				
				artists.add(artist);
				logger.log(Level.FINE, "Former Band Memeber found : " + artist);
			}

			return artists;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
		} finally {
			qe.close();
		}
		return artists;
	}
	
}
