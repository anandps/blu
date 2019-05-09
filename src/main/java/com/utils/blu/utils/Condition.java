package com.utils.blu.utils;

import com.utils.blu.constants.DataType;
import com.utils.blu.datastructures.DataMap;

public final class Condition {

	private Condition() {
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean lessThan(DataMap input, final String key, DataType dataType, final Object value) {
		switch (dataType) {
		case INT:
			return (input.getAsNumber(key).intValue() < (Integer) value);
		case LONG:
			return (input.getAsNumber(key).longValue() < (Long) value);
		default:
			return false;
		}
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean lessThanEqual(DataMap input, final String key, DataType dataType, final Object value) {
		switch (dataType) {
		case INT:
			return (input.getAsNumber(key).intValue() <= (Integer) value);
		case LONG:
			return (input.getAsNumber(key).longValue() <= (Long) value);
		default:
			return false;
		}
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean greaterThan(DataMap input, final String key, DataType dataType, final Object value) {
		switch (dataType) {
		case INT:
			return (input.getAsNumber(key).intValue() > (Integer) value);
		case LONG:
			return (input.getAsNumber(key).longValue() > (Long) value);
		default:
			return false;
		}
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean greaterThanEqual(DataMap input, final String key, DataType dataType, final Object value) {
		switch (dataType) {
		case INT:
			return (input.getAsNumber(key).intValue() >= (Integer) value);
		case LONG:
			return (input.getAsNumber(key).longValue() >= (Long) value);
		default:
			return false;
		}
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean equal(DataMap input, final String key, DataType dataType, final Object value) {
		switch (dataType) {
		case STRING:
			return input.getAsString(key).equals(value);
		case INT:
			return (input.getAsNumber(key).intValue() == (Integer) value);
		case LONG:
			return (input.getAsNumber(key).longValue() == (Long) value);
		case OBJECT:
			return input.get(key).equals(value);
		default:
			return false;
		}
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean contains(DataMap input, final String key, final String value) {
		return input.getAsString(key).contains(value);
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @return
	 */
	public static boolean containsKey(DataMap input, final String key) {
		return input.containsKey(key);
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap is empty
	 * 
	 * @param input
	 * @param key
	 * @return
	 */
	public static boolean empty(DataMap input, final String key) {
		return input.getAsString(key).isEmpty();
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean startsWith(DataMap input, final String key, final String value) {
		return input.getAsString(key).startsWith(value);
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean endsWith(DataMap input, final String key, final String value) {
		return input.getAsString(key).endsWith(value);
	}

	/**
	 * Gets a value as input and checks it with provided key in DataMap
	 * 
	 * @param input
	 * @param key
	 * @param dataType
	 * @param value
	 * @return
	 */
	public static boolean notEqual(DataMap input, final String key, DataType dataType, final Object value) {
		switch (dataType) {
		case STRING:
			return !input.getAsString(key).equals(value);
		case INT:
			return (input.getAsNumber(key).intValue() != (Integer) value);
		case LONG:
			return (input.getAsNumber(key).longValue() != (Long) value);
		case OBJECT:
			if (input.get(key) == null) {
				return true;
			}
			return !input.get(key).equals(value);
		default:
			return false;
		}
	}

}
