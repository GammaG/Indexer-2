package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author admin dataScanner
 */
public class Scanner {

	/**
	 * Main methode starts the Scanner and calls the scanFolder Methode
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {

		new Scanner().scanFolder();
		System.exit(0);

	}

	/**
	 * Open dialog to choose the folder which should be scanned, creates the
	 * root folder for index
	 */
	public void scanFolder() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = chooser.showOpenDialog(chooser);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			File folderfile = new File("Index of " + file.getName());
			folderfile.mkdir();
			scanFolder(file, folderfile + "/");

		} else {
			System.exit(0);
		}

	}

	/*
	 * Uses recusion to create folders in repository calls the saveit methode
	 */
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
	 * here the txt file and lower folder where created, uses Threads to make
	 * sure folder exits while putting txt in it
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
					dialog("Folder creation failed.");
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
					dialog("The \".txt\" couldn't be created.");
				}

				for (File f : list) {
					try {
						writer.write(f.getName());
						writer.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dialog("writing in txt failed");
					}

				}
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dialog("txt creation failed");
				}

			}

		};
		mktxt.run();

	}

	/**
	 * Shows errormessage
	 * 
	 * @param message
	 */
	private void dialog(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
				JOptionPane.ERROR_MESSAGE);
	}
}
