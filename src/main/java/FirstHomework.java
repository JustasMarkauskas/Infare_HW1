import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import kong.unirest.Unirest;

public class FirstHomework {

	public static void main(String[] args) throws IOException {

		final String DepartureYear = "2019";
		final String DepartureMonth = "09";
		final String flyFrom = "OSL";
		final String flyTo = "RIX";
		int departureDays = 30;

		final PrintWriter out = new PrintWriter("Flight_info.txt");
		
		for (int i = 1; i <= departureDays; i++) {

			int day = i;
			final String departureDay = String.valueOf(day);
			final String selectedDay = departureDay;
			final String returnDay = departureDay;

			System.out.println("Flying date: " + DepartureYear + "-" + DepartureMonth + "-" + departureDay + "\n");

			final kong.unirest.HttpResponse<String> response = Unirest
					.get("https://www.norwegian.com/en/ipc/availability/avaday?D_City=" + flyFrom + "&A_City=" + flyTo
							+ "&TripType=1&D_Day=" + departureDay + "&D_Month=201909&D_SelectedDay=" + selectedDay
							+ "&R_Day=" + returnDay
							+ "&R_Month=201909&R_SelectedDay=01&IncludeTransit=false&AgreementCodeFK=-1&CurrencyCode=EUR&rnd=34917&processid=72207&mode=ab")
					.asString();

			// System.out.println(response.getBody());

			final Document htmlSnippset = Jsoup.parseBodyFragment(response.getBody());

			for (Element flightResult : htmlSnippset.select("table.avadaytable > tbody > tr.rowinfo1")) {

				final String depTime = flightResult.child(0).text();
				final String arrTime = flightResult.child(1).text();
				final String price = flightResult.child(4).text();

				out.write("Flying date: " + DepartureYear + "-" + DepartureMonth + "-" + departureDay + "\nDeparture airport: " + flyFrom + "\nDeparture time: " + depTime + "\nArrival airport: "
								+ flyTo + "\nArrival time: " + arrTime + "\nTicket price: " + price + "€\n\n");
				
				System.out.println("Departure airport: " + flyFrom + "\nDeparture time: " + depTime + "\nArrival airport: "
								+ flyTo + "\nArrival time: " + arrTime + "\nTicket price: " + price + "€\n\n");
			}

		}
		out.close();
	}

}
