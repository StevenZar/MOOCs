package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// Additional variables
	int eqSize = 0;
	PointFeature eq0, eq1, eqMax;
	float maxMag = 0;
	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // Save the last earthquakes
	    eq0 = earthquakes.get(0);
	    eq1 = earthquakes.get(1);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	eqSize = earthquakes.size();
	    	System.out.println("Total earthquakes data = " + eqSize);
	    	
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    
	    //TODO: Add code here as appropriate
	    // create the markers
	    float curMag = 0;
	    for (PointFeature pointFeature : earthquakes) {
	    	// Get earthquake with max. magnitude
	    	curMag = getEQMagnitude(pointFeature);
	    	if (curMag > maxMag) {
	    		maxMag = curMag;
	    		eqMax = pointFeature;
	    	}
	    	// Use the helper to get a point marker
	    	SimplePointMarker pointMark = createMarker(pointFeature);
			
			// Add point markers to List<Markers>
			markers.add(pointMark);
		}
	    
	    // Add multiple markers to the map (from java.util.List<Market object>)
	    map.addMarkers(markers);
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		SimplePointMarker pointMarker = new SimplePointMarker(feature.getLocation());
		// Find radius and color to marker according to the magnitude
		int colorMarker = 0;
	    float radiusMarker = 0;
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		if (mag < THRESHOLD_LIGHT) {
			radiusMarker = 7;
			colorMarker = color(0,0,255);	// Blue Marker
		} else if (mag < THRESHOLD_MODERATE) {
			radiusMarker = 10;
			colorMarker = color(255,255,0);	// Yellow Marker
		} else {
			radiusMarker = 15;
			colorMarker = color(255,0,00);	// Red Marker
		}
		// Set radius to the marker
		pointMarker.setRadius(radiusMarker);
		// Set color to the marker
		pointMarker.setColor(colorMarker);
		return pointMarker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		// Draw Rectangle
		fill(254,249,240);
		rect(20, 50, 160, 200);
		// Put Title
		fill(0, 0, 0);
		text("Earthquake Key", 50, 75);
		// Draw ellipse and put text marker
		fill(255, 0, 0);
		ellipse(45, 115, 15, 15);
		fill(0, 0, 0);
		text("5.0+ Magnitude", 65, 120);
		
		fill(255, 255, 0);
		ellipse(47, 165, 10, 10);
		fill(0, 0, 0);
		text("4.0+ Magnitude", 65, 170);
		
		fill(0, 0, 255);
		ellipse(50, 215, 5, 5);
		fill(0, 0, 0);
		text("Below 4.0", 65, 220);
		
		// Additional
		fill(254,249,240);
		rect(20, 270, 160, 280);		// Draw background rectangle
		
		fill(0, 0, 0);
		text("Last 2 Earthquakes", 50, 300);
		text("Country :", 30, 330);
		text(getEQCountry(eq0), 110, 330);
		text("Magnitude :", 30, 350);
		text(getEQMagnitude(eq0), 110, 350);
		text("Country :", 30, 380);
		text(getEQCountry(eq1), 110, 380);
		text("Magnitude :", 30, 400);
		text(getEQMagnitude(eq1), 110, 400);
		
		text("Max. Mag. EQ. last Week", 30, 440);
		text("Country :", 30, 470);
		text(getEQCountry(eqMax), 110, 470);
		text("Magnitude :", 30, 490);
		text(maxMag, 110, 490);
		text("Time :", 30, 510);
		text(getEQTime(eqMax), 110, 510);
		
		text("USGS.GOV", 30, 540);
		text(eqSize + " samples", 100, 540);

	}
	
	private String getEQCountry(PointFeature feature) {
		Object magObj = feature.getProperty("title");
    	String title = magObj.toString();
    	String[] columns = title.split(",");
		return columns[1];
	}
	
	private float getEQMagnitude(PointFeature feature) {
		Object magObj = feature.getProperty("magnitude");
		return Float.parseFloat(magObj.toString());
	}
	
	private String getEQTime(PointFeature feature) {
		Object magObj = feature.getProperty("age");
		return magObj.toString();
	}
}
