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
		collectZip();
		collectRoofSize();
		
		//estimate startup cost
		calculateInstallCosts();
		
		//calculate savings per month to find the estimated time to break even 
		calculateSavings();
	}
	
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
	
	
	public static void collectZip(){
		System.out.println("Enter your Zipcode");
		Scanner scanner = new Scanner (System.in);
		zipcode = scanner.next();
	}
	
	public static void collectRoofSize(){
		System.out.println("Enter your roof size (in terms of squared meters)");
		Scanner scanner = new Scanner (System.in);
		roofSize= scanner.nextDouble();
	}
	
	public static void calculateInstallCosts(){
		panels = (roofSize*.75)/1.6;//average panel is 1.6 meters squared
		double laborCost = panels * 300;//average labor cost is about $300 per panel according to https://www.consumeraffairs.com/solar-energy/how-much-do-solar-panels-cost.html#:~:text=Labor:%20Solar%20installation%20costs%20include,caps%20residential%20fees%20at%20$450)
		double mountCost = panels * 200;//average cost of hardware for mounting
		monoInstallCost = (laborCost + mountCost + (panels * 300.0) + 2000.0); //2000 for the inverters and 300 per monocrystalline panel
		polyInstallCost = (laborCost + mountCost + (panels * 200.0) + 2000.0); //200 per polycrystalline panel
		System.out.println("The initial cost for installing monocrystalline panels; " + monoInstallCost);
		System.out.println("The initial cost for installing polycrystalline panels; " + polyInstallCost);
	}
	/**A method that calculates and prints the savings dependant on the irradiance, roof rize, and calculates for both mono and polycrystalline
	 * 
	 * 
	 */
	public static void calculateSavings(){
		//find the total savings in a year
		double rad = zipToRad.get(zipcode);
		double allWatts = panels * 300.0;
		double dailyWatts = (rad * allWatts)/1000;//(the total watts based on solar irradiance/ 1000) this converts the totals watts per day to kilo watts per day
		double yearlyWatts = dailyWatts * 365;
		
		double yearlySavings = yearlyWatts * .15;//.15 cents is how much PSE charges for one kwh
		//print the results
		double monoYearsTillEven = monoInstallCost/yearlySavings;
		double polyYearsTillEven = polyInstallCost/yearlySavings;
		System.out.println("Years till you break even on investment(Monocrystalline); " + monoYearsTillEven);
		System.out.println("Years till you break even on investment(Polycrystalline); " + polyYearsTillEven);
	}
}

