package pl.karol202.bolekserver.server;

import java.util.HashMap;
import java.util.Map;

public class DataBundle
{
	private Map<String, String> stringEntries;
	private Map<String, Integer> intEntries;
	private Map<String, Float> floatEntries;
	
	public DataBundle()
	{
		stringEntries = new HashMap<>();
		intEntries = new HashMap<>();
		floatEntries = new HashMap<>();
	}
	
	public String getString(String key, String defaultValue)
	{
		return stringEntries.getOrDefault(key, defaultValue);
	}
	
	public int getInt(String key, int defaultValue)
	{
		return intEntries.getOrDefault(key, defaultValue);
	}
	
	public float getFloat(String key, float defaultValue)
	{
		return floatEntries.getOrDefault(key, defaultValue);
	}
	
	public void putString(String key, String value)
	{
		stringEntries.put(key, value);
	}
	
	public void putInt(String key, int value)
	{
		intEntries.put(key, value);
	}
	
	public void putFloat(String key, float value)
	{
		floatEntries.put(key, value);
	}
}