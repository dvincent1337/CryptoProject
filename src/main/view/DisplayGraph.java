package main.view;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.JFrame;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

public class DisplayGraph
{
	private Chart2D chart;
	private Hashtable<String,ITrace2D> traceDict;
	private String title;
	private final boolean useColors = true;

	public DisplayGraph()
	{
		 
		this("Default");
		
	}
	public DisplayGraph(String title)
	{
		// Create a chart:  
		chart = new Chart2D();
		// Create an ITrace Dictionary: 
		traceDict = new Hashtable<String,ITrace2D>();
		this.title = title;
	}
	public void show()
	{
	 
	    // Make it visible:
	    // Create a frame.
	    JFrame frame = new JFrame(title);
	    // add the chart to the frame: 
	    frame.getContentPane().add(chart);
	    frame.setSize(600,600);
	    Random random = new Random();
	    frame.setLocation(random.nextInt(500),random.nextInt(400));
	    // Enable the termination button [cross on the upper right edge]: 
	    frame.addWindowListener(
	        new WindowAdapter(){
	          public void windowClosing(WindowEvent e){
	              //System.exit(0);
	          }
	        }
	      );
	    frame.setVisible(true);
	}

	public void addTrace(String name)
	{
		ITrace2D trace  = new Trace2DSimple(); 
		trace.setName("");
		
		if (useColors)
			trace.setColor( randomColor());
		
		traceDict.put(name, trace);
		// Add the trace to the chart. This has to be done before adding points (deadlock prevention): 
	    chart.addTrace(trace);   
		
	}
	public void addPoint(String name, double x, double y)
	{
		traceDict.get(name).addPoint(x,y);
	}
	private Color randomColor()
	{
		Random randomGenerator = new Random();
		int red = randomGenerator.nextInt(255);
		int green = randomGenerator.nextInt(255);
		int blue = randomGenerator.nextInt(255);

		return new Color(red,green,blue);
	}

	
}
