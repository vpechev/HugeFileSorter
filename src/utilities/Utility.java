package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	public static List<Integer> sortList(List<Integer> list){
		Collections.sort(list);
		return list;	
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
		File folder = new File(Constants.MAIN_FILES_FOLDER_NAME);
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
	}
	
	
	/**
	 * If the passed file is directory, checks if this directory exists. If not, new empty directory would be created
	 * @param dir
	 */
	public static void checkDirectory(File dir){
		if(dir != null && dir.isDirectory() && !dir.exists())
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
