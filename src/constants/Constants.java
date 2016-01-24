package constants;

public class Constants {
	//	file size argument 		actual file size
	//	100_000_000				95 mb
	//	1_048_576				1 mb
	//	1_073_741_824			1 gb
	//	10_737_418_240			10 gb
	//  524_288_000				500 mb
	
	public final static long SUB_FILE_SIZE = 314_572_800;
	
	public final static long MAX_SUBFILE_COLLECTION_ELEMENTS_COUNT = SUB_FILE_SIZE / 4;
	
	public final static String FILE_DELIMETER = " ";
	
	public final static String MAIN_FILES_FOLDER_NAME = "files";
	
	public final static String SUB_FILES_FOLDER_NAME = "subfiles";
	
	public final static String FILE_TYPE = ".txt";
	
	public final static int MAX_INTEGER_NUMBER = 1_000_000; 
	
}	
