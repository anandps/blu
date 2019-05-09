package com.utils.blu.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataStore {

	private DataMap dataMap;
	private List<DataMap> dataMapList;

	public DataStore() {
		dataMap = new DataMap();
		dataMapList = new ArrayList<DataMap>();
	}

	/**
	 * Inserts the provided {@link List}<{@link DataMap}>
	 * 
	 * @param dataMaps
	 */
	public void setDataMapList(List<DataMap> dataMaps) {
		dataMapList.addAll(dataMaps);
	}

	/**
	 * Inserts the {@link DataMap} into the {@link List} of {@link DataMap}
	 * 
	 * @param dataMap
	 */
	public void setDataMapList(DataMap dataMap) {
		dataMapList.add(dataMap);
	}

	/**
	 * Sets the provided {@link DataMap} in the mentioned index
	 * 
	 * @param index
	 * @param dataMap
	 */
	public void setDataMapList(int index, DataMap dataMap) {
		dataMapList.add(index, dataMap);
	}

	/**
	 * Sets the provided {@link DataMap}
	 * 
	 * @param dataMap
	 */
	public void setDataMap(DataMap dataMap) {
		this.dataMap.putAll(dataMap);
	}

	/**
	 * Gets all DataMap from the {@link List}<{@link DataMap}>
	 */
	public List<DataMap> getDataMaps() {
		return dataMapList;
	}

	/**
	 * Gets the {@link DataMap} from {@link DataStore}
	 */
	public DataMap getDataMap() {
		return dataMap;
	}

	/**
	 * Removes the {@link DataMap} from the mentioned index
	 * 
	 * @param index
	 * @return
	 */
	public List<DataMap> removeDataMap(int index) {
		dataMapList.remove(index);
		return dataMapList;

	}

	/**
	 * Get the datastore as string
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer(500);
		buffer.append("DataStore-> : \n");
		buffer.append("DataMap--> : " + dataMap + "\n");
		buffer.append("List<DataMap> --> : ");
		for (DataMap dm : dataMapList) {
			buffer.append(dm + "\n");
		}
		return buffer.toString();
	}

	/**
	 * Clears {@link DataMap} and {@link List}<{@link DataMap}>
	 */
	public void clear() {
		dataMap.clear();
		dataMapList.clear();
	}

	/**
	 * Gets the size of {@link List}<{@link DataMap}>
	 * 
	 * @return int
	 */
	public int size() {
		return dataMapList.size();
	}
}
