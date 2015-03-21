package app;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import updater.UpdateInfo;
import updater.Updater;

public class Main extends javax.swing.JFrame implements ItemListener {

	private static final long serialVersionUID = -6400106209264025973L;

	public static final double VERSION = 0.1;
	ArrayList<String> stringPaths = new ArrayList<>();
	DefaultListModel<File> listModel = new DefaultListModel<File>();
	int choice = 0;
	boolean cancelByUser = false;
	boolean scanning = false;
	Thread t1, t2;
	DupeStart startSearch;

	/**
	 * Creates new form NewApplication
	 */
	public Main() {
		initComponents();
		initChanges();
	}

	private void initChanges() {

		TableColumn tc = null;
		for (int i = 0; i < 4; i++) {
			tc = table.getColumnModel().getColumn(i);
			switch (i) {
			case 0:
				tc.setPreferredWidth(150);
				break;
			case 1:
				tc.setPreferredWidth(60);
				tc.setMaxWidth(80);
				break;
			case 2:
				tc.setPreferredWidth(620);
				break;
			case 3:
				tc.setPreferredWidth(50);
				tc.setMaxWidth(50);
				break;
			}

		}

		setLocationRelativeTo(null);

	}

	private void initComponents() {

		scanDialog = new javax.swing.JDialog();
		cancelButton = new javax.swing.JButton();
		progressBar = new javax.swing.JProgressBar();
		jFileChooser = new javax.swing.JFileChooser();
		aboutDialog = new javax.swing.JDialog();
		searchButton = new javax.swing.JButton();
		browseButton = new javax.swing.JButton();
		removeButton = new javax.swing.JButton();
		jScrollPane3 = new javax.swing.JScrollPane();
		table = new javax.swing.JTable();
		checkBoxPanel = new javax.swing.JPanel();
		boxByName = new javax.swing.JCheckBox();
		boxBySize = new javax.swing.JCheckBox();
		boxByHash = new javax.swing.JCheckBox();
		deleteButton = new javax.swing.JButton();
		checkAllButton = new javax.swing.JButton();
		uncheckAllButton = new javax.swing.JButton();
		checkOnlyOne = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		dirList = new javax.swing.JList();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenuItem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		updateCheck = new javax.swing.JMenuItem();
		aboutMenuItem = new javax.swing.JMenuItem();
		popupTable = new JPopupMenu();

		dupeFoundLabel = new JLabel("Dupe found: ");
		dupeFoundLabel.setSize(40, 20);

		scanFileLabel = new JLabel("Calculating number of files...");
		scanFileLabel.setLocation(50, 50);
		scanFileLabel.setSize(40, 20);

		progressBar.setLocation(150, 150);
		progressBar.setSize(50, 50);

		scanDialog.setSize(155, 132);
		scanDialog.setLayout(new FlowLayout());

		scanDialog.add(scanFileLabel);
		scanDialog.add(dupeFoundLabel);
		scanDialog.add(progressBar);
		scanDialog.add(cancelButton);

		scanDialog.setTitle("Scaning");
		scanDialog.setResizable(false);
		scanDialog.setLocationRelativeTo(null);
		scanDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				cancelByUser = true;
				scanning = false;
				if (t1.isAlive()) {
					t1.stop();
				}
				EnableDisable();

			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		jFileChooser.setCurrentDirectory(new java.io.File("C:\\"));
		jFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jFileChooserActionPerformed(evt);
			}
		});

		progressBar.setIndeterminate(true);

		aboutDialog.setTitle("About");
		aboutDialog.setBounds(new java.awt.Rectangle(0, 0, 300, 400));
		aboutDialog.setLocationRelativeTo(null);
		aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		aboutDialog.setResizable(false);

		javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
		aboutDialog.getContentPane().setLayout(aboutDialogLayout);
		aboutDialogLayout.setHorizontalGroup(aboutDialogLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 387, Short.MAX_VALUE));
		aboutDialogLayout.setVerticalGroup(aboutDialogLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 327, Short.MAX_VALUE));

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Dupe File Cleaner version " + VERSION);
		setResizable(false);

		searchButton.setText("Start search");
		searchButton.setEnabled(false);
		searchButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchButtonActionPerformed(evt);
			}
		});

		browseButton.setText("Browse");
		browseButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				browseButtonActionPerformed(evt);
			}
		});

		removeButton.setText("Remove");
		removeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeButtonActionPerformed(evt);
			}
		});

		table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Name", "Size",
				"Location", "Delete" }

		) {

			public void addRow(Vector rowData) {
				super.addRow(rowData);
			}

			Class[] types = new Class[] { java.lang.String.class, java.lang.Object.class, java.lang.Object.class,
					java.lang.Boolean.class };
			boolean[] canEdit = new boolean[] { false, false, false, true };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}

		});

		JMenuItem menuItem = new JMenuItem("Open File");
		menuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuItemhButtonActionPerformed(evt);
			}

		});

		JMenuItem menuItem2 = new JMenuItem("Open Folder");
		menuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuItem2hButtonActionPerformed(evt);
			}

		});

		popupTable.add(menuItem2);
		popupTable.add(menuItem);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.out.println("mouse pressed");
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {

					table.changeSelection(table.getSelectedRow(), 2, false, false);
					JTable source = (JTable) e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());

					if (!source.isRowSelected(row))
						source.changeSelection(row, column, false, false);

					popupTable.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setResizingAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);
		jScrollPane3.setViewportView(table);

		checkBoxPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		boxByName.setText("Search By Name");
		boxByName.addItemListener(this);

		boxBySize.setText("Search By Size");
		boxBySize.addItemListener(this);

		boxByHash.setText("Search By Hash");
		boxByHash.addItemListener(this);

		javax.swing.GroupLayout checkBoxPanelLayout = new javax.swing.GroupLayout(checkBoxPanel);
		checkBoxPanel.setLayout(checkBoxPanelLayout);
		checkBoxPanelLayout.setHorizontalGroup(checkBoxPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				checkBoxPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								checkBoxPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(boxByName, javax.swing.GroupLayout.DEFAULT_SIZE, 144,
												Short.MAX_VALUE)
										.addComponent(boxBySize, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(boxByHash, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		checkBoxPanelLayout.setVerticalGroup(checkBoxPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				checkBoxPanelLayout.createSequentialGroup().addContainerGap().addComponent(boxByName)
						.addGap(18, 18, 18).addComponent(boxBySize).addGap(18, 18, 18).addComponent(boxByHash)
						.addContainerGap(15, Short.MAX_VALUE)));

		deleteButton.setText("Delete check");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteButtonActionPerformed(e);

			}
		});

		checkAllButton.setText("Check all");
		checkAllButton.setEnabled(false);
		checkAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkAllButtonActionPerformed(e);
			}
		});

		uncheckAllButton.setText("Uncheck all");
		uncheckAllButton.setEnabled(false);
		uncheckAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				uncheckAllButtonActionPerformed(evt);
			}
		});

		checkOnlyOne.setText("Check only uniqe");
		checkOnlyOne.setEnabled(false);
		checkOnlyOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkOnlyOneActionPerformed(e);
			}
		});

		dirList.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
		dirList.setModel(listModel);
		dirList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				dirListValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(dirList);

		fileMenu.setMnemonic('f');
		fileMenu.setText("File");

		openMenuItem.setMnemonic('o');
		openMenuItem.setText("Open");
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openMenuItem);

		saveMenuItem.setMnemonic('s');
		saveMenuItem.setText("Save");
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveMenuItem);

		exitMenuItem.setMnemonic('x');
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		helpMenu.setMnemonic('h');
		helpMenu.setText("Help");

		updateCheck.setMnemonic('c');
		updateCheck.setText("Check for Update");
		updateCheck.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateCheckActionPerformed(evt);
			}
		});
		helpMenu.add(updateCheck);

		aboutMenuItem.setMnemonic('a');
		aboutMenuItem.setText("About");
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(38, 38, 38)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														checkBoxPanel,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														javax.swing.GroupLayout.PREFERRED_SIZE)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(
																																		deleteButton,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		113,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																.addComponent(
																																		checkAllButton,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		113,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																.addComponent(
																																		uncheckAllButton,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		113,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																.addComponent(
																																		checkOnlyOne)))
																								.addGap(27, 143,
																										Short.MAX_VALUE)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														removeButton,
																														javax.swing.GroupLayout.Alignment.TRAILING,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														79,
																														javax.swing.GroupLayout.PREFERRED_SIZE)
																												.addComponent(
																														searchButton,
																														javax.swing.GroupLayout.Alignment.TRAILING,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														133,
																														javax.swing.GroupLayout.PREFERRED_SIZE)))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addContainerGap(
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addComponent(
																										browseButton,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										79,
																										javax.swing.GroupLayout.PREFERRED_SIZE)))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(jScrollPane1,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 193,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(
														javax.swing.GroupLayout.Alignment.LEADING,
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(jScrollPane3,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 965,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(37, 37, 37)
																								.addComponent(
																										checkBoxPanel,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										browseButton)
																								.addGap(18, 18, 18)
																								.addComponent(
																										removeButton)))
																.addGap(70, 70, 70)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(uncheckAllButton)
																				.addComponent(deleteButton)
																				.addComponent(checkAllButton)
																				.addComponent(checkOnlyOne)
																				.addComponent(
																						searchButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						39,
																						javax.swing.GroupLayout.PREFERRED_SIZE)))
												.addComponent(jScrollPane1)).addGap(18, 18, 18)
								.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
								.addContainerGap()));

		pack();

	}

	private void deleteButtonActionPerformed(ActionEvent e) {
		errorHandles(5);

	}

	private void checkAllButtonActionPerformed(ActionEvent e) {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (table.getValueAt(i, 3) != null)
				table.setValueAt(true, i, 3);
		}

	}

	private void uncheckAllButtonActionPerformed(ActionEvent evt) {
		// TODO:
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			table.setValueAt(false, i, 3);
		}

	}

	private void checkOnlyOneActionPerformed(ActionEvent e) {
		boolean flag = true;
		int size = tableModel.getRowCount();
		for (int i = 0; i < size - 1; i++) {
			if (table.getValueAt(i, 2) == null) {
				table.setValueAt(false, i, 3);
			}
			table.setValueAt(false, i, 3);
			i++;
			if (table.getValueAt(i, 2) == null) {
				table.setValueAt(false, i, 3);
			}
			while (table.getValueAt(i, 2) != null) {

				table.setValueAt(true, i, 3);
				i++;

				if (i > size - 1)
					break;
				if (table.getValueAt(i, 2) == null) {
					table.setValueAt(false, i, 3);
				}
			}

		}

	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {

		if (stringPaths.isEmpty()) {
			errorHandles(1);
		} else if (choice == 0) {
			errorHandles(2);
		} else {

			scanDialog.setVisible(true);
			tableModel = (DefaultTableModel) table.getModel();

			int rowCount = tableModel.getRowCount();

			for (int i = rowCount - 1; i >= 0; i--) {
				tableModel.removeRow(i);
			}

			startSearch = new DupeStart(stringPaths, choice);
			t1 = new Thread(startSearch);
			t1.setPriority(8);
			t1.start();
			scanning = true;
			EnableDisable();

			t2 = new Thread(new DupeTraverse());
			t2.setPriority(2);
			t2.start();

		}

	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {

		cancelByUser = true;
		scanning = false;
		if (t1.isAlive()) {
			t1.stop();
		}
		EnableDisable();
		scanDialog.setVisible(false);
	}

	private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {

		String path;

		if (jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
			path = jFileChooser.getSelectedFile().getAbsolutePath();
			path = path.replace('\\', '/');
			path = path.replace(":/", "://");

			if (!listModel.contains(jFileChooser.getSelectedFile().getAbsoluteFile())) {
				listModel.addElement(jFileChooser.getSelectedFile().getAbsoluteFile());
				stringPaths.add(path);
			}

		} else {
			System.out.println("No Selection ");
		}
		enableDisable();
	}

	private void jFileChooserActionPerformed(java.awt.event.ActionEvent evt) {

	}

	private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {

		int[] toindex = dirList.getSelectedIndices();
		for (int i = (toindex.length - 1); i >= 0; i--) {
			listModel.remove(toindex[i]);
			stringPaths.remove(toindex[i]);
		}
		enableDisable();

	}

	private void menuItemhButtonActionPerformed(java.awt.event.ActionEvent evt) {

		Component c = (Component) evt.getSource();
		JPopupMenu popup = (JPopupMenu) c.getParent();
		JTable table = (JTable) popup.getInvoker();

		table.changeSelection(table.getSelectedRow(), 2, false, false);
		Object path = table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn());
		if (path != null && table.getSelectedColumn() == 2) {

			String strPath = path.toString();
			strPath = strPath.replace('\\', '/');
			strPath = strPath.replace(":/", "://");
			Desktop desktop = Desktop.getDesktop();

			File dirToOpen = null;

			try {
				dirToOpen = new File(strPath);
				desktop.open(dirToOpen);
			} catch (IllegalArgumentException e) {
				errorHandles(3);
				System.out.println("File Not Found");
			} catch (IOException e) {
				errorHandles(4);
				System.out.println("File not associated with the operating system");
			}

		}

	}

	private void menuItem2hButtonActionPerformed(ActionEvent evt) {
		Component c = (Component) evt.getSource();
		JPopupMenu popup = (JPopupMenu) c.getParent();
		JTable table = (JTable) popup.getInvoker();

		table.changeSelection(table.getSelectedRow(), 2, false, false);
		Object path = table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn());
		if (path != null && table.getSelectedColumn() == 2) {

			String strPath = path.toString();
			strPath = strPath.replace('\\', '/');
			strPath = strPath.replace(":/", "://");
			Desktop desktop = Desktop.getDesktop();

			File dirToOpen = null;

			try {
				dirToOpen = new File(strPath);
				dirToOpen = dirToOpen.getParentFile();
				desktop.open(dirToOpen);
			} catch (IllegalArgumentException | IOException e) {
				System.out.println("File Not Found");
			}

		}

	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {

			if (e.getItemSelectable() == boxByName) {
				choice -= Math.pow(1, 4);
			} else if (e.getItemSelectable() == boxBySize) {
				choice -= Math.pow(2, 4);
			} else if (e.getItemSelectable() == boxByHash) {
				choice -= Math.pow(3, 4);
			}
		} else {
			if (e.getItemSelectable() == boxByName) {
				choice += Math.pow(1, 4);
			} else if (e.getItemSelectable() == boxBySize) {
				choice += Math.pow(2, 4);
			} else if (e.getItemSelectable() == boxByHash) {
				choice += Math.pow(3, 4);
			}
		}
		enableDisable();
	}

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

		aboutDialog.setVisible(true);
	}

	private void updateCheckActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			if (Double.parseDouble(Updater.getLatestVersion()) > this.VERSION) {

				File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				new UpdateInfo(Updater.getWhatsNew());

			} else {
				errorHandles(6);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errorHandles(7);
		}

	}

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		String path;

		if (jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
			path = jFileChooser.getSelectedFile().getAbsolutePath();
			path = path.replace('\\', '/');
			path = path.replace(":/", "://");

			if (!listModel.contains(jFileChooser.getSelectedFile().getAbsoluteFile())) {
				listModel.addElement(jFileChooser.getSelectedFile().getAbsoluteFile());
			}
			stringPaths.add(path);
		} else {
			System.out.println("No Selection ");
		}

	}

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		errorHandles(5);
	}

	private void dirListValueChanged(javax.swing.event.ListSelectionEvent evt) {

	}

	public void EnableDisable() {
		if (scanning) {
			searchButton.setEnabled(false);
			browseButton.setEnabled(false);
			checkAllButton.setEnabled(false);
			checkOnlyOne.setEnabled(false);
			menuBar.setEnabled(false);
			deleteButton.setEnabled(false);
			uncheckAllButton.setEnabled(false);
			removeButton.setEnabled(false);
			boxByName.setEnabled(false);
			boxBySize.setEnabled(false);
			boxByHash.setEnabled(false);

		} else {
			searchButton.setEnabled(true);
			browseButton.setEnabled(true);
			checkAllButton.setEnabled(true);
			checkOnlyOne.setEnabled(true);
			menuBar.setEnabled(true);
			deleteButton.setEnabled(true);
			uncheckAllButton.setEnabled(true);
			removeButton.setEnabled(true);
			boxByName.setEnabled(true);
			boxBySize.setEnabled(true);
			boxByHash.setEnabled(true);
		}
	}

	private void enableDisable() {
		if (choice > 0 && !stringPaths.isEmpty() && !scanning) {
			searchButton.setEnabled(true);
		} else {
			searchButton.setEnabled(false);
		}

	}

	public void errorHandles(int errorType) {
		JOptionPane optionPane = null;
		JDialog dialog = null;

		switch (errorType) {
		case 1:
			optionPane = new JOptionPane("No selected folder", JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION,
					null, null, "");
			dialog = optionPane.createDialog(null, "Error");
			break;
		case 2:
			optionPane = new JOptionPane("No selected Box", JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION, null,
					null, "");
			dialog = optionPane.createDialog(null, "Error");

			break;
		case 3:
			optionPane = new JOptionPane("File Not Found", JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION, null,
					null, "");
			dialog = optionPane.createDialog(null, "Error");

			break;
		case 4:
			optionPane = new JOptionPane("File not associated with the operating system", JOptionPane.ERROR_MESSAGE,
					JOptionPane.CLOSED_OPTION, null, null, "");
			dialog = optionPane.createDialog(null, "Error");

			break;
		case 5:
			optionPane = new JOptionPane("This Button is under Construction", JOptionPane.ERROR_MESSAGE,
					JOptionPane.CLOSED_OPTION, null, null, "");
			dialog = optionPane.createDialog(null, "Error");
			break;
		case 6:
			optionPane = new JOptionPane("You run the latest version", JOptionPane.INFORMATION_MESSAGE,
					JOptionPane.CLOSED_OPTION, null, null, "");
			dialog = optionPane.createDialog(null, "Update");
			break;
		case 7:
			optionPane = new JOptionPane("Unable login to server", JOptionPane.ERROR_MESSAGE,
					JOptionPane.CLOSED_OPTION, null, null, "");
			dialog = optionPane.createDialog(null, "Login error");
			break;
		}
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Main().setVisible(true);
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (Double.parseDouble(Updater.getLatestVersion()) > VERSION) {

				File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				new UpdateInfo(Updater.getWhatsNew());

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class DupeTraverse extends Thread {

		public void run() {

			boolean flag2 = false;
			int sizeFiles = 0;
			int sizeDupe = 0;
			int prevSize = 0;

			dupeFoundLabel.setText("Dupe found: ");
			while (t1.isAlive()) {

				try {
					prevSize = startSearch.filesArray.size();
				} catch (Exception e1) {
					System.out.println("error1");
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("Error thread t1 sleep");
					e.printStackTrace();
				}
				try {
					sizeFiles = startSearch.filesArray.size();
					scanFileLabel.setText("Total files: " + sizeFiles);
				} catch (Exception e) {
					System.out.println("error2");
				}

				try {
					sizeDupe = startSearch.finder.counter;
					dupeFoundLabel.setText("Dupe found: " + sizeDupe);
				} catch (Exception e) {
					System.out.println("error3");
				}

			}

			deleteButton.setEnabled(true);
			checkAllButton.setEnabled(true);
			checkOnlyOne.setEnabled(true);
			uncheckAllButton.setEnabled(true);
			Set<String> keys = startSearch.finder.multiMap.keySet();
			Iterator<String> items = keys.iterator();

			boolean flag;
			while (items.hasNext()) {
				flag = false;
				String str = (String) items.next();

				Collection<Path> val = (Collection<Path>) startSearch.finder.multiMap.get(str);
				ArrayList<Path> arr = new ArrayList<>(val);

				if (arr.size() > 1) {
					Iterator<Path> items2 = arr.iterator();
					while (items2.hasNext()) {
						Path file = items2.next();
						long fileSize = file.toFile().length();
						String[] sizeTypes = { "KB", "MB", "GB" };
						String fileSizeStr = fileSize + " Bytes";
						for (int i = 0; fileSize > 1000; i++) {
							fileSize /= 1000;
							fileSizeStr = fileSize + " " + sizeTypes[i];
						}

						Vector newRow = new Vector();
						newRow.add(file.toFile().getName());
						newRow.add(fileSizeStr);
						newRow.add(file.toAbsolutePath());
						newRow.add(flag);
						tableModel.addRow(newRow);
						flag = true;
					}
					Vector newRow = new Vector();
					newRow.add(null);
					newRow.add(null);
					newRow.add(null);
					newRow.add(null);
					tableModel.addRow(newRow);
				} else {
					items.remove();
				}

			}

			// }
			int countTableRows = tableModel.getRowCount();
			if (countTableRows > 0) {
				tableModel.removeRow(countTableRows - 1);
			}
			startSearch.finder.multiMap.clear();
			startSearch.finder.multiMap = null;
			startSearch = null;
			System.gc();
			scanning = false;
			EnableDisable();
			scanDialog.setVisible(false);
		}
	}

	private javax.swing.JDialog aboutDialog;
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JCheckBox boxByHash;
	private javax.swing.JCheckBox boxByName;
	private javax.swing.JCheckBox boxBySize;
	private javax.swing.JButton browseButton;
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton checkAllButton;
	private javax.swing.JPanel checkBoxPanel;
	private javax.swing.JButton checkOnlyOne;
	private javax.swing.JButton deleteButton;
	private javax.swing.JList dirList;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JFileChooser jFileChooser;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JProgressBar progressBar;
	private javax.swing.JButton removeButton;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JDialog scanDialog;
	private javax.swing.JButton searchButton;
	private javax.swing.JTable table;
	private javax.swing.JButton uncheckAllButton;
	private javax.swing.JMenuItem updateCheck;

	private DefaultTableModel tableModel;
	private JPopupMenu popupTable;
	private JLabel scanFileLabel;
	private JLabel dupeFoundLabel;
}
