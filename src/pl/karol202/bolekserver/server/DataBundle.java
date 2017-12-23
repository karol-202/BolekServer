package pl.karol202.bolekserver.server;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DataBundle
{
	public class Entry<T>
	{
		private String key;
		private T value;
		
		Entry(String key, T value)
		{
			this.key = key;
			this.value = value;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public T getValue()
		{
			return value;
		}
	}
	
	private Map<String, String> stringEntries;
	private Map<String, Integer> intEntries;
	private Map<String, Boolean> booleanEntries;
	
	public DataBundle()
	{
		stringEntries = new HashMap<>();
		intEntries = new HashMap<>();
		booleanEntries = new HashMap<>();
	}
	
	public String getString(String key, String defaultValue)
	{
		return stringEntries.getOrDefault(key, defaultValue);
	}
	
	public int getInt(String key, int defaultValue)
	{
		return intEntries.getOrDefault(key, defaultValue);
	}
	
	public boolean getBoolean(String key, boolean defaultValue)
	{
		return booleanEntries.getOrDefault(key, defaultValue);
	}
	
	public void putString(String key, String value)
	{
		stringEntries.put(key, value);
	}
	
	public void putInt(String key, int value)
	{
		intEntries.put(key, value);
	}
	
	public void putBoolean(String key, boolean value)
	{
		booleanEntries.put(key, value);
	}
	
	public Stream<Entry<String>> getStringEntriesStream()
	{
		return getEntriesStream(stringEntries);
	}
	
	public Stream<Entry<Integer>> getIntEntriesStream()
	{
		return getEntriesStream(intEntries);
	}
	
	public Stream<Entry<Boolean>> getBooleanEntriesStream()
	{
		return getEntriesStream(booleanEntries);
	}
	
	private <T> Stream<Entry<T>> getEntriesStream(Map<String, T> entries)
	{
		return entries.entrySet().stream().map(e -> new Entry<>(e.getKey(), e.getValue()));
	}
}