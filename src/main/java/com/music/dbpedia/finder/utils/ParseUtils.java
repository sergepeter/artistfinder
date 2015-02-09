package com.music.dbpedia.finder.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.datatypes.DatatypeFormatException;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class ParseUtils {

	private static final Logger logger = Logger.getLogger(ParseUtils.class.getName());

	public static String parseXmlDate(RDFNode node) {
		String toReturn = "";
		try {
			if (node != null && node.isLiteral()) {
				toReturn = node.asLiteral().getValue().toString();
			}
		} catch (DatatypeFormatException e) {
			logger.log(Level.WARNING, "Error on date field parsing : " + e.getMessage());
		}
		return toReturn;
	}
	
	public static Resource parseXmlResource(RDFNode node) {
		Resource toReturn = null;
		try {
			if (node != null && node.isResource()) {
				toReturn = node.asResource();
			}
		} catch (DatatypeFormatException e) {
			logger.log(Level.WARNING, "Error on date field parsing : " + e.getMessage());
		}
		return toReturn;
	}
	
	public static String parsXMLString(RDFNode node) {
		String toReturn = "";
		try {
			if (node != null && node.isLiteral()) {
				toReturn = node.asLiteral().getValue().toString();
			}
		} catch (DatatypeFormatException e) {
			logger.log(Level.WARNING, "Error on date field parsing : " + e.getMessage());
		}
		return toReturn;
	}

	public static URL parseXmlURL(RDFNode node) {
		URL toReturn = null;

		try {
			if (node != null) {
				toReturn = new URL(node.toString());
			}
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Error on date field parsing : " + e.getMessage());
		}
		return toReturn;
	}
}
