import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
	static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {
		showMenu();

		
		
		
		
		Path dir = Paths.get("C://Programs");
		FileTree tree = new FileTree();
		
		try {
			Files.walkFileTree(dir, tree);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int choice = getChoice();
		long time = System.currentTimeMillis();
		switch(choice) {
		case 1:
			tree.finder.dupeByName();
			break;
		case 2:
			tree.finder.dupeBySize();
			break;
		case 3:
			tree.finder.dupeByName();
			tree.finder.dupeBySize();
			break;
			default:
				System.out.println("Not valid input");
				System.exit(0);
		}
		
		
		
	
		System.out.println("Time of excecute:" + (System.currentTimeMillis() - time));
		
		System.out.println("The size of all files: "+tree.counter);
		tree.finder.theSize();

	}

	private static void showMenu() {
		System.out.println("What you want to do ?\n");
		System.out.println("1. Scan by name");
		System.out.println("2. Scan by size");
		System.out.println("3. Scan by name&size");
		
	}
	
	private static int getChoice() {
		int choice = 0;
		
		
		while(!scan.hasNextInt()) {
			System.out.println("int please!");
			scan.nextLine();
		}
		choice = scan.nextInt();
		
		return choice;
	}
}
