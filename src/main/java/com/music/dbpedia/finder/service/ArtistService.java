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
	 * Find artiste by name (firstname then last)
	 */
	@Override
	public List<Artist> findByName(String name) {

		List<Artist> artists = new ArrayList<Artist>();

		String queryString = "" + " prefix prop: <http://dbpedia.org/property/> \n" 
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n" 
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct ?artist ?name ?birthDate ?birthPlace ?shortDescription ?abstract ?yearsActive ?website\n" 
				+ " where {\n" 
				+ " ?artist a owl:MusicalArtist .\n"
				+ " ?artist rdfs:label ?name .\n" 
				+ " OPTIONAL { ?artist owl:birthDate ?birthDate }\n" 
				+ " OPTIONAL { ?artist owl:birthPlace ?birthPlace }\n"
				+ " OPTIONAL { ?artist prop:shortDescription ?shortDescription }\n" 
				+ " OPTIONAL { ?artist owl:abstract ?abstract }\n"
				+ " OPTIONAL { ?artist prop:website ?website  }\n"
				+ " OPTIONAL { ?artist prop:yearsActive ?yearsActive }\n" 
				+ " FILTER (regex(?name, '.*" + name + ".*', 'i') && langMatches(lang(?name), 'fr') && langMatches(lang(?abstract), 'fr'))\n" 
				+ "	} LIMIT " + rowLimit + "\n";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURL, query);

		try {

			ResultSet res = qe.execSelect();

			Artist artist = new Artist();

			while (res.hasNext()) {

				QuerySolution row = res.next();

				artist.setArtist(ParseUtils.parseXmlResource(row.get("artist").asResource()));
				artist.setAbstractStr(ParseUtils.parsXMLString(row.get("abstract")));
				artist.setBirthDate(ParseUtils.parseXmlDate(row.get("birthDate")));
				artist.setBirthPlace(ParseUtils.parseXmlResource(row.get("birthPlace")));
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
		} finally {
			qe.close();
		}
		return artists;
	}

}
