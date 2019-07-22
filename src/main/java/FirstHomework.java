import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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

			System.out.println("\nFlying date: " + DepartureYear + "-" + DepartureMonth + "-" + departureDay);

			final kong.unirest.HttpResponse<String> response = Unirest
					.get("https://www.norwegian.com/en/ipc/availability/avaday?D_City=" + flyFrom + "&A_City=" + flyTo
							+ "&TripType=1&D_Day=" + departureDay + "&D_Month=201909&D_SelectedDay=" + selectedDay
							+ "&R_Day=" + returnDay
							+ "&R_Month=201909&R_SelectedDay=01&IncludeTransit=false&AgreementCodeFK=-1&CurrencyCode=EUR&rnd=34917&processid=72207&mode=ab")
					.asString();

			// System.out.println(response.getBody());

			final Document htmlSnippset = Jsoup.parseBodyFragment(response.getBody());

			ArrayList<Double> prices = new ArrayList<Double>();

			for (Element flightResult : htmlSnippset.select("table.avadaytable > tbody > tr.rowinfo1")) {

				// final String depTime = flightResult.child(0).text();
				// final String arrTime = flightResult.child(1).text();

				final String price = flightResult.child(4).text();

				
				try {
					prices.add(Double.parseDouble(price));
				} catch (NumberFormatException e) {
					// System.out.println("there was an error" + e.getMessage());
				}
			}

		
		// System.out.println(prices);

		MinValue minPrice = getMin(prices);
		System.out.println(minPrice.id + ". " + minPrice.value);
		/*
		 * out.write("Flying date: " + DepartureYear + "-" + DepartureMonth + "-" +
		 * departureDay + "\nDeparture airport: " + flyFrom + "\nDeparture time: " +
		 * depTime + "\nArrival airport: " + flyTo + "\nArrival time: " + arrTime +
		 * "\nTicket price: " + price + "�\n\n");
		 * 
		 * System.out.println("Departure airport: " + flyFrom + "\nDeparture time: " +
		 * depTime + "\nArrival airport: " + flyTo + "\nArrival time: " + arrTime +
		 * "\nTicket price: " + price + "�\n\n");
		 */

		WebDriver driver = new ChromeDriver();
		driver.get("https://www.norwegian.com/en/ipc/availability/avaday?D_City="+flyFrom +"&A_City="+flyTo+"&TripType=1&D_Day="+departureDay+"&D_Month=201909&D_SelectedDay="+selectedDay+"&R_Day="+returnDay
				+ "&R_Month=201909&R_SelectedDay=01&IncludeTransit=false&AgreementCodeFK=-1&CurrencyCode=EUR&rnd=34917&processid=72207&mode=ab");
		
		driver.findElement(By.id("FlightSelectOutboundStandardLowFare"+minPrice.id)).click();
		
	
		
	}out.close();
	}


// Method for getting the minimum value
	public static MinValue getMin(ArrayList<Double> inputList) {
	      if (!inputList.isEmpty()) {
	         Double minValue = inputList.get(0);
	         int i = 0;
	         for (Double price : inputList) {
	            if (price !=null & price < minValue) {
	               minValue = price;
	            }
	            i++;
	         }
	         return new MinValue(minValue, i);
	      }
	      return new MinValue(null, -1);
	   }

	}