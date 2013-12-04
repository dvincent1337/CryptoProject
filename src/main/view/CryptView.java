package main.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SpinnerNumberModel;

import main.controller.Controller;
import main.model.CryptoNet;
import main.model.CypherType;

import javax.swing.JSplitPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class CryptView extends JFrame {

	private Controller controller;
	
	private JPanel contentPane;
	private JTextField sigNetNeurons;
	private JButton trainBrowseBtn;
	private JLabel trainFileStatusLbl;
	private JSpinner itrSpinner;
	private JButton btnTrainNetwork;
	private File trainingFile;
	private JTextArea inputTextArea;
	private JLabel inputResultLbl;
	private JButton saveNetworkBtn;
	private JButton btnOpenNetwork;
	private JButton plotFrequencyBtn;
	private JLabel lblScralnetHiddenNeurons;
	private JTextField scrawlNetNeurons;
	private JTextArea crackTextArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CryptView frame = new CryptView();
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
	public CryptView() {
		controller = Controller.getInstance();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 844, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblConfigurationAndTraining = new JLabel("Configuration and Training");
		lblConfigurationAndTraining.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblConfigurationAndTraining.setBounds(31, 11, 268, 34);
		contentPane.add(lblConfigurationAndTraining);
		
		JLabel lblTrainingFile = new JLabel("Training File:");
		lblTrainingFile.setBounds(143, 58, 76, 14);
		contentPane.add(lblTrainingFile);
		
		trainBrowseBtn = new JButton("Browse");
		trainBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trainBrowseBtnPress();
			}
		});
		trainBrowseBtn.setBounds(31, 54, 89, 23);
		contentPane.add(trainBrowseBtn);
		
		trainFileStatusLbl = new JLabel("No file opened");
		trainFileStatusLbl.setBounds(229, 58, 203, 14);
		contentPane.add(trainFileStatusLbl);
		
		btnTrainNetwork = new JButton("Train Network!");
		btnTrainNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trainBtnPress();
			}
		});
		btnTrainNetwork.setBounds(31, 184, 121, 23);
		contentPane.add(btnTrainNetwork);
		
		sigNetNeurons = new JTextField();
		sigNetNeurons.setText("15");
		sigNetNeurons.setBounds(198, 127, 123, 20);
		contentPane.add(sigNetNeurons);
		sigNetNeurons.setColumns(10);
		
		JLabel lblHiddenLayerNeurons = new JLabel("SigNet Hidden Neurons");
		lblHiddenLayerNeurons.setBounds(31, 136, 143, 14);
		contentPane.add(lblHiddenLayerNeurons);
		
		itrSpinner = new JSpinner();
		itrSpinner.setModel(new SpinnerNumberModel(new Integer(1), null, null, new Integer(1)));
		itrSpinner.setBounds(196, 185, 125, 20);
		contentPane.add(itrSpinner);
		
		JButton plotSig = new JButton("Plot Signiture");
		plotSig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				plotTrainSigBtnPress();
			}
		});
		plotSig.setBounds(31, 96, 116, 23);
		contentPane.add(plotSig);
		
		JLabel inputLbl = new JLabel("Input");
		inputLbl.setFont(new Font("Tahoma", Font.PLAIN, 21));
		inputLbl.setBounds(446, 11, 61, 34);
		contentPane.add(inputLbl);
		
		inputTextArea = new JTextArea();
		inputTextArea.setLineWrap(true);
		inputTextArea.setWrapStyleWord(true);
		inputTextArea.setBounds(331, 54, 300, 196);
		contentPane.add(inputTextArea);
		
		JButton btnRunNetwork = new JButton("Run Network");
		btnRunNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runNetworkBtnPress();
			}
		});
		btnRunNetwork.setBounds(665, 126, 140, 23);
		contentPane.add(btnRunNetwork);
		
		JLabel lblResults = new JLabel("Results:");
		lblResults.setBounds(706, 172, 46, 14);
		contentPane.add(lblResults);
		
		inputResultLbl = new JLabel("[nothing]");
		inputResultLbl.setVerticalAlignment(SwingConstants.TOP);
		inputResultLbl.setBounds(665, 197, 140, 71);
		contentPane.add(inputResultLbl);
		
		saveNetworkBtn = new JButton("Save Network");
		saveNetworkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveNetworkBtnPress();
			}
		});
		saveNetworkBtn.setBounds(30, 227, 122, 23);
		contentPane.add(saveNetworkBtn);
		
		btnOpenNetwork = new JButton("Open Network");
		btnOpenNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openNetworkBtnPress();
			}
		});
		btnOpenNetwork.setBounds(177, 227, 122, 23);
		contentPane.add(btnOpenNetwork);
		
		plotFrequencyBtn = new JButton("Plot Signiture");
		plotFrequencyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotFrequencyBtnPress();
			}
		});
		plotFrequencyBtn.setBounds(663, 54, 142, 23);
		contentPane.add(plotFrequencyBtn);
		
		lblScralnetHiddenNeurons = new JLabel("ScrawlNet Hidden Neurons");
		lblScralnetHiddenNeurons.setBounds(31, 159, 165, 14);
		contentPane.add(lblScralnetHiddenNeurons);
		
		scrawlNetNeurons = new JTextField();
		scrawlNetNeurons.setText("15");
		scrawlNetNeurons.setColumns(10);
		scrawlNetNeurons.setBounds(198, 154, 123, 20);
		contentPane.add(scrawlNetNeurons);
		
		JButton btnPlotScrawl = new JButton("Plot Scrawl");
		btnPlotScrawl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotTrainScrawlBtnPress();
			}
		});
		btnPlotScrawl.setBounds(183, 96, 116, 23);
		contentPane.add(btnPlotScrawl);
		
		JButton btnPlotScrawl_1 = new JButton("Plot Scrawl");
		btnPlotScrawl_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				plotInputScrawl();
			}
		});
		btnPlotScrawl_1.setBounds(663, 88, 142, 23);
		contentPane.add(btnPlotScrawl_1);
		
		crackTextArea = new JTextArea();
		crackTextArea.setWrapStyleWord(true);
		crackTextArea.setLineWrap(true);
		crackTextArea.setBounds(258, 314, 468, 196);
		contentPane.add(crackTextArea);
		
		JButton crackBtn = new JButton("Crack!");
		crackBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCrackPress();
			}
		});
		crackBtn.setBounds(31, 315, 188, 23);
		contentPane.add(crackBtn);
	}
	
	public void trainBrowseBtnPress()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(CryptView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            trainingFile = fc.getSelectedFile();
            //This is where a real application would open the file.
            trainFileStatusLbl.setText(trainingFile.getName());
        }
	}
	public void trainBtnPress()
	{
		//make or load a nerual network
		//currently we are just creating a new one based on the given topology.
		
		//-- Get the topology.
		//Create a neural network with given topology.
		
		//Get hidden topology
		String [] sigHidden = sigNetNeurons.getText().split(",");
		String [] scrawlHidden = scrawlNetNeurons.getText().split(",");
		
		int tempInt;
		ArrayList<Integer> sigList = new ArrayList<Integer>();
		ArrayList<Integer> scrawlList = new ArrayList<Integer>();
		int[] sigTop;
		int [] scrawlTop;
		
		//put sig into the list
		for (String str : sigHidden)
		{
			tempInt = Integer.parseInt(str);
			if (tempInt > 0)
			{
				sigList.add(tempInt);
			}
		}
		//put scrawl into the list
		for (String str : scrawlHidden)
		{
			tempInt = Integer.parseInt(str);
			if (tempInt > 0)
			{
				scrawlList.add(tempInt);
			}
		}
		
		tempInt = 0;
		sigTop = new int[sigList.size()];
		for (Integer theInt : sigList)
		{
			sigTop[tempInt++] = theInt.intValue();
		}
		
		tempInt = 0;
		scrawlTop = new int[scrawlList.size()];
		for (Integer theInt : scrawlList)
		{
			scrawlTop[tempInt++] = theInt.intValue();
		}
		//-- Got the topology.
		
		//-- Create the neural network.
		controller.createNetworkIfNotExist(sigTop,scrawlTop);
		//-- Created the neural network
		
		//-- Train the neural network.
		if (trainingFile != null)	
		{
			controller.trainNetwork(trainingFile, ((Integer)itrSpinner.getValue()).intValue() );
		}
		else
		{
			return;
		}
		//-- Trained the neural network. 
		
		//-- Compute the accuracy and print the results
		controller.printAccuracy();
		//-- Computed the accuracy and printed the results
	}
	public void plotTrainSigBtnPress()
	{
		controller.plotTrainingSig(trainingFile);
	}
	public void plotTrainScrawlBtnPress()
	{
		controller.plotTrainingScrawl(trainingFile);
	}
	public void runNetworkBtnPress()
	{
		
		if (inputTextArea.getText() != null)
		{
			CypherType type = controller.getPrediction(inputTextArea.getText());
			
			String text = "";
			
			if (type == null)
				text = "Not found";
			else 
			{
				int theInt = 0;
				switch(type)
				{
				case PLAIN_TEXT:
					text = "Plain Text";
					theInt = 0;
					break;
				case MONO_ALPHABETIC:
					text = "Mono Alhpabetic";
					theInt = 1;
					break;
				case POLY_ALPHABETIC:
					text = "Poly Alphabetic";
					theInt = 2;
					break;
				}
			}
			
			inputResultLbl.setText("<html>Detected Type: "+text+"</html>");
		}
	}
	public void saveNetworkBtnPress()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(CryptView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File theFile = fc.getSelectedFile();
            controller.saveFile(theFile);
        }
	}
	public void openNetworkBtnPress()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(CryptView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File theFile = fc.getSelectedFile();
            controller.loadFile(theFile);
        }
	}
	public void plotFrequencyBtnPress()
	{
		DisplayGraph dispGraph = new DisplayGraph("Input Text : Signiture");
		dispGraph.addTrace("InputTrace");
		double [] things = CryptoNet.getSigArray(inputTextArea.getText());
		for (int i = 0; i < things.length; i++)
		{
			dispGraph.addPoint("InputTrace", i, things[i]);
		}
		dispGraph.show();
	}
	public void plotInputScrawl()
	{
		DisplayGraph dispGraph = new DisplayGraph("Input Text : Scrawl");
		dispGraph.addTrace("InputTrace");
		double [] things = CryptoNet.getFreqArray(inputTextArea.getText());
		for (int i = 0; i < things.length; i++)
		{
			dispGraph.addPoint("InputTrace", i, things[i]);
		}
		dispGraph.show();
	}
	public void btnCrackPress()
	{
		//Try to crack the Cypher. 
		String originalStr = inputTextArea.getText();
		String tempStr = "";;
		CypherType originalType = controller.getPrediction(originalStr);
		
		
		if (originalType == CypherType.MONO_ALPHABETIC)
		{
			//try all shifts until plain text is found.
			for (int i = 1; i <=25; i++)
			{
				tempStr = CryptoNet.monoShift(originalStr, i);
				if (controller.getPrediction(tempStr) == CypherType.PLAIN_TEXT)
				{
					break;
				}
			}
			crackTextArea.setText(tempStr);
			//System.out.println("Success: " + shiftAmount);
		}
		else if(originalType == CypherType.POLY_ALPHABETIC)
		{
			int keyLen = 1;
			
			String tempKey = "";
			long  num;
			long max =9223372036854775807L;
			System.out.println("Going to crack!");
			for (num = 27; num <max; num++)
			{
				tempKey = intToLetter(num);
				tempStr = CryptoNet.polyDecrypt(originalStr, tempKey);
				System.out.print("\rCurrent: "+ tempKey + " " + tempStr);

				
				if (controller.getPrediction(tempStr) == CypherType.PLAIN_TEXT)
				{
					System.out.println("Cracked!"+tempKey);
					break;
				}
			}
			crackTextArea.setText(tempStr);
		}
	}
	public static String intToLetter(long Int) {
		//from http://stackoverflow.com/questions/8710719/
	    if (Int<27){
	      return Character.toString((char)(Int+96));
	    } else {
	      if (Int%26==0) {
	        return intToLetter((Int/26)-1)+intToLetter((Int%26)+1);
	      } else {
	        return intToLetter(Int/26)+intToLetter(Int%26);
	      }
	    }
	  }
}
