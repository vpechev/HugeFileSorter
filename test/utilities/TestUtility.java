package utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TestUtility {

	@Test(expected=FileNotFoundException.class)
	public void validateFileNullReference() throws FileNotFoundException {
		File testFile = null;
		Utility.validateFile(testFile);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void validateFileUnexistingFile() throws FileNotFoundException  {
		File testFile = new File("someUnexistingFile.odt");
		Utility.validateFile(testFile);
	}
	
	@Test
	public void validateFileExistingFile() throws FileNotFoundException  {
		File testFile = new File(".project");
		Utility.validateFile(testFile);
	}
	
	@Test
	public void validateFileContentNegative(){
		String nonDigitsContent = "12331 some 23 non digits1231414";
		assertFalse(Utility.validateFileContent(nonDigitsContent));
	}
	
	@Test
	public void validateFileContentPositive(){
		String digitsContent = "12331";
		assertTrue(Utility.validateFileContent(digitsContent));
	}
	
	@Test
	public void parseStringToIntPositive(){
		String digitsContent = "12331";
		Utility.parseStringToInt(digitsContent);
	}
	
	@Test(expected=NumberFormatException.class)
	public void parseStringToIntNegative(){
		String nonDigitsContent = "3asd12";
		Utility.parseStringToInt(nonDigitsContent);
	}
	
	@Test
	public void sortList(){
		List<Integer> testList = new ArrayList<Integer>();
		Collections.addAll(testList, 2, 5, 0, 7, 6);
		
		Utility.sortList(testList);
		
		for (int i = 0; i < testList.size()-1; i++) {
			if(testList.get(i) > testList.get(i+1))
				fail();
		}
	}
}
