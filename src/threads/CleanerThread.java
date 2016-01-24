package threads;

import java.io.IOException;

import utilities.Utility;

public class CleanerThread extends Thread{
	@Override
	public void run() {
		while(true){
			try {
				Utility.deleteParticularFiles();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
