package com.encore.piano.map.gpxparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.encore.piano.model.GpxTrackModel;

public class GpxParser {

	
	static ArrayList<GpxTrackModel> GpxTracks = new ArrayList<GpxTrackModel>();
	
	public GpxParser(){}
	
	public static ArrayList<GpxTrackModel> Parse(InputStream is){
		
		try {
			XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			GpxXmlHandler handler = new GpxXmlHandler();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(is));
			
			GpxTracks = handler.GetGpxTracks();			
			
		} catch (SAXException e) {			// 
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return GpxTracks;
	}
}
