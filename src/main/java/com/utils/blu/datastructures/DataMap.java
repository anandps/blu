package com.utils.blu.datastructures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.lang.Number;

public class DataMap {

	private HashMap<Object, Object> map;

	public DataMap() {
		this.map = new HashMap<Object, Object>();
	}

	public DataMap(DataMap dm) {
		this.map = new HashMap<Object, Object>();
		this.map.putAll(dm.map);
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public void putAll(DataMap dataMap) {
		map.putAll(dataMap.map);
	}

	public void clear() {
		map.clear();
	}

	public Set<Object> keySet() {
		return map.keySet();
	}

	public Collection<Object> values() {
		return map.values();
	}

	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	/**
	 * This method gets a value for a key as String
	 * 
	 * @param key
	 * @return String
	 */
	public String getAsString(String key) {
		Object value = get(key);
		return (value == null ? "" : value.toString());
	}

	/**
	 * This method gets a value for a key as byte array
	 * 
	 * @param key
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getAsByteArray(String key) throws Exception {
		Object value = get(key);
		if (value instanceof byte[]) {
			return (byte[]) value;
		} else {
			throw new Exception("The value for " + key.toString() + " is not a byte array");
		}
	}

	/**
	 * This method gets the value for a key as a Number. As of now, it supports
	 * only integer and long
	 * 
	 * @param key
	 * @return Number
	 * @throws Exception
	 */
	public Number getAsNumber(String key) throws NumberFormatException {
		Object value = get(key);
		if (value == null)
			return null;
		if (value instanceof Number) {
			return (Number) value;
		} else {
			try {
				return Long.valueOf(value.toString());
			} catch (NumberFormatException e) {
				throw new NumberFormatException("The value for " + key.toString() + "is not a number");
			}
		}
	}

	/**
	 * This method gets the value for a key as a boolean
	 * 
	 * @param key
	 * @return boolean
	 * @throws Exception
	 */
	public boolean getAsBoolean(String key) throws Exception {
		Object value = get(key);
		if (value instanceof Boolean) {
			return (Boolean) value;
		} else {
			throw new Exception("The value for " + key.toString() + " is not a Boolean");
		}
	}

	/**
	 * Get Map as String
	 */
	public String toString() {
		return this.map.toString();
	}
}
