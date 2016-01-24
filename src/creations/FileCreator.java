package creations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import utilities.Utility;
import constants.Constants;
import exceptions.InvalidFileException;

public class FileCreator {
	private static long SubFilesCount = 0;
	
	
	/**
	 * Creates file with random integer numbers
	 * @param fileSize size of the file that will be created
	 * @throws IOException
	 */
	public static File CreateRandomNumbersFile(long fileSize) throws IOException{
		File dir = new File(Constants.MAIN_FILES_FOLDER_NAME);
		
		Utility.createDirectory(dir);
		
		File f = new File(Constants.MAIN_FILES_FOLDER_NAME + "/fileForSort" + Constants.FILE_TYPE);
		
		Utility.recreateFile(f);
		
		Random rand = new Random(System.currentTimeMillis()); 
		try(PrintWriter pr = new PrintWriter(f)){
			System.out.println("Begin writing into a file.");
			
			while(Files.size(f.toPath()) <= fileSize){
				int value = rand.nextInt(Constants.MAX_INTEGER_NUMBER); 
				pr.println(value);
			}
			
			System.out.println("The File is filled with random Numbers.");
		}
		return f;
	}
	
	
	/**
	 * Divides the passed file into subfiles, which count is defined by the passed size for each subFile
	 * @param file passed file for dividing
	 * @param subFileSize the size of each subfile
	 * @throws FileNotFoundException
	 */
	public static void CreateSortedSubFiles(File file, long subFileSize) throws FileNotFoundException{
		System.out.println("Begin creating subfiles!");
		
		Utility.validateFile(file);
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String text = null;
		    
		    List<Integer> fileExtractionList = new ArrayList<Integer>();
		    
		    while ((text = br.readLine()) != null) {
		    	if(!Utility.validateFileContent(text))
		    	{
		    		throw new InvalidFileException("The passed file content formatting is not supported");
		    	}
		    	
	    		if(text.contains(Constants.FILE_DELIMETER)){
	    			String[] numStrings = text.split(Constants.FILE_DELIMETER);
		    		
		    		for(String s : numStrings){
		    			appendToList(fileExtractionList, Integer.valueOf(s));
		    		}	
	    		}
	    		else
	    			appendToList(fileExtractionList, Integer.valueOf(text));
		    		
		    }
		    
		    // After the whole file is read, check if the collection contains some remained elements
		    if(fileExtractionList.size() > 0){
		    	writeListToFile(fileExtractionList);
		    }
		    
		    System.out.println("Done creating subfiles!");
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Appends the passed element to the collection. If the elements count is more than the maximum allowed elements, 
	 * the collection will be written to a file and this collection will be cleared.
	 * 
	 * @param fileExtractionList
	 * @param element
	 * @throws IOException
	 */
	private static void appendToList(List<Integer> fileExtractionList, int element) throws IOException{
		if( fileExtractionList.size() >= Constants.MAX_SUBFILE_COLLECTION_ELEMENTS_COUNT ){
			System.out.println(Arrays.toString(fileExtractionList.toArray()).substring(0, 100));
			writeListToFile(fileExtractionList);
			fileExtractionList.clear();
		}
		
		fileExtractionList.add(element);
	}
	
	
	/**
	 * writes the passed list to a file 
	 * @param list
	 * @throws IOException
	 */
	private static void writeListToFile(List<Integer> list) throws IOException{
		File outerDir = new File(Constants.MAIN_FILES_FOLDER_NAME);
		File innerDir = new File(Constants.MAIN_FILES_FOLDER_NAME + "/" + Constants.SUB_FILES_FOLDER_NAME);
		
		Utility.createDirectory(outerDir);
		Utility.createDirectory(innerDir);
						
		String fileName = "subFile_" + (SubFilesCount++) + Constants.FILE_TYPE;
		String pathString = Constants.MAIN_FILES_FOLDER_NAME + "/" + Constants.SUB_FILES_FOLDER_NAME + "/" + fileName;
		File file = new File(pathString);
		
		Utility.recreateFile(file);
		
		Utility.sortList(list);
		
		try(PrintWriter pr = new PrintWriter(file)){
			for(int i : list){
				pr.println(i);
			}
		}
	}	
}
