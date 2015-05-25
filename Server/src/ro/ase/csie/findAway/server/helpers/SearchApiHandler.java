package ro.ase.csie.findAway.server.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import ro.ase.csie.findAway.server.model.Constants;
import ro.ase.csie.findAway.server.model.api.Agency;
import ro.ase.csie.findAway.server.model.api.Aircraft;
import ro.ase.csie.findAway.server.model.api.Airline;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.CarSegment;
import ro.ase.csie.findAway.server.model.api.Codeshare;
import ro.ase.csie.findAway.server.model.api.FlightHop;
import ro.ase.csie.findAway.server.model.api.FlightItinerary;
import ro.ase.csie.findAway.server.model.api.FlightLeg;
import ro.ase.csie.findAway.server.model.api.FlightSegment;
import ro.ase.csie.findAway.server.model.api.IndicativePrice;
import ro.ase.csie.findAway.server.model.api.OtherSegment;
import ro.ase.csie.findAway.server.model.api.Place;
import ro.ase.csie.findAway.server.model.api.Route;
import ro.ase.csie.findAway.server.model.api.SearchResponse;
import ro.ase.csie.findAway.server.model.api.Stop;
import ro.ase.csie.findAway.server.model.api.TransitHop;
import ro.ase.csie.findAway.server.model.api.TransitItinerary;
import ro.ase.csie.findAway.server.model.api.TransitLeg;
import ro.ase.csie.findAway.server.model.api.TransitLine;
import ro.ase.csie.findAway.server.model.api.TransitSegment;
import ro.ase.csie.findAway.server.model.api.WalkSegment;

public class SearchApiHandler implements Constants {

	protected Connection con;
	protected String sql;
	protected PreparedStatement stmt;
	protected ResultSet rs;

	public SearchApiHandler() {
		this.con = DatabaseConnectionHandler.getInstance().getConnection();
		this.stmt = null;
		this.sql = null;
		this.rs = null;
	}

	public SearchResponse getApiResponse(String oName, String dName) {
		SearchResponse response = null;
		try {
			URL url = new URL(
					"http://free.rome2rio.com/api/1.2/xml/Search?key=XtEsWlQh&currencyCode=EUR&oName="
							+ oName.replace(" ", "%20")
							+ "&dName="
							+ dName.replace(" ", "%20"));
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String inputLine;
			String result = "";

			while ((inputLine = in.readLine()) != null)
				result += inputLine;

			JAXBContext jc = JAXBContext.newInstance(SearchResponse.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			StringReader reader = new StringReader(result);
//			unmarshaller.setEventHandler(
//				    new ValidationEventHandler() {
//				        public boolean handleEvent(ValidationEvent event ) {
//				            throw new RuntimeException(event.getMessage(),
//				                                       event.getLinkedException());
//				        }
//				});
			response = (SearchResponse) unmarshaller.unmarshal(reader);

			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	public void handleRequest(String oName, String dName) throws SQLException {
		SearchResponse response = getApiResponse(oName, dName);

		try {
			// this.con = DatabaseConnection.getInstance().getConnection();
			if (response != null) {
				List<Place> places = response.getPlaces();
				List<Agency> agencies = response.getAgencies();
				List<Airline> airlines = response.getAirlines();
				List<Aircraft> aircrafts = response.getAircrafts();
				List<Airport> airports = response.getAirports();
				List<Route> routes = response.getRoutes();
				String sName = null, tName = null;
				if (places != null) {
					if (places.size() == 2) {
						sName = places.get(0).getLongName();
						tName = places.get(1).getLongName();
					}
				}
				int responseID = insertSearchResponse(sName, tName);

				if (responseID != 0) {
					if (places != null) {
						for (int i = 0; i < places.size(); i++) {
							Place place = places.get(i);
							insertPlace(place, responseID);
						}
					}
					if (agencies != null) {
						for (int i = 0; i < agencies.size(); i++) {
							Agency agency = agencies.get(i);
							insertAgency(agency, responseID);
						}
					}
					if (airlines != null) {
						for (int i = 0; i < airlines.size(); i++) {
							Airline airline = airlines.get(i);
							insertAirline(airline, responseID);
						}
					}

					if (aircrafts != null) {
						for (int i = 0; i < aircrafts.size(); i++) {
							Aircraft aircraft = aircrafts.get(i);
							insertAircraft(aircraft, responseID);
						}
					}
					if (airports != null) {
						for (int i = 0; i < airports.size(); i++) {
							Airport airport = airports.get(i);
							insertAirports(airport, responseID);
						}
					}
					if (routes != null) {
						for (int i = 0; i < routes.size(); i++) {
							Route route = routes.get(i);
							IndicativePrice price = route.getIndicativePrice();
							int indicativePriceID = insertIndicativePrice(price);
							int routeID = insertRoute(route, indicativePriceID,
									responseID);

							List<Stop> stops = route.getStops();
							List<CarSegment> carSegments = route
									.getCarSegments();
							List<WalkSegment> walkSegments = route
									.getWalkSegments();
							List<FlightSegment> flightSegments = route
									.getFlightSegments();
							List<TransitSegment> transitSegments = route
									.getTransitSegments();

							if (stops != null) {
								for (int j = 0; j < stops.size(); j++) {
									Stop stop = stops.get(j);
									insertStop(stop, routeID, 0);
								}
							}

							if (carSegments != null) {
								for (int j = 0; j < carSegments.size(); j++) {
									CarSegment car = carSegments.get(j);
									insertOtherSegment(car, routeID,
											indicativePriceID);
								}
							}
							if (walkSegments != null) {
								for (int j = 0; j < walkSegments.size(); j++) {
									WalkSegment walk = walkSegments.get(j);
									insertOtherSegment(walk, routeID,
											indicativePriceID);
								}
							}
							if (flightSegments != null) {
								for (int j = 0; j < flightSegments.size(); j++) {
									FlightSegment fs = flightSegments.get(j);
									IndicativePrice priceFS = fs
											.getIndicativePrice();
									int indicativePriceFSID = insertIndicativePrice(priceFS);
									int flightSegmentID = insertFlightSegment(
											fs, routeID, indicativePriceFSID);

									List<FlightItinerary> itineraries = fs
											.getItineraries();
									if (itineraries != null) {
										for (int k = 0; k < itineraries.size(); k++) {
											List<FlightLeg> legs = itineraries
													.get(k).getLegs();
											if (legs != null) {
												for (int l = 0; l < legs.size(); l++) {
													FlightLeg fl = legs.get(l);
													IndicativePrice priceFL = fl
															.getIndicativePrice();
													int indicativePriceFLID = insertIndicativePrice(priceFL);
													int flightLegID = insertFlightLeg(
															fl,
															flightSegmentID,
															indicativePriceFLID);

													List<FlightHop> hops = fl
															.getHops();
													if (hops != null) {
														for (int m = 0; m < hops
																.size(); m++) {
															FlightHop hop = hops
																	.get(m);
															int flightHopID = insertFlightHop(
																	hop,
																	flightLegID);
															int sAirportID = getAirportByCode(hop
																	.getsCode());
															int tAirportID = getAirportByCode(hop
																	.gettCode());
															int fAirportID = getAirportByCode(fs
																	.gettCode());

															if (sAirportID != 0
																	&& tAirportID != 0) {
																boolean hasStop;
																if (hops.size() > 1)
																	hasStop = true;
																else
																	hasStop = false;
																insertFlightItinerary(
																		hop,
																		sAirportID,
																		tAirportID,
																		fAirportID,
																		fs.gettCode(),
																		priceFL.getPrice(),
																		hasStop,
																		flightLegID,
																		indicativePriceFLID,
																		flightHopID);
															}

															List<Codeshare> shares = hop
																	.getShares();
															if (shares != null) {
																for (int n = 0; n < shares
																		.size(); n++) {
																	Codeshare share = shares
																			.get(n);
																	insertCodeshare(
																			share,
																			flightHopID);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}

							if (transitSegments != null) {
								for (int j = 0; j < transitSegments.size(); j++) {
									TransitSegment ts = transitSegments.get(j);
									IndicativePrice priceTS = ts
											.getIndicativePrice();
									int indicativePriceTSID = insertIndicativePrice(priceTS);
									int transitSegmentID = insertTransitSegment(
											ts, routeID, indicativePriceTSID);

									List<Stop> stopsTS = ts.getStops();
									if (stopsTS != null) {
										for (int k = 0; k < stopsTS.size(); k++) {
											Stop stop = stopsTS.get(k);
											insertStop(stop, routeID,
													transitSegmentID);
										}
									}

									List<TransitItinerary> itineraries = ts
											.getItineraries();
									if (itineraries != null) {
										for (int k = 0; k < itineraries.size(); k++) {
											List<TransitLeg> legs = itineraries
													.get(k).getLegs();
											if (legs != null) {
												for (int l = 0; l < legs.size(); l++) {
													TransitLeg tlg = legs
															.get(l);
													int transitLegID = insertTransitLeg(
															tlg,
															transitSegmentID);

													List<TransitHop> hops = tlg
															.getHops();
													if (hops != null) {
														for (int m = 0; m < hops
																.size(); m++) {
															TransitHop hop = hops
																	.get(m);
															IndicativePrice priceTH = hop
																	.getIndicativePrice();
															int indicativePriceTHID = insertIndicativePrice(priceTH);
															int transitHopID = insertTransitHop(
																	hop,
																	transitLegID,
																	indicativePriceTHID);

															List<TransitLine> lines = hop
																	.getLines();
															if (lines != null) {
																for (int n = 0; n < lines
																		.size(); n++) {
																	TransitLine line = lines
																			.get(n);
																	insertTransitLine(
																			line,
																			transitHopID);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
	}

	private int insertSearchResponse(String sName, String tName) {
		int responseID = 0;
		try {
			sql = "INSERT INTO " + TABLE_SEARCHERESPONSES
					+ SEARCHRESPONSE_COLUMNS + " VALUES(?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, sName);
			stmt.setString(2, tName);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();

			if (rs != null && rs.next()) {
				responseID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return responseID;
	}

	private void insertPlace(Place place, int responseID) {
		try {
			sql = "INSERT INTO " + TABLE_PLACES + PLACES_COLUMNS + " VALUES(?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, place.getKind());
			stmt.setString(2, place.getLongName());
			stmt.setString(3, place.getPos());
			stmt.setString(4, place.getCountryCode());
			stmt.setString(5, place.getRegionCode());
			stmt.setString(6, place.getTimeZone());
			stmt.setInt(7, responseID);
			stmt.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertAgency(Agency agency, int responseID) {
		try {
			sql = "INSERT INTO "
					+ TABLE_AGENCIES + AGENCIES_COLUMNS  
					+ " VALUES(?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, agency.getCode());
			stmt.setString(2, agency.getName());
			stmt.setString(3, agency.getUrl());
			stmt.setString(4, agency.getIconPath());
			stmt.setString(5, agency.getIconSize());
			stmt.setString(6, agency.getIconOffset());
			stmt.setInt(7, responseID);
			stmt.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertAirline(Airline airline, int responseID) {
		try {
			sql = "INSERT INTO " + TABLE_AIRLINES + AIRLINES_COLUMNS + " VALUES(?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, airline.getCode());
			stmt.setString(2, airline.getName());
			stmt.setString(3, airline.getUrl());
			stmt.setString(4, airline.getIconPath());
			stmt.setString(5, airline.getIconSize());
			stmt.setString(6, airline.getIconOffset());
			stmt.setInt(7, responseID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertAircraft(Aircraft aircraft, int responseID) {
		try {
			sql = "INSERT INTO " + TABLE_AIRCRAFTS + AIRCRAFTS_COLUMNS + " VALUES(?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, aircraft.getCode());
			stmt.setString(2, aircraft.getManufacturer());
			stmt.setString(3, aircraft.getModel());
			stmt.setInt(4, responseID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertAirports(Airport airport, int responseID) {
		try {
			sql = "INSERT INTO " + TABLE_AIRPORTS +  AIRPORTS_COLUMNS +" VALUES(?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, airport.getCode());
			stmt.setString(2, airport.getName());
			stmt.setString(3, airport.getPos());
			stmt.setString(4, airport.getCountryCode());
			stmt.setString(5, airport.getRegionCode());
			stmt.setString(6, airport.getTimeZone());
			stmt.setInt(7, responseID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int insertIndicativePrice(IndicativePrice price) {
		int indicativePriceID = 0;
		try {
			sql = "INSERT INTO " + TABLE_INDICATIVEPRICES + INDICATIVEPRICES_COLUMNS + " VALUES(?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setFloat(1, price.getPrice());
			stmt.setString(2, price.getCurrency());
			stmt.setFloat(3, price.getNativePrice());
			stmt.setString(4, price.getNativeCurrency());
			// stmt.setBoolean(5, price.getIsFreeTransfer());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				indicativePriceID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return indicativePriceID;
	}

	private int insertRoute(Route route, int indicativePriceID, int responseID) {
		int routeID = 0;
		try {
			sql = "INSERT INTO " + TABLE_ROUTES + ROUTES_COLUMNS + " VALUES(?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, route.getName());
			stmt.setFloat(2, route.getDistance());
			stmt.setFloat(3, route.getDuration());
			stmt.setInt(4, indicativePriceID);
			stmt.setInt(5, responseID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				routeID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return routeID;
	}

	private void insertStop(Stop stop, int routeID, int transitSegmentID) {
		try {
			sql = "INSERT INTO " + TABLE_STOPS + STOPS_COLUMNS + " VALUES(?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, stop.getKind());
			stmt.setString(2, stop.getName());
			stmt.setString(3, stop.getPos());
			stmt.setString(4, stop.getCode());
			stmt.setString(5, stop.getCountryCode());
			stmt.setString(6, stop.getRegionCode());
			stmt.setString(7, stop.getTimeZone());
			stmt.setInt(8, routeID);
			if (transitSegmentID == 0)
				stmt.setNull(9, java.sql.Types.INTEGER);
			else
				stmt.setInt(9, transitSegmentID);

			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertOtherSegment(OtherSegment segment, int routeID,
			int indicativePriceID) {
		try {
			sql = "INSERT INTO " + TABLE_OTHERSEGMENTS + OTHERSEGMENTS_COLUMNS +
					" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, segment.getKind());
			stmt.setString(2, segment.getSubkind());
			stmt.setString(3, segment.getVehicle());
			stmt.setBoolean(4, segment.getIsMajor());
			stmt.setBoolean(5, segment.getIsImperial());
			stmt.setFloat(6, segment.getDistance());
			stmt.setFloat(7, segment.getDuration());
			stmt.setString(8, segment.getsName());
			stmt.setString(9, segment.getsPos());
			stmt.setString(10, segment.gettName());
			stmt.setString(11, segment.gettPos());
			stmt.setString(12, segment.getPath());
			stmt.setInt(13, indicativePriceID);
			stmt.setInt(14, routeID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int insertFlightSegment(FlightSegment fs, int routeID,
			int indicativePriceID) {
		int flightSegmentID = 0;
		try {
			sql = "INSERT INTO " + TABLE_FLIGHTSEGMENTS + FLIGHTSEGMENTS_COLUMNS
					+ " VALUES(?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, fs.getKind());
			stmt.setBoolean(2, fs.getIsMajor());
			stmt.setFloat(3, fs.getDistance());
			stmt.setFloat(4, fs.getDuration());
			stmt.setFloat(5, fs.getTransferDuration());
			stmt.setString(6, fs.getsCode());
			stmt.setString(7, fs.gettCode());
			stmt.setInt(8, indicativePriceID);
			stmt.setInt(9, routeID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				flightSegmentID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return flightSegmentID;
	}

	private int insertFlightLeg(FlightLeg fl, int flightSegmentID,
			int indicativePriceID) {
		int flightLegID = 0;
		try {
			sql = "INSERT INTO " + TABLE_FLIGHTLEGS + FLIGHTLEGS_COLUMNS +  " VALUES(?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, fl.getDays());
			stmt.setInt(2, flightSegmentID);
			stmt.setInt(3, indicativePriceID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				flightLegID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return flightLegID;
	}

	private int insertFlightHop(FlightHop hop, int flightLegID) {
		int flightHopID = 0;
		try {
			sql = "INSERT INTO " + TABLE_FLIGHTHOPS + FLIGHTHOPS_COLUMNS 
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, hop.getsCode());
			stmt.setString(2, hop.gettCode());
			stmt.setString(3, hop.getsTerminal());
			stmt.setString(4, hop.gettTerminal());
			stmt.setString(5, hop.getsTime());
			stmt.setString(6, hop.gettTime());
			stmt.setString(7, hop.getFlight());
			stmt.setString(8, hop.getAirline());
			stmt.setFloat(9, hop.getDuration());
			stmt.setFloat(10, hop.getlDuration());
			stmt.setString(11, hop.getAircraft());
			stmt.setInt(12, hop.getDayChange());
			stmt.setInt(13, flightLegID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				flightHopID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return flightHopID;
	}

	private void insertCodeshare(Codeshare share, int flightHopID) {
		try {
			sql = "INSERT INTO " + TABLE_CODESHARES + CODESHARES_COLUMNS + " VALUES(?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, share.getAirline());
			stmt.setString(2, share.getFlight());
			stmt.setInt(3, flightHopID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int insertTransitSegment(TransitSegment ts, int routeID,
			int indicativePriceID) {
		int transitSegmentID = 0;
		try {
			sql = "INSERT INTO " + TABLE_TRANSITSEGMENTS + TRANSITSEGMENTS_COLUMNS
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, ts.getKind());
			stmt.setString(2, ts.getSubkind());
			stmt.setString(3, ts.getVehicle());
			stmt.setBoolean(4, ts.getIsMajor());
			stmt.setBoolean(5, ts.getIsImperial());
			stmt.setFloat(6, ts.getDistance());
			stmt.setFloat(7, ts.getDuration());
			stmt.setString(8, ts.getsName());
			stmt.setString(9, ts.getsPos());
			stmt.setString(10, ts.gettName());
			stmt.setString(11, ts.gettPos());
			stmt.setString(12, ts.getPath());
			stmt.setInt(13, indicativePriceID);
			stmt.setInt(14, routeID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				transitSegmentID = rs.getInt(1);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return transitSegmentID;
	}

	private int insertTransitLeg(TransitLeg tlg, int transitSegmentID) {
		int transitLegID = 0;
		try {
			sql = "INSERT INTO " + TABLE_TRANSITLEGS + TRANSITLEGS_COLUMNS + " VALUES(?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, tlg.getUrl());
			stmt.setInt(2, transitSegmentID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				transitLegID = rs.getInt(1);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return transitLegID;
	}

	private int insertTransitHop(TransitHop hop, int transitLegID,
			int indicativePriceID) {
		int transitHopID = 0;
		try {
			sql = "INSERT INTO " + TABLE_TRANSITHOPS + TRANSITHOPS_COLUMNS
					+ " VALUES(?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, hop.getsName());
			stmt.setString(2, hop.getsPos());
			stmt.setString(3, hop.gettName());
			stmt.setString(4, hop.gettPos());
			stmt.setFloat(5, hop.getFrequency());
			stmt.setFloat(6, hop.getDuration());
			stmt.setInt(7, indicativePriceID);
			stmt.setInt(8, transitLegID);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				transitHopID = rs.getInt(1);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return transitHopID;
	}

	private void insertTransitLine(TransitLine line, int transitHopID) {
		try {
			sql = "INSERT INTO " + TABLE_TRANSITLINES + TRANSITLINES_COLUMNS
					+ " VALUES(?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, line.getName());
			stmt.setString(2, line.getVehicle());
			stmt.setString(3, line.getCode());
			stmt.setString(4, line.getAgency());
			stmt.setFloat(5, line.getFrequency());
			stmt.setFloat(6, line.getDuration());
			stmt.setString(7, line.getDays());
			stmt.setInt(8, transitHopID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	private void insertFlightItinerary(FlightHop hop, int sID, int tID,
			int fID, String fCode, float price, boolean hasStop,
			int flightLegID, int indicativePriceID, int flightHopID) {
		try {
			sql = "INSERT INTO " + TABLE_FLIGHTITINERARIES + FLIGHTITINERARIES_COLUMNS
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = con.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, sID);
			stmt.setString(2, hop.getsCode());
			stmt.setInt(3, tID);
			stmt.setString(4, hop.gettCode());
			stmt.setInt(5, fID);
			stmt.setString(6, fCode);
			stmt.setFloat(7, price);
			stmt.setFloat(8, hop.getDuration());
			stmt.setBoolean(9, hasStop);
			stmt.setInt(10, flightLegID);
			stmt.setInt(11, indicativePriceID);
			stmt.setInt(12, flightHopID);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private int getAirportByCode(String code) {
		int airportID = 0;
		try {
			sql = "SELECT ID, code FROM " + TABLE_AIRPORTS
					+ " WHERE code LIKE ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, code);
			stmt.setMaxRows(1);
			rs = stmt.executeQuery();
			while (rs != null && rs.next()) {
				airportID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return airportID;
	}

}
