package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
			//deletes the files directory from the last run
			//Utility.cleanWorkingDirectory();
						
			//File initialRandomFile = FileCreator.CreateRandomNumbersFile(1_073_741_824);
			
			File initialRandomFile = new File("files/fileForSort.txt");
			
			long t0 = System.currentTimeMillis();
			FileCreator.CreateSortedSubFiles(initialRandomFile, Constants.SUB_FILE_SIZE);
			long t01 = System.currentTimeMillis();
			System.out.println("Total time for creating sub files " + (t01 - t0));
			
			
			Path subFilesDir = Paths.get(Constants.MAIN_FILES_FOLDER_NAME + "/" + Constants.SUB_FILES_FOLDER_NAME);
			List<File> subfiles = Utility.GetSubFilesFromDirectory(subFilesDir);
			
			
			ForkJoinPool pool = new ForkJoinPool();
			try{
				long t1 = System.currentTimeMillis();
				pool.invoke(new FilesSorterAndMergerTask(subfiles, 0, subfiles.size()));
				long t2 = System.currentTimeMillis();
				System.out.println("Time elapsed: " + (t2-t1) + " ms.");
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
