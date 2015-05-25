package ro.ase.csie.findAway.server.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import ro.ase.csie.findAway.server.model.api.GeocodeResponse;
import ro.ase.csie.findAway.server.model.api.Place;
import ro.ase.csie.findAway.server.model.api.Position;

public class GeocodeApiHandler {

	public GeocodeResponse getApiResponse(String place) {
		GeocodeResponse response = null;
		try {
			URL url = new URL(
					"http://free.rome2rio.com/api/1.2/xml/Geocode?key=XtEsWlQh&query="
							+ place.replace(" ", "%20"));
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String inputLine;
			String result = "";

			while ((inputLine = in.readLine()) != null)
				result += inputLine;

			JAXBContext jc = JAXBContext.newInstance(GeocodeResponse.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			StringReader reader = new StringReader(result);
			response = (GeocodeResponse) unmarshaller.unmarshal(reader);

			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return response;
	}

	public Position getPlacePosition(String placeName) {
		Position pos = null;
		GeocodeResponse response = getApiResponse(placeName);
		if (response != null) {
			Place place = response.getPlaces().get(0);
			pos = new Position(place.getLat(), place.getLng());
			System.out.println(place);
		}
		return pos;
	}
}
