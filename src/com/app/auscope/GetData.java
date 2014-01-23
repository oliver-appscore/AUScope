package com.app.auscope;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.app.auscope.XMLUtility.XMLUtilityInterfaces;

public class GetData implements XMLUtilityInterfaces
{
	public static final String 
		AUSIS_STATIONS_URL = "http://service.iris.edu/fdsnws/station/1/query?net=S&sta=AU*&loc=--&cha=*&level=station&format=xml&nodata=404#";
	
	public void getAusisStations()
	{
		final String KEY_ITEM = "Station";
		final String LATITURE = "Latitude";
		final String LONGITUDE = "Longitude";
		final String ELEVATION = "Elevation";
		final String CREATION_DATE = "CreationDate";
		final String TOTAL_NUMBER_OF_CHANNELS = "TotalNumberChannels";
		final String SELECTED_NUMBER_OF_CHANNELS = "SelectedNumberChannels";
		
		ArrayList<HashMap<String, String>> Stations = new ArrayList<HashMap<String, String>>();
		
		String XML = XMLUtility.getXmlFromUrl(AUSIS_STATIONS_URL, this);
		Document doc = XMLUtility.getDomElement(XML);
		
		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		
		for (int i = 0; i < nl.getLength(); i++) 
		{
            //Creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            //Adding each child node to HashMap key => value
            map.put(LATITURE, XMLUtility.getValue(e, LATITURE));
            map.put(LATITURE, XMLUtility.getValue(e, LONGITUDE));
            map.put(ELEVATION, XMLUtility.getValue(e, ELEVATION));
            map.put(CREATION_DATE, XMLUtility.getValue(e, CREATION_DATE));
            map.put(TOTAL_NUMBER_OF_CHANNELS, XMLUtility.getValue(e, TOTAL_NUMBER_OF_CHANNELS));
            map.put(SELECTED_NUMBER_OF_CHANNELS, XMLUtility.getValue(e, SELECTED_NUMBER_OF_CHANNELS));
            
            //Adding HashList to ArrayList
            Stations.add(map);
        }
		
		Fn.Out(Stations);
	}

	@Override
	public void onXMLRequestComplete(String XML) 
	{
		Fn.Out(XML);
	}
	
	
}
