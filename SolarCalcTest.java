import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import java.lang.*;
import java.nio.*;
import java.io.*;
import java.util.function.*;
import java.util.stream.*;
import java.nio.file.*;


/**
 * JUnit Tests for SolarCalc
 */
public class SolarCalcTest {
	private double radThree, radFour, radFive, roofThree, roofFour, bigRoof;
	private String zipOne,zipTwo;
	@BeforeEach 
	void reset(){
		double radThree = 3.0;
		double radFour =  4.0;
		double radFive = 5.0;
		
		roofThree = 30.0;
		roofFour = 40.0;
		bigRoof = 100.0;
		
		zipOne = "98312";
		zipTwo = "98908";
		
		Path emptyPath = Paths.get("notReal.txt");
	}
	
	/**
	 * A method to test IOException
	 */
	@Test
	public void testCreateHashThrowsIOE(){
		//assertThrows(SolarCalc.createHash(emptyPath),IOException);
	}
	/**
	 * A method to test calculateInstallCostsMono
	 */
	@Test
	public void testCalculateInstallCostsMono(){
		assertEquals(SolarCalc.calculateInstallCostsMono(roofThree),13250.0);
	}
	
	/**
	 * A method to test calculateInstallCostsPoly
	 */
	@Test
	public void testCalculateInstallCostsPoly(){
		assertEquals(SolarCalc.calculateInstallCostsPoly(roofThree),11843.75);
	}
	
	/**
	 * A method to test calculateSavingsMono using 30 square meter roof
	 */
	@Test
	public void testCalculateSavingsMono(){
		assertEquals(SolarCalc.calculateSavingsPoly(zipOne,roofThree),18.50487989831045);
	}
	
	/**
	 * A method to test calculateSavingsPoly using 30 square meter roof
	 */
	@Test
	public void testCalculateSavingsPoly(){
		assertEquals(SolarCalc.calculateSavingsPoly(zipTwo,roofThree),10.682676588308247);
	}
	
}

