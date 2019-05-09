package com.utils.blu.utils;

import com.utils.blu.constants.DataType;

public class SortArgs {

	/**
	 * Sort based on this key
	 */
	private String key;

	/**
	 * Key contains data in this data type
	 */
	private DataType dataType;

	/**
	 * This flag will be true, if sorting should be done in descending order
	 */
	private boolean isDecending;

	/**
	 * Creates sort argument for the provided key which will be sorted in
	 * ascending order
	 * 
	 * @param key
	 * @param dataType
	 */
	public SortArgs(String key, DataType dataType) {
		this.key = key;
		this.dataType = dataType;
		this.isDecending = false;
	}

	/**
	 * Creates sort argument for the provided key. If isDecending is set to
	 * <code>true</code>, records will be sorted in descending order.
	 * 
	 * @param key
	 * @param dataType
	 * @param isDecending
	 */
	public SortArgs(String key, DataType dataType, boolean isDecending) {
		this.key = key;
		this.dataType = dataType;
		this.isDecending = isDecending;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @return the isDecending
	 */
	public boolean isDecending() {
		return isDecending;
	}

}
