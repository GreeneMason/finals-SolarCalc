import java.util.*;
import java.nio.*;
import java.io.*;
import java.util.function.*;
import java.util.stream.*;
import java.nio.file.*;


public class SolarCalc {
	private static Map<String, Double> zipToRad = new HashMap<>();
	private static String zipcode;
	private static boolean continueCheck;
	private static double panels, roofSize, polyInstallCost, monoInstallCost;
	private static ArrayList<String> allZipcode = new ArrayList<>();
	private static ArrayList<Double> allRoofSize = new ArrayList<>();
	private static ArrayList<Double> allMonoInstallCosts = new ArrayList<>();
	private static ArrayList<Double> allPolyInstallCosts = new ArrayList<>();
	private static ArrayList<Double> allMonoYearsTillEven = new ArrayList<>();
	private static ArrayList<Double> allPolyYearsTillEven = new ArrayList<>();
	
	
	public static void main (String[] args) {
		Path file = Paths.get("zipToIrradiance.txt");
		//create the hashMap
		createHash(file);
		
		continueCheck = true;
		while(continueCheck == true){
		//collect answers from user
		zipcode = collectZip();
		roofSize = collectRoofSize();
		allZipcode.add(zipcode);
		allRoofSize.add(roofSize);
		
		//estimate startup cost
		System.out.println("The cost of installing Monocrystalline panels; " + calculateInstallCostsMono(roofSize));
		
		System.out.println("The cost of installing Polycrystalline panels; " + calculateInstallCostsPoly(roofSize));
		
		//calculate savings per month to find the estimated time to break even 
		System.out.println("Years till breaking even on investment with monocrystalline; " + calculateSavingsMono(zipcode,roofSize));
		System.out.println("Years till breaking even on investment with polycrystalline; " + calculateSavingsPoly(zipcode,roofSize));
		//ask if the user would like to continue
		System.out.println("Would you like to calculate more?(Yes/No)");
		Scanner scanner = new Scanner (System.in);
		String tempOne = scanner.next();
		tempOne = tempOne.toLowerCase();
		if (tempOne.equals("no")){
			continueCheck = false;
		}
	}
		printAll();
	}
	/** 
	 * A method that reads the .txt file and creats a hashMap, only works with a 4 column format seperated by commas. 
	 */
	public static void createHash (Path file){
		try(Stream<String> lines = Files.lines(file)){;
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
		System.out.println("Enter your Zipcode, or quit to exit");
		Scanner scanner = new Scanner (System.in);
		String temp = scanner.next();
		if(temp.length() != 5){
			System.out.println("Zipcode invalid, Washington Zipcodes only");
			return collectZip();
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
		allMonoInstallCosts.add(monoInstallCost);
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
		allPolyInstallCosts.add(polyInstallCost);
		return polyInstallCost;
	}
	/**A method that calculates and prints the savings dependant on the irradiance, roof rize, and calculates for monocrystalline panels
	 * 
	 * @return a double representing the ammount of years needed to break even
	 */
	public static double calculateSavingsMono(String zipcode, double roofSize){
		//find the total savings in a year
		panels = (roofSize*.75)/1.6;
		double rad = zipToRad.get(zipcode);
		double allWatts = panels * 300.0;
		double dailyWatts = (rad * allWatts)/1000;//(the total watts based on solar irradiance/ 1000) this converts the totals watts per day to kilo watts per day
		double yearlyWatts = dailyWatts * 365;
		
		double yearlySavings = yearlyWatts * .15;//.15 cents is how much PSE charges for one kwh
		//print the results
		double monoYearsTillEven = monoInstallCost/yearlySavings;
		
		allMonoYearsTillEven.add(monoYearsTillEven);
		return monoYearsTillEven;
	}
	
	/**A method that calculates and prints the savings dependant on the irradiance, roof rize, and calculates for polycrystalline panels
	 * 
	 * @return a double representing the ammount of years needed to break even
	 */
	public static double calculateSavingsPoly(String zipcode,double roofSize){
		//find the total savings in a year
		panels = (roofSize*.75)/1.6;
		double rad = zipToRad.get(zipcode);
		double allWatts = panels * 300.0;
		double dailyWatts = (rad * allWatts)/1000;//(the total watts based on solar irradiance/ 1000) this converts the totals watts per day to kilo watts per day
		double yearlyWatts = dailyWatts * 365;
		
		double yearlySavings = yearlyWatts * .15;//.15 cents is how much PSE charges for one kwh
		//print the results
		double polyYearsTillEven = polyInstallCost/yearlySavings;
		allPolyYearsTillEven.add(polyYearsTillEven);
		return polyYearsTillEven;
	}
	/**
	 * A method that prints all of the outputs in accending order of efficiency
	 * 
	 */
	public static void printAll(){
		for(int i = 0; i < allZipcode.size(); i++){
			System.out.println("Zipcode; " + allZipcode.get(i));
			System.out.println("Roof Size; " + allRoofSize.get(i) + " m^2" );
			System.out.println("Installation cost (Monocrystalline); $" + allMonoInstallCosts.get(i));
			System.out.println("Installation cost (Polycrystalline); $" + allPolyInstallCosts.get(i));
			System.out.println("Years until breaking even (Monocrystalline); " + allMonoYearsTillEven.get(i));
			System.out.println("Years until breaking even (Monocrystalline); " + allPolyYearsTillEven.get(i));
		}
	}
}

