import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import kong.unirest.Unirest;

public class FirstHomework {

	public static void main(String[] args) throws IOException {

		String DepartureYear = "2019";
		String DepartureMonth = "09";
		String flyFrom = "OSL";
		String flyTo = "RIX";
		int departureDays = 30;
		System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

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

			ArrayList<MinValue> prices = new ArrayList<MinValue>();

			for (Element flightResult : htmlSnippset.select("table.avadaytable > tbody > tr.rowinfo1")) {

				final String price = flightResult.child(4).text();
				try {
					prices.add(new MinValue(Double.parseDouble(price),
							flightResult.child(3).child(0).childNodes().get(0).attributes().get("id")));
				} catch (NumberFormatException e) {

				}
			}

			 //System.out.println(prices);

			MinValue minPrice = getMin(prices);
			// System.out.println(minPrice.id + ". " + minPrice.value);

			if (minPrice.value != null) {

				driver.get("https://www.norwegian.com/en/ipc/availability/avaday?D_City=" + flyFrom + "&A_City=" + flyTo
						+ "&TripType=1&D_Day=" + departureDay + "&D_Month=201909&D_SelectedDay=" + selectedDay
						+ "&R_Day=" + returnDay
						+ "&R_Month=201909&R_SelectedDay=01&IncludeTransit=false&AgreementCodeFK=-1&CurrencyCode=EUR&rnd=34917&processid=72207&mode=ab");

				driver.findElement(By.id(minPrice.id)).click();

				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				WebElement taxes = driver.findElement(By.cssSelector(
						"#ctl00_MainContent_ipcAvaDay_upnlResSelection > div:nth-child(1) > div > table > tbody > tr:nth-child(18) > td.rightcell.emphasize"));
				if (taxes != null) {

				}

				WebElement price = driver.findElement(By.cssSelector(
						"#avaday-outbound-result > div > div > div.bodybox > div > table > tbody > tr.selectedrow.rowinfo1 > td.fareselect.selectedfare > div > label"));
				if (price != null) {

				}

				WebElement depTime = driver.findElement(By.cssSelector(
						"#avaday-outbound-result > div > div > div.bodybox > div > table > tbody > tr.selectedrow.rowinfo1 > td.depdest > div"));
				if (depTime != null) {

				}

				WebElement arrTime = driver.findElement(By.cssSelector(
						"#avaday-outbound-result > div > div > div.bodybox > div > table > tbody > tr.selectedrow.rowinfo1 > td.arrdest > div"));
				if (arrTime != null) {

				}

				WebElement from = driver.findElement(By.cssSelector(
						"#avaday-outbound-result > div > div > div.bodybox > div > table > tbody > tr.selectedrow.rowinfo2 > td.depdest > div"));
				if (from != null) {

				}

				WebElement to = driver.findElement(By.cssSelector(
						"#avaday-outbound-result > div > div > div.bodybox > div > table > tbody > tr.selectedrow.rowinfo2 > td.arrdest > div"));
				if (to != null) {

				}

				System.out.println("Departure airport: " + from.getText() + "\nDeparture time: " + depTime.getText()
						+ "\nArrival airport: " + to.getText() + "\nArrival time: " + arrTime.getText()
						+ "\nTicket price: " + price.getText() + "€" + "\nTaxes: " + taxes.getText() + "€" + "\n\n");

			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		driver.quit();
	}

	// Method for getting the minimum value
	public static MinValue getMin(ArrayList<MinValue> inputList) {
		if (!inputList.isEmpty()) {
			MinValue minValue = inputList.get(0);
			for (MinValue price : inputList) {
				if (price != null & price.value < minValue.value) {
					minValue = price;
				}
			}
			return minValue;
		}
		return new MinValue(null, "-1");
	}

}