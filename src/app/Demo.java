package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import sorters.FilesSorterAndMergerTask;
import utilities.Utility;
import constants.Constants;
import creations.FileCreator;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File initialRandomFile = FileCreator.CreateRandomNumbersFile(1_048_576);
						
			FileCreator.CreateSortedSubFiles(initialRandomFile, Constants.SUB_FILE_SIZE);
			
			Path subFilesDir = Paths.get("files/subfiles");
			List<File> subfiles = Utility.GetSubFilesFromDirectory(subFilesDir);
			
			ForkJoinPool pool = new ForkJoinPool();
			try{
				pool.invoke(new FilesSorterAndMergerTask(subfiles, 0, subfiles.size()));
			}
			finally{
				pool.shutdown();
			}
			
			
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
