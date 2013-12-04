package main.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import main.model.CryptoNet;
import main.model.CypherType;
import main.view.DisplayGraph;

/*
 * This class is NOT thread safe.
 */

//Author: David Vincent

public class Controller 
{
	private static Controller instance = null;
	private CryptoNet theNet;
	private boolean hasCreated;
	
	private Controller()
	{
		theNet = new CryptoNet();
	}
	
	public static Controller getInstance()
	{
		if (instance == null)
		{
			//create a new instance.
			return new Controller();
		}
		else
		{
			//return the instance.
			return instance;
		}
	}

	public void createNetwork(int [] sigHidden, int [] scrawlHidden)
	{
		theNet = new CryptoNet(sigHidden, scrawlHidden);
		hasCreated = true;
	}
	public void createNetworkIfNotExist(int [] sigHidden, int[] scrawlHidden)
	{
		if (hasCreated == false)
			createNetwork(sigHidden,scrawlHidden);
	}
	public void trainNetwork(File trainFile,int iters)
	{
		loadTrainingData(trainFile);
		theNet.train(iters);
	}
	
	public void plotTrainingSig(File trainFile)
	{
		theNet.clearTrainingData();
		loadTrainingData(trainFile);
		
		DisplayGraph[] dispGraph = new DisplayGraph[CypherType.size()];
		for (int i = 0; i< dispGraph.length; i++)
		{
			dispGraph[i] = new DisplayGraph(CypherType.fromIntToString(i) + " : Signiture Plot");
		}
		//For each training example add a new plot.
		

		//Loads the data from train file
		//Read the file
		BufferedReader br = null;
		String line;
		int index;
		String text;
		int type;
		int count;
		
		try 
		{
			br = new BufferedReader(new FileReader(trainFile));
		
			//First step to training. load the data.
			theNet.clearTrainingData();		//Clear the data first.
			count = 0;
			while ((line = br.readLine()) != null)
			{
				//Add the training example.
				//format is [English text]:[class]
				index = line.indexOf(":");
				if (index >=0 )
				{
					text = line.substring(0,index);
					text.replaceAll("[^A-Za-z]+", "");
					
					type = Integer.parseInt(line.substring(index+1,index+2));
					// Here we go.
					theNet.addTrainingExample(text, CypherType.fromInt(type));
					double [] tempArray = CryptoNet.getSigArray(text);
					
					dispGraph[type].addTrace("train" + count);
					for (int i = 0; i <tempArray.length;i++)
					{
						dispGraph[type].addPoint("train" + count, i, tempArray[i]);
					}
				}
				else
				{
					System.out.println("Bad Line ("+count+") in training data..");
				}
				count++;
			}
			br.close();
		} //end try
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	
		//Show the graphs.
		for (int i = 0; i < dispGraph.length; i++)
		{
			dispGraph[i].show();
		}
	}
	public void plotTrainingScrawl(File trainFile)
	{
		theNet.clearTrainingData();
		loadTrainingData(trainFile);
		
		DisplayGraph[] dispGraph = new DisplayGraph[CypherType.size()];
		for (int i = 0; i< dispGraph.length; i++)
		{
			dispGraph[i] = new DisplayGraph(CypherType.fromIntToString(i) + " : Scrawl Plot");
		}
		//For each training example add a new plot.
		

		//Loads the data from train file
		//Read the file
		BufferedReader br = null;
		String line;
		int index;
		String text;
		int type;
		int count;
		
		try 
		{
			br = new BufferedReader(new FileReader(trainFile));
		
			//First step to training. load the data.
			theNet.clearTrainingData();		//Clear the data first.
			count = 0;
			while ((line = br.readLine()) != null)
			{
				//Add the training example.
				//format is [English text]:[class]
				index = line.indexOf(":");
				if (index >=0 )
				{
					text = line.substring(0,index);
					text.replaceAll("[^A-Za-z]+", "");
					
					type = Integer.parseInt(line.substring(index+1,index+2));
					// Here we go.
					theNet.addTrainingExample(text, CypherType.fromInt(type));
					double [] tempArray = CryptoNet.getFreqArray(text);
					
					dispGraph[type].addTrace("train" + count);
					for (int i = 0; i <tempArray.length;i++)
					{
						dispGraph[type].addPoint("train" + count, i, tempArray[i]);
					}
				}
				else
				{
					System.out.println("Bad Line ("+count+") in training data..");
				}
				count++;
			}
			br.close();
		} //end try
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	
		//Show the graphs.
		for (int i = 0; i < dispGraph.length; i++)
		{
			dispGraph[i].show();
		}
	}
	
	public void loadTrainingData(File trainFile)
	{
		//Loads the data from train file
		//Read the file
		BufferedReader br = null;
		String line;
		int index;
		String text;
		int type;
		int count;
		
		try 
		{
			br = new BufferedReader(new FileReader(trainFile));
		
			//First step to training. load the data.
			theNet.clearTrainingData();		//Clear the data first.
			count = 0;
			while ((line = br.readLine()) != null)
			{
				//Add the training example.
				//format is [English text]:[class]
				index = line.indexOf(":");
				if (index >=0 )
				{
					text = line.substring(0,index);
					text.replaceAll("[^A-Za-z]+", "");
					
					type = Integer.parseInt(line.substring(index+1,index+2));
					theNet.addTrainingExample(text, CypherType.fromInt(type));
				}
				else
				{
					System.out.println("Bad Line ("+count+") in training data..");
				}
					count++;
			}
			br.close();
		} //end try
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void printAccuracy()
	{
		//Last step to training. Calculate the accuracy.
		double [] accuracy = theNet.computeTrainingAccuracy();
		System.out.println("SigNet Accuracy: \t" + accuracy[0]);
		System.out.println("ScrawlNet Accuracy: \t"+ accuracy[1]);
	}

	public CypherType getPrediction (String inputStr)
	{
		return theNet.predictNew(inputStr);
	}
	
	public void saveFile (File theFile)
	{
		try 
		{
			theNet.save(theFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadFile (File theFile)
	{
		try
		{
			theNet.load(theFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
