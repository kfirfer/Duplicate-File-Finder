package app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

public class DupeFinder {

	HashSet<Path> filesArray; // Storing ALL the files (before dupe search)
	MultiMap<String, Path> multiMap = null;
	int i;
	int counter = 0;

	public DupeFinder(HashSet<Path> filesArray) {
		this.filesArray = filesArray;
		this.multiMap = new MultiValueMap<String, Path>();
	}

	// Dupe finder starts here
	public void findDupe(int choice) {

		String fullName = null;
		boolean flag = true;

		for (Path file : filesArray) {
			try {
				fullName = getFullName(file, choice);
			} catch (Exception e) {
				System.out.println("problem read file: " + file);
				flag = false;
			}

			if (flag) {
				multiMap.put(fullName, file);
				Collection<Path> vals = (Collection<Path>) multiMap.get(fullName);

				if (vals.size() > 1) {
					counter++;
				}
			}
			flag = true;
		}

		Set<String> keys = multiMap.keySet();
		Iterator<String> items = keys.iterator();

		while (items.hasNext()) {
			String str = (String) items.next();

			@SuppressWarnings("unchecked")
			Collection<Path> val = (Collection<Path>) multiMap.get(str);
			ArrayList<Path> arr = new ArrayList<>(val);

			if (arr.size() <= 1)
				items.remove();

		}

	}

	// this method return String represent the file
	private String getFullName(Path file, int choice) throws Exception {
		String fullName = "";

		switch (choice) {
		case 1:
			fullName = getFileName(file); // name
			break;
		case 16:
			fullName = getFileSize(file); // size
			break;
		case 81:
			fullName = getFileHash(file); // hash
			break;
		case 17:
			fullName = getFileName(file) + getFileSize(file); // name+size
			break;
		case 82:
			fullName = getFileName(file) + getFileHash(file); // name+hash
			break;
		case 97:
			fullName = getFileSize(file) + getFileHash(file);
			break;
		case 98:
			fullName = getFileName(file) + getFileSize(file) + getFileHash(file);
			break;
		}
		return fullName;
	}

	public String getFileName(Path file) {
		return file.toFile().getName();
	}

	public String getFileSize(Path file) {
		return file.toFile().length() + "";
	}

	public String getFileHash(Path file) throws Exception {

		int num, ctr;
		InputStream fis = new FileInputStream(file.toString());
		byte[] buf = new byte[1024];

		ctr = 256;
		MessageDigest filecnt = MessageDigest.getInstance("MD5");
		num = fis.read(buf);
		while (num != -1) {
			if (num > 0)
				filecnt.update(buf, 0, num);
			num = fis.read(buf);
			ctr--;
			if (ctr == 0)
				break;
		}
		fis.close();

		String MD5sum = DatatypeConverter.printHexBinary(filecnt.digest());
		if (ctr == 0) {
			MD5sum += "*";
		}

		return MD5sum;
	}

	public void printFiles() {

		Set<String> keys = multiMap.keySet();
		Iterator<String> items = keys.iterator();

		i = 1;

		while (items.hasNext()) {
			String str = (String) items.next();

			@SuppressWarnings("unchecked")
			Collection<Path> val = (Collection<Path>) multiMap.get(str);
			ArrayList<Path> arr = new ArrayList<>(val);

			for (Path strstr : arr) {
				System.out.println(i++ + "  " + strstr);
			}
			System.out.println();

		}
		i--;
	}

	public synchronized MultiMap<String, Path> getMap() {
		return multiMap;
	}

}
