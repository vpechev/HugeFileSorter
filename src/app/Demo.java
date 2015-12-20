package app;

import java.io.IOException;

import creations.FileCreator;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileCreator.CreateRandomNumbersFile(1_048_576);
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
