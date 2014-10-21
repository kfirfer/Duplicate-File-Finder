import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


public class DupeFinder {

	private LinkedList<Path> allFiles;

	
	private Collection<String> filesNameSet = new HashSet<>();
	private ArrayList<Path> dupeFilesByName = new ArrayList<>();


	private Collection<Long> filesSizeSet = new HashSet<>();
	private ArrayList<Path> dupeFilesBySize = new ArrayList<>();
	
	
	// Contractor
	public DupeFinder(LinkedList<Path> allFiles) {
		this.allFiles = allFiles;
		
	}
	

	public void theSize() {
		System.out.println("The size of all Files after clean: " + allFiles.size());
		System.out.println("The size of dupe size :" + dupeFilesBySize.size());
		System.out.println("The size of dupe name :" + dupeFilesByName.size());
	}


	public void dupeByName() {

		Iterator<Path> items = allFiles.iterator();
		Path file;
		
		while(items.hasNext()){
			file = items.next();
			
			if(filesNameSet.add(file.toFile().getName()) == false) {
				dupeFilesByName.add(file);
				items.remove();
			}			
		}
	}
	
	public void dupeBySize() {
		Iterator<Path> items = allFiles.iterator();
		Path file;
		
		while(items.hasNext()){
			file = items.next();
			
			if(filesSizeSet.add(file.toFile().length()) == false) {
				dupeFilesBySize.add(file);
				items.remove();
			}
			

		}
		
	}
	
	public void showDupeByName() {
		
		Iterator<Path> items = dupeFilesByName.iterator();
		Path file;
		while(items.hasNext()){
			file = items.next();

			System.out.println(file.toAbsolutePath());

		}
	}
	
	public void showDupeBySize() {
		Iterator<Path> items = dupeFilesBySize.iterator();
		Path file;
		while(items.hasNext()){
			file = items.next();

			System.out.println(file.toAbsolutePath());

		}
	}
	
}
