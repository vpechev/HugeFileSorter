package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constants.Constants;

public class Utility {
	
	public static List<Integer> sortList(List<Integer> list){
		Collections.sort(list);
		return list;	
	}
	
	public static boolean validateFileContent(String input){
		Pattern p = Pattern.compile("^[0-9]*$");
	    Matcher m = p.matcher(input);
	    boolean b = m.matches() || input.contains(Constants.FILE_DELIMETER);
	    return b;
	}
	
	public static List<File> GetSubFilesFromDirectory(Path filesDir) throws IOException{
		List<File> subFiles = new ArrayList<>();
        
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(filesDir))
        {
            for (Path file : dirStream)
            {
                if (Files.isRegularFile(file)  &&  file.toString().endsWith(".txt"))
                {
                	subFiles.add(file.toFile());
                }
            }
        }
        return subFiles;
	}
}
