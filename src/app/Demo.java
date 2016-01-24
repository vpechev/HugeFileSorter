package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
			// deletes the files directory from the last run
			// Utility.cleanWorkingDirectory();
			
			File initialRandomFile = getInitialFile();

			long t0 = System.currentTimeMillis();
			FileCreator.CreateSortedSubFiles(initialRandomFile,
					Constants.SUB_FILE_SIZE);
			measureElapsedTime(t0, "Total time for creating sub files ");

			Path subFilesDir = Paths.get(Constants.MAIN_FILES_FOLDER_NAME + "/" + Constants.SUB_FILES_FOLDER_NAME);
			List<File> subfiles = Utility.GetSubFilesFromDirectory(subFilesDir);

			//merging the divided subfiles
			ForkJoinPool pool = new ForkJoinPool();
			try {
				long t1 = System.currentTimeMillis();
				pool.invoke(new FilesSorterAndMergerTask(subfiles, 0, subfiles
						.size()));
				measureElapsedTime(t1, "Time for merging the sub files ");
				measureElapsedTime(t0, "TOTAL TIME for merging of the file ");
			} finally {
				pool.shutdown();
			}

			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * measures the total time by passed start time
	 * @param start
	 * @param message that would be printed with the elapsed time
	 */
	private static void measureElapsedTime(long start, String message){
		long finish = System.currentTimeMillis();
		long timeMillis = finish - start;
		long timeSeconds = timeMillis / 1000;
		System.out.println(message + timeMillis + " ms. = " + timeSeconds + " sec.");
	}
	
	
	//just for purposes of the Demo
	private static File getInitialFile() throws IOException{
		File initialRandomFile;
		boolean useReadyFile = false;
		if(useReadyFile)
			initialRandomFile = new File("files/fileForSort.txt");
		else{
			//long fileForSortSize_10GB = 10_737_418_240L;
			long fileForSortSize_512MB = 524_288_000L, fileForSortSize_200MB= 209_715_200, fileForSortSize_1MB = 1_048_576;
			initialRandomFile = FileCreator.CreateRandomNumbersFile(fileForSortSize_1MB);
		}
		return initialRandomFile;
	}

}
