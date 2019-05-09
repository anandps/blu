package com.utils.blu.utils;

import java.util.Comparator;

import com.utils.blu.constants.DataType;
import com.utils.blu.datastructures.DataMap;

public class DataMapComparator implements Comparator<DataMap> {

	final private String compareKey;
	final private DataType dataType;

	public DataMapComparator(String compareKey, DataType dataType) {
		this.compareKey = compareKey;
		this.dataType = dataType;
	}

	@Override
	public int compare(DataMap dm1, DataMap dm2) {
		int result = 0;
		switch (dataType) {
		case STRING:
			result = (dm1.getAsString(compareKey) == null ? "" : dm1.getAsString(compareKey))
					.compareTo(dm2.getAsString(compareKey) == null ? "" : dm2.getAsString(compareKey));
			break;
		case LONG:
			Long l = 0L, long1 = (Long) dm1.getAsNumber(compareKey), long2 = (Long) dm2.getAsNumber(compareKey);
			result = ((long1 == null ? l : long1).compareTo(long2 == null ? l : long2));
			break;
		case INT:
			Integer integer1 = (Integer) dm1.get(compareKey), integer2 = (Integer) dm2.get(compareKey);
			result = (integer1 == null ? 0 : integer1) - (integer2 == null ? 0 : (Integer) integer2);
			break;
		case BOOLEAN:
			Boolean b1 = false;
			Boolean b2 = false;
			try {
				b1 = dm1.get(compareKey) == null ? false : dm1.getAsBoolean(compareKey);
			} catch (Exception e) {
				// Do Nothing
			}
			try{
				b2 = dm2.get(compareKey) == null ? false : dm2.getAsBoolean(compareKey);
			} catch (Exception e) {
				// Do Nothing
			}
			result = b1.compareTo(b2);
			break;
		}
		return result;
	}
}
