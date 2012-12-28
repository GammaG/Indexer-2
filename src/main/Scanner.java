package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

/**
 * @author admin dataScanner
 */
public class Scanner implements Runnable {

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {

		new Scanner().scanFolder();
		System.exit(0);

	}

	/**
	 * Holt alle dateinnamen aus einem Ordner
	 */
	public void scanFolder() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = chooser.showOpenDialog(chooser);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			file.mkdir();
			scanFolder(file, file.getName() + "/");

		} else {
			System.exit(0);
		}

	}

	private void scanFolder(File file, String root) {

		ArrayList<File> datalist = new ArrayList<File>();
		ArrayList<File> folderlist = new ArrayList<File>();

		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				folderlist.add(f);
			} else {
				datalist.add(f);
			}
		}

		for (File f : folderlist) {
			String rootold = root;
			root += f.getName();
			saveIt(datalist, root);
			scanFolder(f, root + "/");
			root = rootold;
		}
		saveIt(datalist, root);

	}

	/**
	 * Speichert den Index in einer txt;
	 */
	public void saveIt(ArrayList<File> list, String root) {
		File folder = new File(root);
		String[] txtname = root.split("[//]+");
		BufferedWriter writer = null;

		try {
			Thread mkfolder = new Thread(this);

			if (!folder.exists())
				folder.mkdir();

			if (list.isEmpty())
				return;

			writer = new BufferedWriter(new FileWriter(new File(
					folder.getAbsolutePath() + "/"
							+ txtname[txtname.length - 1] + ".txt")));

			for (File f : list) {
				writer.write(f.getName());
				writer.newLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {

				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
