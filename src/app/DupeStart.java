package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;


public class DupeStart implements Runnable {

	ArrayList<String> stringPath;
	int choice;
	HashSet<Path> filesArray = new HashSet<>();
	DupeFinder finder;
	
	
	public DupeStart(ArrayList<String> stringPath,int choice) {
		finder = new DupeFinder(filesArray);
		this.stringPath = stringPath;
		this.choice = choice;
		
	}
	
	
	
	public void run() {
		for(int i=0;i<stringPath.size();i++) {
			Path dir = Paths.get(stringPath.get(i));
			FileTree tree = new FileTree(filesArray);
			try {
				Files.walkFileTree(dir, tree);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		finder.findDupe(this.choice);
		System.gc();
		//finder.printFiles();
	}
	
	
}
