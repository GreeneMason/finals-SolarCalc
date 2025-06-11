import java.util.*;
import java.nio.*;
import java.io.*;
import java.util.function.*;
import java.util.stream.*;
import java.nio.file.*;


public class SolarCalc {
	private static Map<String, Double> zipToRad = new HashMap<>();
	private static String zipcode;
	private static double panels, roofSize, polyInstallCost, monoInstallCost;
	
	
	public static void main (String[] args) {
		//create the hashMap
		createHash();
		
		//collect answers from user
		zipcode = collectZip();
		roofSize = collectRoofSize();
		
		//estimate startup cost
		System.out.println("The cost of installing Monocrystalline panels; " + calculateInstallCostsMono(roofSize));
		System.out.println("The cost of installing Polycrystalline panels; " + calculateInstallCostsPoly(roofSize));
		
		//calculate savings per month to find the estimated time to break even 
		System.out.println("Years till breaking even on investment; " + calculateSavingsMono());
		System.out.println("Years till breaking even on investment; " + calculateSavingsPoly());
		
		
	}
	/** 
	 * A method that reads the .txt file and creats a hashMap, only works with a 4 column format seperated by commas. 
	 */
	public static void createHash (){
		try(Stream<String> lines = Files.lines(Paths.get("zipToIrradiance.txt"))){;
		zipToRad = lines
			.map(line -> line.split(","))
			.filter(columns -> columns.length >= 4)
			//.peek(columns -> System.out.println(Arrays.toString(columns)))
			.collect(Collectors.toMap(
				columns -> columns[0].trim(),
				columns ->Double.parseDouble(columns[3].trim())
				));
		}catch(IOException ioe){
			System.out.println("System could not read file" + ioe);
		}
		}
	
	/** A method to collect the zipcode
	 * @return String representing a zipcode
	 */
	public static String collectZip(){
		System.out.println("Enter your Zipcode");
		Scanner scanner = new Scanner (System.in);
		String temp = scanner.next();
		if(temp.length() != 5){
			System.out.println("Zipcode invalid, Washington Zipcodes only");
			collectZip();
		}
		return temp;
	}
	/** A method to collect the rood size in meters squared
	 * @return double representing the roof size in meters squared
	 */
	public static double collectRoofSize(){
		System.out.println("Enter your roof size (in terms of squared meters)");
		Scanner scanner = new Scanner (System.in);
		double temp = scanner.nextDouble();
		return temp;
	}
	/** A method to calulate the intallation cost for
	 * 
	 */
	public static double calculateInstallCostsMono(double roofSize){
		panels = (roofSize*.75)/1.6;//average panel is 1.6 meters squared
		double laborCost = panels * 300;//average labor cost is about $300 per panel according to https://www.consumeraffairs.com/solar-energy/how-much-do-solar-panels-cost.html#:~:text=Labor:%20Solar%20installation%20costs%20include,caps%20residential%20fees%20at%20$450)
		double mountCost = panels * 200;//average cost of hardware for mounting
		monoInstallCost = (laborCost + mountCost + (panels * 300.0) + 2000.0); //2000 for the inverters and 300 per monocrystalline panel
		return monoInstallCost;
	}
	
	/** A method to calulate the intallation cost for
	 * 
	 */
	public static double calculateInstallCostsPoly(double roofSize){
		panels = (roofSize*.75)/1.6;//average panel is 1.6 meters squared
		double laborCost = panels * 300;//average labor cost is about $300 per panel according to https://www.consumeraffairs.com/solar-energy/how-much-do-solar-panels-cost.html#:~:text=Labor:%20Solar%20installation%20costs%20include,caps%20residential%20fees%20at%20$450)
		double mountCost = panels * 200;//average cost of hardware for mounting
		polyInstallCost = (laborCost + mountCost + (panels * 200.0) + 2000.0);//2000 for the inverters and 200 per polycrystalline panel
		return polyInstallCost;
	}
	/**A method that calculates and prints the savings dependant on the irradiance, roof rize, and calculates for monocrystalline panels
	 * 
	 * @return a double representing the ammount of years needed to break even
	 */
	public static double calculateSavingsMono(){
		//find the total savings in a year
		double rad = zipToRad.get(zipcode);
		double allWatts = panels * 300.0;
		double dailyWatts = (rad * allWatts)/1000;//(the total watts based on solar irradiance/ 1000) this converts the totals watts per day to kilo watts per day
		double yearlyWatts = dailyWatts * 365;
		
		double yearlySavings = yearlyWatts * .15;//.15 cents is how much PSE charges for one kwh
		//print the results
		double monoYearsTillEven = monoInstallCost/yearlySavings;
		return monoYearsTillEven;
	}
	
	/**A method that calculates and prints the savings dependant on the irradiance, roof rize, and calculates for polycrystalline panels
	 * 
	 * @return a double representing the ammount of years needed to break even
	 */
	public static double calculateSavingsPoly(){
		//find the total savings in a year
		double rad = zipToRad.get(zipcode);
		double allWatts = panels * 300.0;
		double dailyWatts = (rad * allWatts)/1000;//(the total watts based on solar irradiance/ 1000) this converts the totals watts per day to kilo watts per day
		double yearlyWatts = dailyWatts * 365;
		
		double yearlySavings = yearlyWatts * .15;//.15 cents is how much PSE charges for one kwh
		//print the results
		double polyYearsTillEven = polyInstallCost/yearlySavings;
		return polyYearsTillEven;
	}
}

