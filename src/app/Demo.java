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
			//Demo.cleanWorkingDirectory();
						
			//File initialRandomFile = FileCreator.CreateRandomNumbersFile(1_073_741_824);
			
			File initialRandomFile = new File("files/fileForSort.txt");
			
			FileCreator.CreateSortedSubFiles(initialRandomFile, Constants.SUB_FILE_SIZE);
			
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

	private static void cleanWorkingDirectory() throws IOException{
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
	
}
