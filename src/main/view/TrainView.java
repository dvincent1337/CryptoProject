package main.view;
//Author: David Vincent

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

import main.model.CryptoNet;
import main.model.CypherType;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.TextArea;

public class TrainView extends JFrame {

	private JPanel contentPane;
	private JLabel outputLbl;
	private JLabel polyLbl;
	private JLabel plainLbl;
	
	private File plainTextFile;
	private File polyKeywordFile;
	private File outputFile;
	private TextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainView frame = new TrainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TrainView() {
		plainTextFile = null;
		polyKeywordFile = null;
		outputFile = null;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 506, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Plain Text File:");
		lblNewLabel.setBounds(144, 37, 109, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				plainTextFileBtnPress();
			}
		});
		btnBrowse.setBounds(31, 33, 89, 23);
		contentPane.add(btnBrowse);
		
		plainLbl = new JLabel("[None Selected]");
		plainLbl.setBounds(274, 37, 109, 14);
		contentPane.add(plainLbl);
		
		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				polyKeywordFileBtnPress();
			}
		});
		btnBrowse_1.setBounds(31, 67, 89, 23);
		contentPane.add(btnBrowse_1);
		
		JLabel lblNewLabel_1 = new JLabel("Poly Keyword File:");
		lblNewLabel_1.setBounds(144, 71, 109, 14);
		contentPane.add(lblNewLabel_1);
		
		polyLbl = new JLabel("[None Selected]");
		polyLbl.setBounds(274, 71, 109, 14);
		contentPane.add(polyLbl);
		
		JButton btnBrowse_2 = new JButton("Browse");
		btnBrowse_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileBtnPress();
			}
		});
		btnBrowse_2.setBounds(31, 101, 89, 23);
		contentPane.add(btnBrowse_2);
		
		JLabel lblOutputFile = new JLabel("Output File:");
		lblOutputFile.setBounds(144, 105, 109, 14);
		contentPane.add(lblOutputFile);
		
		outputLbl = new JLabel("[None Selected]");
		outputLbl.setBounds(274, 105, 109, 14);
		contentPane.add(outputLbl);
		
		JButton btnFilter = new JButton("Filter");
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterBtnPress();
			}
		});
		btnFilter.setBounds(195, 140, 89, 23);
		contentPane.add(btnFilter);
		
		textArea = new TextArea();
		textArea.setBounds(53, 175, 380, 160);
		contentPane.add(textArea);
	}
	
	private void plainTextFileBtnPress()
	{
		//Get the input file
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(TrainView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            plainTextFile = fc.getSelectedFile();
            plainLbl.setText(plainTextFile.getName());
        }
	}
	private void polyKeywordFileBtnPress()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(TrainView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            polyKeywordFile = fc.getSelectedFile();
            polyLbl.setText(polyKeywordFile.getName());
        }
	}
	private void outputFileBtnPress()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(TrainView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            outputFile = fc.getSelectedFile();
            outputLbl.setText(outputFile.getName());
        }
        
        //This is where the work begins

        BufferedReader br1,br2;
        String line1,line2,fLine;
        FileWriter fw;
        char tempChar;
        
        //fw.write("add a line\n");

		try 
		{
			br1 = new BufferedReader(new FileReader(plainTextFile));
			fw = new FileWriter(outputFile,true); 
			int count = 0;
			while ((line1 = br1.readLine()) != null)
			{
				//for each line of plain text do the following
				// Encipher the data using 
				// all 26 mono shifts
				// all poly keywords
				// write the line to the output file.
				count++;
				System.out.println("Processing line: " + count);
				
				fLine = "";
				//filter the line.
				for (int i = 0; i < line1.length();i++)
				{
					tempChar = line1.toUpperCase().charAt(i);
					if( ('A' <= tempChar) && (tempChar <= 'Z'))
						fLine+=tempChar;
				}
				
				//Plain text
				fw.write(fLine + ":0\n");
				
				//Mono shifts
				for (int i =1; i<26; i++)
				{
					fw.write(CryptoNet.monoShift(fLine, i)+":1\n");				
				}
				
				//Poly codes
				br2 = new BufferedReader(new FileReader(polyKeywordFile));
				while ((line2 = br2.readLine()) != null)
				{
					fw.write(CryptoNet.polyEncrypt(fLine,line2) +":2\n");
				}
				br2.close();
			}
			br1.close();
			fw.close();
		} //end try
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        
		System.out.println("done");
	}
	public void filterBtnPress()
	{
		String inputStr = textArea.getText();
		String outputStr = "";
		char tempChar;
		
		for (int i = 0;i<inputStr.length();i++)
		{
			tempChar = inputStr.toUpperCase().charAt(i);
			if( ('A' <= tempChar) && (tempChar <= 'Z'))
				outputStr += inputStr.charAt(i);
		}
		StringSelection stringSelection = new StringSelection (outputStr);
		Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
		clpbrd.setContents (stringSelection, null);
		textArea.setText("");
	}
}