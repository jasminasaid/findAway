package ro.ase.csie.findAway.server.model;

public interface Constants {
	// public static final String TABLE_AGENCIES = "dbo.Agencies";
	// public static final String TABLE_AIRCRAFTS = "dbo.Aircrafts";
	// public static final String TABLE_AIRLINES = "dbo.Airlines";
	// public static final String TABLE_AIRPORTS = "dbo.Airports";
	// public static final String TABLE_CODESHARES = "dbo.Codeshares";
	// public static final String TABLE_FLIGHTHOPS = "dbo.FlightHops";
	// public static final String TABLE_FLIGHTLEGS = "dbo.FlightLegs";
	// public static final String TABLE_FLIGHTSEGMENTS = "dbo.FlightSegments";
	// public static final String TABLE_FLIGHTITINERARIES =
	// "dbo.FlightItineraries";
	// public static final String TABLE_INDICATIVEPRICES =
	// "dbo.IndicativePrices";
	// public static final String TABLE_OTHERSEGMENTS = "dbo.OtherSegments";
	// public static final String TABLE_PLACES = "dbo.Places";
	// public static final String TABLE_ROUTES = "dbo.Routes";
	// public static final String TABLE_SEARCHERESPONSES =
	// "dbo.SearchResponses";
	// public static final String TABLE_STOPS = "dbo.Stops";
	// public static final String TABLE_TRANSITHOPS = "dbo.TransitHops";
	// public static final String TABLE_TRANSITLEGS = "dbo.TransitLegs";
	// public static final String TABLE_TRANSITLINES = "dbo.TransitLines";
	// public static final String TABLE_TRANSITSEGMENTS = "dbo.TransitSegments";

	public static final String TABLE_AGENCIES = "Agencies";
	public static final String TABLE_AIRCRAFTS = "Aircrafts";
	public static final String TABLE_AIRLINES = "Airlines";
	public static final String TABLE_AIRPORTS = "Airports";
	public static final String TABLE_CODESHARES = "Codeshares";
	public static final String TABLE_FLIGHTHOPS = "FlightHops";
	public static final String TABLE_FLIGHTLEGS = "FlightLegs";
	public static final String TABLE_FLIGHTSEGMENTS = "FlightSegments";
	public static final String TABLE_FLIGHTITINERARIES = "FlightItineraries";
	public static final String TABLE_INDICATIVEPRICES = "IndicativePrices";
	public static final String TABLE_OTHERSEGMENTS = "OtherSegments";
	public static final String TABLE_PLACES = "Places";
	public static final String TABLE_ROUTES = "Routes";
	public static final String TABLE_SEARCHERESPONSES = "SearchResponses";
	public static final String TABLE_STOPS = "Stops";
	public static final String TABLE_TRANSITHOPS = "TransitHops";
	public static final String TABLE_TRANSITLEGS = "TransitLegs";
	public static final String TABLE_TRANSITLINES = "TransitLines";
	public static final String TABLE_TRANSITSEGMENTS = "TransitSegments";

	public static final String AGENCIES_COLUMNS = "(code, name, url, iconPath, iconSize, iconOffset, responseID)";
	public static final String AIRCRAFTS_COLUMNS = "(code, manufacturer, model, responseID)";
	public static final String AIRLINES_COLUMNS = "(code, name, url, iconPath, iconSize, iconOffset, responseID)";
	public static final String AIRPORTS_COLUMNS = "(code, name, pos, countryCode, regionCode, timeZone, responseID)";
	public static final String CODESHARES_COLUMNS = "(airline, flight, flightHopID)";
	public static final String FLIGHTHOPS_COLUMNS = "(sCode, tCode, sTerminal, tTerminal, sTime, tTime, flight, airline, duration,lDuration,aircraft, dayChange, flightLegID)";
	public static final String FLIGHTITINERARIES_COLUMNS = "(sID, sCode, tID, tCode, fID, fCode, price, duration, hasStop,flightLegID, indicativePriceID, flightHopID)";
	public static final String FLIGHTLEGS_COLUMNS = "(days, flightSegmentID, indicativePriceID)";
	public static final String FLIGHTSEGMENTS_COLUMNS = "(kind, isMajor, distance, duration, transferDuration, sCode, tCode, indicativePriceID, routeID)";
	public static final String INDICATIVEPRICES_COLUMNS = "(price, currency, nativePrice, nativeCurrency)";
	public static final String OTHERSEGMENTS_COLUMNS = "(kind, subkind, vehicle, isMajor, isImperial, distance, duration, sName, sPos, tName, tPos, path, indicativePriceID, routeID)";
	public static final String PLACES_COLUMNS = "(kind, longName, pos, countryCode, regionCode, timeZone, responseID)";
	public static final String ROUTES_COLUMNS = "(name, distance, duration, indicativePriceID, responseID)";
	public static final String SEARCHRESPONSE_COLUMNS = "(sName, tName)";
	public static final String STOPS_COLUMNS = "(kind, name, pos, code, countryCode, regionCode, timeZone, routeID, transitSegmentID)";
	public static final String TRANSITHOPS_COLUMNS = "(sName, sPos, tName, tPos, frequency, duration, indicativePriceID, transitLegID)";
	public static final String TRANSITLEGS_COLUMNS = "(url, transitSegmentID)";
	public static final String TRANSITLINES_COLUMNS = "(name, vehicle, code, agency, frequency, duration, days, transitHopID)";
	public static final String TRANSITSEGMENTS_COLUMNS = "(kind, subkind, vehicle, isMajor, isImperial, distance, duration,sName, sPos, tName, tPos, path, indicativePriceID, routeID)";
}
