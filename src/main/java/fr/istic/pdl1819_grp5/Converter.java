package fr.istic.pdl1819_grp5;


import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public  interface Converter
{
	/**
	 * @param link of the page where to get the tables in HTML to convert to CSV
	 * @throws IOException can caused by the implementation in class ConverterToCSV
	 */
	Set<FileMatrix> convertFromHtml(String link) throws IOException;

	/**
	 * @param link of the page where we get the tables in Wikitext to convert to CSV
	 **/
	public Set<FileMatrix> convertFromWikitext(String link) ;
	public HashMap getRelev();
}

