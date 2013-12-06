package main.model;
//Author David Vincent
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import org.apache.commons.io.FileUtils;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.util.TransferFunctionType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class CryptoNet 
{
	private NeuralNetwork<BackPropagation> sigNet;
	private NeuralNetwork<BackPropagation> scrawlNet;
	
	private static int instCount = 0;
	
	private DataSet sigTrainData;
	private DataSet scrawlTrainData;
	
	private final static int sigInputNumber = 26;
	private final static int scrawlInputNumber = 26;
	
	private final static int sigOutputNumber = 2;
	private final static int scrawlOutputNumber = 3;
		
	public CryptoNet()
	{
		//default one hidden layer of 15 neurons.
		this(new int[] {15},new int[]{15});
	} 
	public CryptoNet(int [] sigHidden, int[] scrawlHidden)
	{		
		clearDatas();

		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		//Set up the Signature Network.
		
		tempList.add(sigInputNumber);
		for (int value : sigHidden)
			tempList.add(value);
		tempList.add(sigOutputNumber);
		
		sigNet = new MultiLayerPerceptron (tempList, TransferFunctionType.TANH);
		
		//Set up the Scrawl Network
		
		tempList.clear();
		
		tempList.add(scrawlInputNumber);
		for (int value : scrawlHidden)
			tempList.add(value);
		tempList.add(scrawlOutputNumber);
		
		scrawlNet = new MultiLayerPerceptron (tempList, TransferFunctionType.TANH);

	}
	
	public void save(File theFile) throws IOException
	{
		//Place the networks in an array to place in file.
		List<NeuralNetwork> nnArray = new ArrayList<NeuralNetwork>();
		nnArray.add(sigNet);
		nnArray.add(scrawlNet);

		FileOutputStream fout= new FileOutputStream (theFile);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(nnArray);
		fout.close();
	}
	public void load(File theFile) throws IOException, ClassNotFoundException
	{
		
		FileInputStream fin= new FileInputStream (theFile);
		ObjectInputStream ois = new ObjectInputStream(fin);
		ArrayList<NeuralNetwork> nnArray= (ArrayList<NeuralNetwork>) ois.readObject();
		fin.close();

		sigNet = nnArray.get(0);
		scrawlNet = nnArray.get(1);
		
	}
	
	public void addTrainingExample(String text, CypherType type )
	{
		//{1,0,0}	- plain text
		//{0,1,0} 	- mono text
		//{0,0,1}	- poly text
		
		double []  result = getFreqArray(text);
		
		//Add to the signature.
		
		double [] sigOutput = {0,0};
		double [] scrawlOutput = {0,0,0};
		
		if (type == CypherType.PLAIN_TEXT)
		{
			sigOutput = new double[]{1,0};
			scrawlOutput = new double[]{1,0,0};
		}
		else if (type == CypherType.MONO_ALPHABETIC)
		{
			sigOutput = new double[]{1,0};
			scrawlOutput = new double[]{0,1,0};
		}
		else if (type == CypherType.POLY_ALPHABETIC)
		{
			sigOutput = new double[]{0,1};
			scrawlOutput = new double[]{0,0,1};
		}
				
		sigTrainData.addRow(getSigArray(result), sigOutput);		
		scrawlTrainData.addRow(result, scrawlOutput);
	}

	public void clearDatas()
	{
		clearTrainingData();
	}
	public void clearTrainingData()
	{
		sigTrainData = new DataSet(sigInputNumber,sigOutputNumber);
		scrawlTrainData = new DataSet(scrawlInputNumber, scrawlOutputNumber);
		
	}
		
	public DataSet getSigData()
	{
		return sigTrainData;
	}
	public DataSet getScrawlData()
	{
		return scrawlTrainData;
	}
	
	public void train(int iters)
	{
		long startTime;
		BackPropagation learningRule = new BackPropagation();
		
		learningRule.setMaxIterations(iters);
		
		
		
		startTime = System.currentTimeMillis();
		System.out.printf("Training SiglNet [%d examples] ",sigTrainData.size());
		sigNet.learn(sigTrainData, learningRule);
		System.out.println("... ["+(System.currentTimeMillis()-startTime)+" ms]");

		startTime = System.currentTimeMillis();
		System.out.printf("Training ScrawlNet [%d examples] ",scrawlTrainData.size());
		scrawlNet.learn(scrawlTrainData, learningRule);
		System.out.println("... ["+(System.currentTimeMillis()-startTime)+" ms]");

		
	}
	
	public double[] sigPredict(String text)
	{
		double [] result = getSigArray(text);
		
		sigNet.setInput(result);
		sigNet.calculate();
		double [] returnArray = new double[sigNet.getOutput().length];
		System.arraycopy(sigNet.getOutput(), 0, returnArray, 0, returnArray.length);
		
		return returnArray;
	}
	public double[] sigPredict(double[] result)
	{		
		sigNet.setInput(result);
		sigNet.calculate();
		double [] returnArray = new double[sigNet.getOutput().length];
		System.arraycopy(sigNet.getOutput(), 0, returnArray, 0, returnArray.length);
		
		return returnArray;
	}
	
	public double[] scrawlPredict(String text)
	{
		double [] result = getFreqArray(text);
		
		scrawlNet.setInput(result);
		scrawlNet.calculate();
		
		double [] returnArray = new double[scrawlNet.getOutput().length];
		System.arraycopy(scrawlNet.getOutput(), 0, returnArray, 0, returnArray.length);
		
		return returnArray;
	}
	
	public CypherType predictNew(String text)
	{
		//to determine the type. get the sig and scrawl predictions,
		// then get the highest value of their vector additions.
		
		double [] sigPredict = sigPredict(text);
		double [] scrawlPredict = scrawlPredict(text);
		
		int highestIndex;
		double highestValue;
		
		highestIndex = 0;
		highestValue = sigPredict[0];
		for (int i = 0; i < sigPredict.length;i++)
		{
			if (highestValue < sigPredict[i])
			{
				highestValue = sigPredict[i];
				highestIndex = i;
			}
		}
		
		if (highestValue > 0.8)
		{
			if (highestIndex == 0)
			{
				//plain or mono
				if (scrawlPredict[0] > scrawlPredict[1])
					return CypherType.PLAIN_TEXT;
				else
					return CypherType.MONO_ALPHABETIC;
			}
			else
			{
				return CypherType.POLY_ALPHABETIC;
			}
		}
		else
		{
				
			// see what it thinks based on the scrawl
			
			highestIndex = 0;
			highestValue = scrawlPredict[0];
			for (int i = 0; i < scrawlPredict.length;i++)
			{
				if (highestValue < scrawlPredict[i])
				{
					highestValue = scrawlPredict[i];
					highestIndex = i;
				}
			}
		
			return CypherType.fromInt(highestIndex);
			
			
		}
		
		
	
	}
		
	public double[] computeTrainingAccuracy()
	{
		double [] ret = new double[2];
		
		ret[0] = computeAccuracy(sigTrainData,sigNet,sigOutputNumber);		
		ret[1] = computeAccuracy(scrawlTrainData, scrawlNet, scrawlOutputNumber);

		return ret;		
	}

	//Static method declarations.
	
	public static String monoShift(String inputText, int shift)
	{
		String outputStr = "";
		char c;
		char newChar;
		int actualShift = shift;
		
		while (actualShift > 26)
		{
			actualShift -=26;
		}
		//TODO: Finish here
		for (int i = 0; i < inputText.length(); i++)
		{
			c = inputText.toUpperCase().charAt(i);
			newChar = c;
			if (Character.isLetter(c))
			{
				//Apply the shift
				if ( (c + actualShift) > 'Z')
				{
					newChar = (char) ((char) 'A' +((char) (actualShift-1) - ('Z'-c)));
				}
				else
					newChar = (char) (c+actualShift);
			}
			outputStr += newChar;
		}
		
		return outputStr;
	}
	
	public static String polyEncrypt(String text, final String key) {
        String res = "";
        String theKey = key.toUpperCase();
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z')
            {
            	res += c;
            	continue;
            }
            res += (char)((c + theKey.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % theKey.length();
        }
        return res;
    }
    public static String polyDecrypt(String text, final String key) {
        String res = "";
        String theKey = key.toUpperCase();
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z')
            {
            	res += c;
            	continue;
            }
            res += (char)((c - theKey.charAt(j) + 26) % 26 + 'A');
            j = ++j % theKey.length();
        }
        return res;
    }
	
	public static double [][] predictSet(DataSet data, NeuralNetwork net, int outputSize)
	{
		//TODO: Finish Here.
		double result[][] = new double[data.size()][outputSize];
		int i = 0;
		
		for(DataSetRow dataRow : data.getRows()) 
		{
			net.setInput(dataRow.getInput());
			net.calculate();
			double output [] = new double[outputSize];
			System.arraycopy(net.getOutput(), 0, output,0, outputSize);
			result [i] = output;
			System.out.print(i+" predict ");
			for (int j = 0; j < output.length; j++)
			{
				System.out.printf(", %.4f", output[j]);
			}
			double temp[] = dataRow.getDesiredOutput();
			for (int j = 0; j < temp.length; j++)
			{
				System.out.printf(", %.4f", temp[j]);
			}
			i++;
			System.out.println("");
		}
		System.out.println("Done");		
		return result;
	}
	public static double computeAccuracy(DataSet data, NeuralNetwork net, int outputSize)
	{
		double [][] prediction = probabilityToDiscrete(predictSet(data,net,outputSize),0.6);
		double [] currentRow;
		int tempIncorrect;
		int totalCorrect = 0;
		int total = 0;
		
		for (int i = 0; i < prediction.length; i++)
		{
			currentRow = data.getRowAt(i).getDesiredOutput();
			total++;
			tempIncorrect = 0;
			for (int j = 0; j<prediction[i].length;j++)
			{
				//System.out.println(prediction[i].length + " " + currentRow.length);
				if (!doubleEqual(prediction[i][j], currentRow[j]))
					tempIncorrect++;
			}
			if (tempIncorrect == 0)
				totalCorrect++;
		}
		return (double) totalCorrect / total;
	}
	public static boolean doubleEqual(double num1, double num2)
	{
		double t = 0.0000000001;
		return (Math.abs(num1-num2) < t);
	}
	public static int[] getTopologyFromNetwork(NeuralNetwork net)
	{
		Layer[] layers = net.getLayers();
		
		int [] topology = new int[layers.length];
		for (int i = 0; i < topology.length; i++)
		{
			topology[i] = layers[i].getNeuronsCount();
		}
		return topology;
	}
	public static ArrayList<Integer> arraytoIntArrayList(int[] intArray)
	{
		 ArrayList<Integer> array = new ArrayList<Integer>();
		 for (int i = 0; i < intArray.length; i++)
		 {
			 array.add(new Integer(intArray[i]));
		 }
		 return array;
	}
	public static double[] typeToArray(CypherType type)
	{
		double [] array = null;
		switch (type)
		{
		case PLAIN_TEXT:
			array = new double[] {1,0,0};
			break;
		case MONO_ALPHABETIC:
			array =  new double[] {0,1,0};
			break;
		case POLY_ALPHABETIC:
			array = new double[] {0,0,1};
			break;
		}
		return array;
	}
	public static CypherType arrayToType(double[] array)
	{
	
		CypherType type = null;
		if (array[0] == 1 && array[1] == 0 && array[2] == 0)
			type = CypherType.PLAIN_TEXT;
		else if (array[0] == 0 && array[1] == 1 && array[2] == 0)
			type = CypherType.MONO_ALPHABETIC;
		else if (array[0] == 0 && array[1] == 0 && array[2] == 1)
			type = CypherType.POLY_ALPHABETIC;
	
		return type;
		
	}
	
	public static double[] getFreqArray(String str)
	{
		// get the frequency of each letter
		int count;
		double [] freqArray =  new double[26];
		char tempChar;
		char letterA = 'A';
		
		count = 0;
		for (int i = 0; i < str.length(); i++)
		{
			tempChar = str.toUpperCase().charAt(i);
			
			if (!(tempChar < 'A' || tempChar > 'Z'))
			{
				freqArray[tempChar-letterA] += 1;
				count++;
			}
			
		}
		
		
		for (int i = 0; i < freqArray.length; i++)
		{
			freqArray[i] /= (double) count;
		}
		
		return freqArray;
	}
	public static double[] getSigArray(double[] frequencies)
	{
		double [] freq = Arrays.copyOf(frequencies, frequencies.length);
		Arrays.sort(freq);
		return freq;
	}
	public static double[] getSigArray(String str)
	{
		return getSigArray(getFreqArray(str));
	}
	
	public static double [][] probabilityToDiscrete( double[][] array)
	{
		
		return probabilityToDiscrete(array, -1);
	}
	public static double [][] probabilityToDiscrete( double[][] array, double threashold)
	{
		
		double[][] result = new double[array.length][array[0].length];
		
		for (int i = 0; i< array.length;i++)
		{
			
			result[i] = probabilityToDiscrete(array[i],threashold);

		}
		return result;
	}
	public static double [] probabilityToDiscrete(double[] array)
	{
		return probabilityToDiscrete(array,-1);
	}
	public static double [] probabilityToDiscrete(double[] array, double threashold)
	{
		
		//find the biggest index.	
		int index;
		double biggest;
		double [] result = new double[array.length];
		
		//Assume biggest is zero
		index = 0;
		biggest = array[0];
		for (int j =1; j<array.length;j++)
		{
			if (biggest<array[j])
			{
				biggest = array[j];
				index = j;
			}
		}	
		
		//Apply threashold
		if (array[index] >= threashold)
			result[index] = 1;
		else
			result[index] = 0;
		
		return result;
	}

}