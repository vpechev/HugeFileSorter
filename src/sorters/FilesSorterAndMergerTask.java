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
	
	private static final long serialVersionUID = 3247978118036520529L;
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
		finally{
			//deletes the read subfiles
//			try {
//				Utility.deleteParticularFiles();
//			} catch (IOException e) {
//				throw new RuntimeException(e.getMessage());
//			}
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
		
		//it's commend, because the rename method does not work by the appropriate way.
//		if(!isFileARenamed || !isFileBRenamed)	throw new RuntimeException("Error in renaming file");
		
		FileReader fileReaderA = null;
		FileReader fileReaderB = null;
		
		try{
			fileReaderA = new FileReader(fileA); 
			fileReaderB = new FileReader(fileB);
		
			try( BufferedReader readerA = new BufferedReader(fileReaderA);
				 BufferedReader readerB = new BufferedReader(fileReaderB); 
				 PrintWriter writer = new PrintWriter(mergedFile)) 
			{
					
				String lineA = Utility.readNextLine(readerA);
				String lineB = Utility.readNextLine(readerB);
	
				int first, second;
				
				while(true){
					//this to if statements cannot be extracted to methods, because the sequence order is important
					//and that's while different while loops constructions are used in the else statements
					if(lineA != null){
						first = Utility.parseStringToInt(lineA);
					}
					else{
						if(lineB != null){
							do{
								Utility.WriteNumbersToStream(writer, Utility.parseStringToInt(lineB));
							}while((lineB = Utility.readNextLine(readerB)) != null);
						}
						
						return mergedFile;
					}
					
					if(lineB != null){
						second = Utility.parseStringToInt(lineB);
					}
					else{
						while((lineA = Utility.readNextLine(readerA)) != null){
							Utility.WriteNumbersToStream(writer, Utility.parseStringToInt(lineA));
						}
						return mergedFile;
					}
					
					
					if(first < second){
						Utility.WriteNumbersToStream(writer, first);
						
						lineA = Utility.readNextLine(readerA);
					}
					else if(first > second){
						Utility.WriteNumbersToStream(writer, second);
						
						lineB = Utility.readNextLine(readerB);
					}
					else{
						Utility.WriteNumbersToStream(writer, first, second);
						
						lineA = Utility.readNextLine(readerA);
						lineB = Utility.readNextLine(readerB);
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
			 fileA.renameTo(new File(Constants.MAIN_FILES_FOLDER_NAME + "/x_" + fileA.getName()));
			 fileB.renameTo(new File(Constants.MAIN_FILES_FOLDER_NAME + "/x_" + fileB.getName()));	
			 			
			 if(fileReaderA != null)
			  	fileReaderA.close();
			 
			 if(fileReaderB != null)
			  	fileReaderB.close();
		}
	}
	
}
