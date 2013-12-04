package main.model;

//Author: David Vincent

public enum CypherType 
{
	PLAIN_TEXT, MONO_ALPHABETIC, POLY_ALPHABETIC,UNKNOWN;

	public static CypherType fromInt(int type)
	{
		switch (type)
		{
			case 0:
				return PLAIN_TEXT;
			case 1:
				return MONO_ALPHABETIC;
			case 2:
				return POLY_ALPHABETIC;
			case 3:
				return UNKNOWN;
		}
		return null;
	}
	public static String fromIntToString(int type)
	{
		switch (type)
		{
			case 0:
				return "PLAIN_TEXT";
			case 1:
				return "MONO_ALPHABETIC";
			case 2:
				return "POLY_ALPHABETIC";
			case 3: 
				return "UNKNOWN";
		}
		return "";
	}
	
	public static CypherType fromArray(double[] discreteArray)
	{
		if (discreteArray[0] == 1)
			return PLAIN_TEXT;
		else if (discreteArray[1] == 1)
			return MONO_ALPHABETIC;
		else if (discreteArray[2] == 1)
			return POLY_ALPHABETIC;
		else if (discreteArray[3] == 1)
			return UNKNOWN;
		else
			return null;
	}
	
	public static int size()
	{
		return 4;
	}
	
}
