package utilities;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	
	public static List<Integer> sortList(List<Integer> list){
		Collections.sort(list);
		return list;	
	}
	
	public static boolean checkStringForNonCharacters(String input){
		Pattern p = Pattern.compile("[^0-9.]");
	    Matcher m = p.matcher(input);
	    boolean b = m.matches();
	    return b;
	}
}
