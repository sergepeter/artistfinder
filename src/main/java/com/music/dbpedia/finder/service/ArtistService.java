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

/**
 * Artist Repository
 * 
 * @author speter Perform queries and data mapping with dbpedia ontology.
 */
@Component
public class ArtistService implements IArtistService {

	private String serviceURL = "http://dbpedia.org/sparql";
	private String rowLimit = "100";

	private static final Logger logger = Logger.getLogger(ArtistService.class.getName());

	/**
	 * Find artist by name (firstname then last)
	 */
	@Override
	public List<Artist> findByName(String name) {

		List<Artist> artists = new ArrayList<Artist>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct *\n" 
				+ " where {\n" 
				+ " ?artist a owl:MusicalArtist .\n"
				+ " ?artist rdfs:label ?name .\n" 
				+ " OPTIONAL { ?artist owl:birthDate ?birthDate }\n" 
				+ " OPTIONAL { ?artist prop:shortDescription ?shortDescription }\n" 
				+ " OPTIONAL { ?artist prop:website ?website  }\n"
				+ " OPTIONAL { ?artist prop:yearsActive ?yearsActive }\n" 
				+ " FILTER (regex(?name, '.*" + name + ".*', 'i') "
						+ "&& langMatches(lang(?name), 'en'))\n" 
				+ "	} LIMIT " + rowLimit + "\n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			Artist artist = new Artist();

			while (res.hasNext()) {

				QuerySolution row = res.next();

				artist = new Artist();
				artist.setResource(ParseUtils.parseXmlResource(row.get("artist").asResource()));
				artist.setBirthDate(ParseUtils.parseXmlDate(row.get("birthDate")));
				artist.setName(ParseUtils.parseXmlDate(row.get("name")));
				artist.setShortDescription(ParseUtils.parsXMLString(row.get("shortDescription")));
				artist.setYearsActive(ParseUtils.parsXMLString(row.get("yearsActive")));
				artist.setWebsite(ParseUtils.parseXmlURL(row.get("website")));

				artists.add(artist);
				logger.log(Level.INFO, "Artist found : " + artist.toString());

			}

			return artists;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
			e.printStackTrace();
		} finally {
			qe.close();
		}
		return artists;
	}

	/**
	 * Get details info
	 */
	@Override
	public Artist getArtistDetails(String resourceURI) {

		Artist artist = null;
		
		String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select *\n" 
				+ " where {\n" 
				+ " ?artist owl:sameAs? <" + resourceURI + "> .\n"
				+ " ?artist rdfs:label ?name .\n" 
				+ " OPTIONAL { ?artist owl:birthDate ?birthDate } \n"
				+ " OPTIONAL { ?artist owl:deathDate ?deathDate } \n" 
				+ " OPTIONAL { ?artist owl:birthPlace ?birthPlace } \n"
				+ " OPTIONAL { ?birthPlace rdfs:label ?birthPlaceStr }\n"
				+ " OPTIONAL { ?artist owl:deathPlace ?deathPlace } \n"
				+ " OPTIONAL { ?deathPlace rdfs:label ?deathPlaceStr }\n" 
				+ " OPTIONAL { ?artist owl:thumbnail ?image} \n"
				+ " OPTIONAL { ?artist prop:shortDescription ?shortDescription }\n" 
				+ " OPTIONAL { ?artist owl:abstract ?abstract }\n"
				+ " OPTIONAL { ?artist prop:website ?website  }\n"
				+ " OPTIONAL { ?artist prop:yearsActive ?yearsActive }\n" 
				+ " FILTER (langMatches(lang(?name), 'en') \n" 
				+ " && langMatches(lang(?birthPlaceStr), 'en')\n" 
				+ " && langMatches(lang(?abstract), 'en')) \n" 
				+ "	} LIMIT 1\n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			// should have only one row
			while (res.hasNext()) {

				QuerySolution row = res.next();
				
				artist = new Artist();

				artist.setResource(ParseUtils.parseXmlResource(row.get("artist").asResource()));
				artist.setAbstractStr(ParseUtils.parsXMLString(row.get("abstract")));
				
				artist.setBirthDate(ParseUtils.parseXmlDate(row.get("birthDate")));
				
				
				artist.setDeathDate(ParseUtils.parseXmlDate(row.get("deathDate")));
				
				if (row.get("image") != null) {
					artist.setImageURL(ParseUtils.parseXmlURL(row.get("image")).toString());
				}
				
				artist.setBirthPlace(ParseUtils.parseXmlResource(row.get("birthPlace")));
				artist.setDeathPlace(ParseUtils.parseXmlResource(row.get("deathPlace")));
				
				artist.setBirthPlaceStr(ParseUtils.parsXMLString(row.get("birthPlaceStr")));
				artist.setDeathPlaceStr(ParseUtils.parsXMLString(row.get("deathPlaceStr")));
				
				
				artist.setName(ParseUtils.parseXmlDate(row.get("name")));
				artist.setShortDescription(ParseUtils.parsXMLString(row.get("shortDescription")));
				artist.setYearsActive(ParseUtils.parsXMLString(row.get("yearsActive")));
				artist.setWebsite(ParseUtils.parseXmlURL(row.get("website")));
				
				List<Band> associatedBands = getAssociatedBands(artist);
				artist.setAssociatedBands(associatedBands);
				
				List<Artist> associatedArtists = getAssociatedArtists(artist);
				artist.setAssociatedArtists(associatedArtists);
				
				List<String> genres = getGenres(artist);
				artist.setGenres(genres);
				
				List<String> intruments = getInstruments(artist);
				artist.setInstruments(intruments);
				
				logger.log(Level.INFO, "Artist found : " + artist.toString());

			}

			return artist;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
		} finally {
			qe.close();
		}
		return artist;
	}

	/**
	 * Get list of associated artists
	 * @param artist
	 * @return
	 */
	@Override
	public List<Artist> getAssociatedArtists(Artist artist) {

			List<Artist> artists = new ArrayList<Artist>();

			String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
					+ " prefix owl: <http://dbpedia.org/ontology/>\n"
					+ " prefix res: <http://dbpedia.org/resource/> \n" 
					+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
					+ " select distinct ?artist ?name ?associatedArtist ?assName "
					+ " where {"
					+ " ?artist owl:sameAs? <" + artist.getArtistURI() + "> .\n"
					+ " ?artist rdfs:label ?name ."
					+ " ?artist owl:associatedMusicalArtist  ?associatedArtist. "
					+ " ?associatedArtist owl:background ?background. \n"					
					+ " FILTER (?background != 'group_or_band')\n"
					+ " ?associatedArtist rdfs:label ?assName .\n"
					+ " FILTER (langMatches(lang(?name), 'en')\n"
					+ " && langMatches(lang(?assName), 'en'))\n" 
					+ "	} LIMIT " + rowLimit + "\n"; 
					

			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

			try {

				ResultSet res = qe.execSelect();

				

				while (res.hasNext()) {

					QuerySolution row = res.next();
					
					Artist associatedArtist = new Artist();

					associatedArtist.setResource(ParseUtils.parseXmlResource(row.get("associatedArtist").asResource()));
					associatedArtist.setName(ParseUtils.parsXMLString(row.get("assName")));
					
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
	 * Get list of associated artists
	 * @param artist
	 * @return
	 */
	@Override
	public List<Band> getAssociatedBands(Artist artist) {

			List<Band> bands = new ArrayList<Band>();
			
			String queryString = " prefix prop: <http://dbpedia.org/property/> \n" 
					+ " prefix owl: <http://dbpedia.org/ontology/>\n"
					+ " prefix res: <http://dbpedia.org/resource/> \n" 
					+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
					+ " select distinct * "
					+ " where {"
					+ " ?artist owl:sameAs? <" + artist.getArtistURI() + "> .\n"
					+ " ?artist rdfs:label ?name .\n"
					+ " ?artist owl:associatedBand ?associatedBand.\n"
					+ " ?associatedBand rdfs:label ?assName .\n"					
					+ " ?associatedBand owl:background ?background. \n"					
					+ " FILTER (?background = 'group_or_band')\n"
					+ " FILTER (langMatches(lang(?name), 'en')\n "
					+ " && langMatches(lang(?assName), 'en'))\n" 
					+ "	} LIMIT " + rowLimit + "\n";
			
			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

			try {

				ResultSet res = qe.execSelect();


				while (res.hasNext()) {

					QuerySolution row = res.next();
					
					Band associatedBand = new Band();

					associatedBand.setResource(ParseUtils.parseXmlResource(row.get("associatedBand").asResource()));
					associatedBand.setName(ParseUtils.parsXMLString(row.get("assName")));
					
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

	/**
	 * Get list of genres
	 * @param artist
	 * @return
	 */
	public List<String> getGenres(Artist artist) {

		List<String> genres = new ArrayList<String>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n"
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n"
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "select *\n"
				+ "where {\n"
				+ " ?artist owl:sameAs? <" + artist.getArtistURI() + "> .\n"
				+ "?artist rdfs:label ?name .\n"
				+ "OPTIONAL { ?artist owl:genre ?genre}\n"
				+ "OPTIONAL { ?genre rdfs:label  ?genrelabel}\n"
				+ "FILTER (langMatches(lang(?name), 'en') && langMatches(lang(?genrelabel), 'en') )\n"
				+ "} LIMIT 10 \n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL,
				query);

		try {

			ResultSet res = qe.execSelect();
			while (res.hasNext()) {

				QuerySolution row = res.next();
				String genre = (ParseUtils.parsXMLString(row.get("genrelabel")));
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
	
	/**
	 * Get list of intruments
	 * @param artist
	 * @return
	 */
	public List<String> getInstruments(Artist artist) {

		List<String> intruments = new ArrayList<String>();

		String queryString = " prefix prop: <http://dbpedia.org/property/> \n"
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n"
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "select *\n"
				+ "where {\n"
				+ " ?artist owl:sameAs? <" + artist.getArtistURI() + "> .\n"
				+ "?artist rdfs:label ?name .\n"
				+ "OPTIONAL { ?artist owl:instrument ?intrument}\n"
				+ "OPTIONAL { ?intrument rdfs:label  ?intrumentlabel}\n"
				+ "FILTER (langMatches(lang(?name), 'en') && langMatches(lang(?intrumentlabel), 'en') )\n"
				+ "} LIMIT 10 \n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL,
				query);

		try {

			ResultSet res = qe.execSelect();
			while (res.hasNext()) {

				QuerySolution row = res.next();
				String intrument = (ParseUtils.parsXMLString(row.get("intrumentlabel")));
				intruments.add(intrument);
				logger.log(Level.FINE, "Genre found : " + intruments);
			}

			return intruments;

		} catch (QueryExceptionHTTP e) {
			logger.log(Level.SEVERE, serviceURL + " is DOWN", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, serviceURL + " is on error : ", e);
		} finally {
			qe.close();
		}
		return intruments;
	}
}
