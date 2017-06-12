package com.encore.piano.map.gpxparser;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//import com.com.encore.piano.model.GpxTrackCoordinateModel;
import com.encore.piano.model.GpxTrackModel;

public class GpxXmlHandler extends DefaultHandler {

	
	ArrayList<GpxTrackModel> GpxTracks = new ArrayList<GpxTrackModel>();
//	ArrayList<GpxTrackCoordinateModel> GpxTrackCoordinates = null;	
//	
//	GpxTrackModel GpxTrack = null;
//	GpxTrackCoordinateModel GpxTrackPoint = null;
//	
	String Value = "";
	 
	int trkPointCounter = 0;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
//		if(qName.equalsIgnoreCase("trk"))
//			GpxTrack = new GpxTrackModel();
//		else if(qName.equalsIgnoreCase("trkseg"))
//			GpxTrackCoordinates = new ArrayList<GpxTrackCoordinateModel>();
//		else if(qName.equalsIgnoreCase("trkpt")){
//			GpxTrackPoint = new GpxTrackCoordinateModel();
//			GpxTrackPoint.setLatitude(Double.parseDouble(attributes.getValue("lat")));
//			GpxTrackPoint.setLongitude(Double.parseDouble(attributes.getValue("lon")));
//			trkPointCounter++;
//		}
//		
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
//		if(qName.equalsIgnoreCase("trkpt"))
//			GpxTrackCoordinates.add(GpxTrackPoint);
//		else if(qName.equalsIgnoreCase("name"))
//			GpxTrack.setName(Value);
//		else if(qName.equalsIgnoreCase("trkseg")){
//			GpxTrack.setTrackCoordinates(GpxTrackCoordinates);			
//		}
//		else if(qName.equalsIgnoreCase("trk"))
//			GpxTracks.add(GpxTrack);
//		else { }
//		
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		Value = new String(ch, start, length);
		
		super.characters(ch, start, length);
	}
	
	
	public ArrayList<GpxTrackModel> GetGpxTracks()
	{
		return GpxTracks;
	}
}
