package sorters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import utilities.Utility;
import constants.Constants;

public class FilesSorterAndMergerTask extends RecursiveTask<File> {
	
	private int begin;
	private int end;
	private List<File> files;
	private static long SubForkFilesCount = 0;

	public FilesSorterAndMergerTask(List<File> files, int begin, int end) {
		this.begin = begin;
		this.end = end;
		this.files = files;
	}

	@Override
	protected File compute() {
		int count = this.end - this.begin;
		if (count == 1) {
			return this.files.get(this.begin);
		}

		int middle = this.begin + (this.end - this.begin) / 2;
		
		FilesSorterAndMergerTask leftTask = new FilesSorterAndMergerTask(this.files, this.begin, middle);
		FilesSorterAndMergerTask rightTask = new FilesSorterAndMergerTask(this.files, middle, this.end);

		invokeAll(leftTask, rightTask);

		try {
			return mergeFiles(leftTask.join(), rightTask.join());
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	private File mergeFiles(File fileA, File fileB) throws FileNotFoundException, IOException{
		System.out.println("Merging files: " + fileA.getName() + " and  " + fileB.getName());
		
		String newSubFileName = "/subForkFile_" + (SubForkFilesCount++);
		File mergedFile = new File(   Constants.MAIN_FILES_FOLDER_NAME + "/" 
									+ Constants.SUB_FILES_FOLDER_NAME 
									+ newSubFileName
									+ Constants.FILE_TYPE);
		
		Utility.checkFile(mergedFile);
		
		FileReader fileReaderA = null;
		FileReader fileReaderB = null;
		
//		if(!isFileARenamed || !isFileBRenamed)
//			throw new RuntimeException("Error in renaming file");
			
		try
		{
			fileReaderA = new FileReader(fileA); 
			fileReaderB = new FileReader(fileB);
			
			try( BufferedReader readerA = new BufferedReader(fileReaderA);
				 BufferedReader readerB = new BufferedReader(fileReaderB); 
				 PrintWriter writer = new PrintWriter(mergedFile)) 
			{
					
				String lineA = readerA.readLine();
				String lineB = readerB.readLine();
	
				int first;
				int second;
				
				while(true){
					if(lineA != null){
						first = Integer.valueOf(lineA);
					}
					else{
						if(lineB != null){
							do{
								writer.println(Integer.valueOf(lineB));
							}while((lineB = readerB.readLine()) != null);
						}
									
						return mergedFile;
					}
					
					if(lineB != null){
						second = Integer.valueOf(lineB);
					}
					else{
						while((lineA = readerA.readLine()) != null){
							writer.println(Integer.valueOf(lineA));
						}
						return mergedFile;
					}
					
					
					if(first < second){
						writer.println(first);
						
						lineA = readerA.readLine();
					}
					else if(first > second){
						writer.println(second);
						
						lineB = readerB.readLine();
					}
					else{
						writer.println(first);
						writer.println(second);
						
						lineA = readerA.readLine();
						lineB = readerB.readLine();
					}	
	
				}
			}
		}
		catch (FileNotFoundException e) 
        {
			e.printStackTrace();
			throw new IllegalArgumentException("Passed files do not exist");
		}
		finally{	
			
			/*boolean isFileARenamed = */ fileA.renameTo(new File(Constants.MAIN_FILES_FOLDER_NAME + "/x_" + fileA.getName()));
			/*boolean isFileBRenamed = */ fileB.renameTo(new File(Constants.MAIN_FILES_FOLDER_NAME + "/x_" + fileB.getName()));	
			
			if(fileReaderA != null)
				fileReaderA.close();
			
			if(fileReaderB != null)
				fileReaderB.close();
		}
	}
}
