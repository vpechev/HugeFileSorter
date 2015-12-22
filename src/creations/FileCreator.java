package creations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utilities.Utility;
import constants.Constants;
import exceptions.InvalidFileException;

public class FileCreator {
	private static long SubFilesCount = 0;
	/**
	 * Creates file with random numbers. The size of the file is passed as argument
	 * @param fileSize
	 * @throws IOException
	 */
	public static File CreateRandomNumbersFile(long fileSize) throws IOException{
		File dir = new File("files");
				
		if(!dir.exists())
			dir.mkdir();
		
		File f = new File("files/fileForSort.txt");
		
		checkFile(f);
		
		Random rand = new Random(System.currentTimeMillis()); 
		try(PrintWriter pr = new PrintWriter(f)){
			System.out.println("Begin writing into a file");
			int numbersCount = 0;
			
			while(Files.size(f.toPath()) <= fileSize){
				int value = rand.nextInt(1_000_000); 
				pr.println(value);
				numbersCount++;
			}
			
			System.out.println(numbersCount + " numbers are writen into the file");
			System.out.println("The File is filled with random Numbers");
		}
		return f;
	}
	
	private static void checkFile(File f) throws IOException{
		if(f != null)
		{
			if(f.exists())
				f.delete();
			else
				f.createNewFile();
		}
	}
	
	public static void CreateSortedSubFiles(File f, long subFileSize) throws FileNotFoundException{
		System.out.println("Begin creating subfiles!");
		
		if(f == null || !f.exists())
			throw new FileNotFoundException();
		
		try(BufferedReader br = new BufferedReader(new FileReader(f))) {
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
	
	private static void appendToList(List<Integer> fileExtractionList, int element) throws IOException{
		if( fileExtractionList.size() >= Constants.MAX_SUBFILE_COLLECTION_ELEMENTS_COUNT ){
			writeListToFile(fileExtractionList);
			fileExtractionList.clear();
		}
		
		fileExtractionList.add(element);
	}
	
	
	
	private static void writeListToFile(List<Integer> list) throws IOException{
		File outerDir = new File("files");
		File innerDir = new File("files/subfiles");
		
		if(!outerDir.exists())
			outerDir.mkdir();
		
		if(!innerDir.exists())
			innerDir.mkdir();
						
		String fileName = "subFile_" + (SubFilesCount++) + ".txt";
		String pathString = "files/subFiles/" + fileName;
		File file = new File(pathString);
		
		checkFile(file);
		
		list = Utility.sortList(list);
		
		try(PrintWriter pr = new PrintWriter(file)){
			for(int i : list){
				pr.println(i);
			}
		}
	}
	
}
