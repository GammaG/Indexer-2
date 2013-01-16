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
public class Scanner {

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
			File folderfile = new File(file.getName());
			folderfile.mkdir();
			scanFolder(file, folderfile + "/");

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
	public void saveIt(final ArrayList<File> list, String root) {
		final File folder = new File(root);
		final String[] txtname = root.split("[//]+");

		final Thread mkFolder = new Thread() {
			@Override
			public void run() {
				if (!folder.exists())
					folder.mkdir();
			}
		};
		mkFolder.run();
		Thread mktxt = new Thread() {
			@Override
			public void run() {
				try {

					mkFolder.join();

				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (list.isEmpty())
					return;

				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(new File(
							folder.getAbsolutePath() + "/"
									+ txtname[txtname.length - 1] + ".txt")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (File f : list) {
					try {
						writer.write(f.getName());
						writer.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		mktxt.run();

	}

}
