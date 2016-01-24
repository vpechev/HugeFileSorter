package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constants.Constants;

public class Utility {
	
	/**
	 * sorts the passed list. Currently uses the default Collections.sort() sorting
	 * @param list list with unordered integers, that would be sorted.
	 * @return
	 */
	public static void sortList(List<Integer> list){
		Collections.sort(list);
	}
	
	
	/**
	 * reads the next line from the passed reader
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String readNextLine(BufferedReader reader) throws IOException{
		return reader.readLine();
	}
	
	
	/**
	 * parse the passed string to integer
	 * @param inputString string that would be passed
	 * @return
	 */
	public static int parseStringToInt(String inputString){
		return Integer.valueOf(inputString);
	}
	
	
	/**
	 * Writes the passed number using the passed writer
	 * @param writer
	 * @param number
	 */
	public static synchronized void WriteNumbersToStream(PrintWriter writer, int...numbers){
		for (int number : numbers) {
			writer.println(number);			
		}
	}
	
	
	/**
	 * checks if the passed input contains only numbers
	 * @param fileContent string representation of the file content
	 * @return true if the input parameter contains only numbers
	 */
	public static boolean validateFileContent(String fileContent){
		Pattern p = Pattern.compile("^[0-9]*$");
	    Matcher m = p.matcher(fileContent);
	    boolean b = m.matches() || fileContent.contains(Constants.FILE_DELIMETER);
	    return b;
	}
	
	
	/**
	 * By passed directory returns arrayList, which elements are reference to the files in this directory
	 * @param filesDir
	 * @return
	 * @throws IOException
	 */
	public static List<File> GetSubFilesFromDirectory(Path filesDir) throws IOException{
		List<File> subFiles = new ArrayList<>();
        
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(filesDir))
        {
            for (Path file : dirStream)
            {
                if (Files.isRegularFile(file)  &&  file.toString().endsWith(Constants.FILE_TYPE))
                {
                	subFiles.add(file.toFile());
                }
            }
        }
        return subFiles;
	}
	
	
	/**
	 * Checks if the passed file exists
	 * @param file 
	 * @throws FileNotFoundException
	 */
	public static void validateFile(File file) throws FileNotFoundException{
		if(file == null || !file.exists())
			throw new FileNotFoundException();
	}
	
	
	/**
	 * Cleans the working folder, where subfiles used for creating of subfiles are created. 
	 * Uses walkFileTree() method for walking throw the directory.
	 * @throws IOException
	 */
	public static void cleanWorkingDirectory() throws IOException{
		System.out.println("Begin cleaning working directory");
		
		File folder = new File(Constants.MAIN_FILES_FOLDER_NAME + "/" + Constants.SUB_FILES_FOLDER_NAME);
		if(folder.exists()){
		   Path directory = Paths.get(folder.getAbsolutePath());
		   Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				   Files.delete(file);
				   return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				   Files.delete(dir);
				   return FileVisitResult.CONTINUE;
			   }

		   });
		}
		
		System.out.println("Working directory successfully cleaned!");
	}
	
	
	/**
	 * deletes the merges subfiles
	 * @throws IOException
	 */
	public static void deleteParticularFiles() throws IOException{
		File folder = new File(Constants.MAIN_FILES_FOLDER_NAME);
		if(folder.exists()){
		   Path directory = Paths.get(folder.getAbsolutePath());
		   Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				   if(file.getFileName().toString().charAt(0) == 'x')
					   Files.delete(file);
				   return FileVisitResult.CONTINUE;
			   }
		   });
		}
	}
	
	/**
	 * If the passed file is directory, checks if this directory exists. If not, new empty directory would be created
	 * @param dir
	 */
	public static void checkDirectory(File dir){
		if(dir != null && !dir.exists())
			dir.mkdir();
	}
	
	
	/**
	 * If the passed file exists it should be deleted an then created new empty file
	 * @param f
	 * @throws IOException
	 */
	public static void checkFile(File f) throws IOException{
		if(f != null)
		{
			if(f.exists())
				f.delete();
			
		}
	}
}
