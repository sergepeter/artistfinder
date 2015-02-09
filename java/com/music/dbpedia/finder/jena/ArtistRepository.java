package com.music.dbpedia.finder.jena;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;
import com.music.dbpedia.finder.beans.Artist;


public class ArtistRepository {
	private static final Logger logger = LoggerFactory
			.getLogger(ArtistRepository.class);
	
	public List<Artist> findByName(String name) {

		List<Artist> artists = new ArrayList<Artist>();

		String service = "http://dbpedia.org/sparql";
		String query = ""
				+ " prefix prop: <http://dbpedia.org/property/> \n"
				+ " prefix owl: <http://dbpedia.org/ontology/>\n"
				+ " prefix res: <http://dbpedia.org/resource/> \n"
				+ " prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " select distinct ?artist ?name ?birthDate ?birthPlace ?shortDescription ?abstract ?yearsActive ?website\n"
				+ " where { \n"
				+ " ?artist a owl:MusicalArtist ; owl:birthDate ?birthDate ; owl:birthPlace ?birthPlace ; prop:shortDescription ?shortDescription; owl:abstract ?abstract ;prop:yearsActive ?yearsActive; prop:website ?website .\n"
				+ " ?artist rdfs:label ?name .\n"
				+ " FILTER (regex(?name, '."
				+ name
				+ "', 'i') && langMatches(lang(?name), 'fr')  && langMatches(lang(?abstract), 'fr'))\n"
				+ "	} LIMIT 100\n";

		QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
		try {

			ResultSet res = qe.execSelect();

			Artist artist = new Artist();

			while (res.hasNext()) {

				QuerySolution row = res.next();

				artist.setArtist(row.get("artist").asResource());
				artist.setAbstractStr(row.get("abstract").asLiteral()
						.getValue().toString());

				artist.setBirthDate(parseXmlDate(row.get("birthDate")));
				
				artist.setBirthPlace(row.get("birthPlace").asResource());
				artist.setName(row.get("name").asLiteral().getValue()
						.toString());

				artist.setShortDescription(row.get("shortDescription")
						.asLiteral().getValue().toString());
				
				if (row.get("yearsActive").isLiteral()) {
					artist.setYearsActive(row.get("yearsActive").asLiteral().getValue().toString());
				}

				artist.setWebsite(new URL(row.get("website").toString()));

				artists.add(artist);

				logger.info("artists found " + artist.toString());
			}

			return artists;

		} catch (QueryExceptionHTTP e) {
			System.out.println(service + " is DOWN");
		} catch (MalformedURLException e) {
			System.out.println(service + " is ON ERROR");
		} finally {
			qe.close();
		}
		return null;
	}

	private String parseXmlDate(RDFNode node) {
		String toReturn = "";

		try {
			if (node.isLiteral()) {
				toReturn = node.asLiteral().getValue().toString();
			}
		} catch (DatatypeFormatException e) {
			logger.error("An error occurs durring node field treatement " + e.getMessage());

		}

		return toReturn;

	}

}
