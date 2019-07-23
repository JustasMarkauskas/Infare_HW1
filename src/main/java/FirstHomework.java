
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
       

        //final PrintWriter out = new PrintWriter("Flight_info.txt");

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
             //   final String depTime = flightResult.child(0).text();
                // final String arrTime = flightResult.child(1).text();


                final String price = flightResult.child(4).text();
                try {
                    prices.add(new MinValue(Double.parseDouble(price), flightResult.child(3).child(0).childNodes().get(0).attributes().get("id")));
                } catch (NumberFormatException e) {
                    
                }
            }


             //System.out.println(prices);

            MinValue minPrice = getMin(prices);
           //System.out.println(minPrice.id + ". " + minPrice.value);
           
            if (minPrice.value != null) {
            	WebDriver driver = new ChromeDriver();
                driver.get("https://www.norwegian.com/en/ipc/availability/avaday?D_City=" + flyFrom + "&A_City=" + flyTo + "&TripType=1&D_Day=" + departureDay + "&D_Month=201909&D_SelectedDay=" + selectedDay + "&R_Day=" + returnDay
                        + "&R_Month=201909&R_SelectedDay=01&IncludeTransit=false&AgreementCodeFK=-1&CurrencyCode=EUR&rnd=34917&processid=72207&mode=ab");

                driver.findElement(By.id(minPrice.id)).click();
                
              try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}        
             
                          
             // WebElement element;
             // = driver.findElement(By.cssSelector("#avaday-outbound-result > div > div > div.bodybox > div > table > tbody > tr.evenrow.selectedrow.rowinfo1 > td.fareselect.standardlowfare.selectedfare > div > label"));
              
            //  driver.toString();
              //System.out.println(departureDay);
              
             
             // String response2 = driver.getPageSource();
               
               
               
               
             //  System.out.println(response2);
                
             // String body2 = response.getBody();
             // System.out.println(body2);
             
                          
             // WebElement dt = driver.findElement(By.cssSelector("tr.selectedrow.rowinfo1"));
              
              //System.out.println(dt);
              //for (Element flightResult2 : body2.se   htmlSnippset2.select("table.avadaytable > tbody > tr.selectedrow.rowinfo1"))
                
                
                //final Document htmlSnippset2 = Jsoup.parseBodyFragment(response2.getBody());
               // System.out.println(htmlSnippset2);
                
                
				try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			
                
                
              for (Element flightResult : htmlSnippset.select("table.avadaytable > tbody > tr.selectedrow.rowinfo1")) {
                    
                	
                	
                	final String depTime = flightResult.child(0).child(0).text();
                    final String arrTime = flightResult.child(0).child(1).text();
                    final String price = flightResult.child(0).child(4).text();
                    //final String taxes = flightResult.;
                    //final String depAirport= flightResult.;
                   //final String arrAirport = flightResult.;
                    
                    System.out.println(depTime);
                   
              
                
               }
                

                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                driver.close();
                
            }

        }
        
        //out.close();
    }


    // Method for getting the minimum value
    public static MinValue getMin(ArrayList<MinValue> inputList) {
        if (!inputList.isEmpty()) {
            MinValue minValue = inputList.get(0);
            int i = 0;
            for (MinValue price : inputList) {
                if (price != null & price.value < minValue.value) {
                    minValue = price;
                }
                i++;
            }
            return minValue;
        }
        return new MinValue(null, "-1");
    }

}
