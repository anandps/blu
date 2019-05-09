package com.utils.blu.utils;

import java.util.Collections;
import java.util.List;

import com.utils.blu.datastructures.DataMap;
import com.utils.blu.datastructures.DataStore;

import org.apache.commons.collections.comparators.ComparatorChain;

public class SortRecords {

	/**
	 * Logging Instance
	 */
	private static final NotificationLogger LOGGER = NotificationLoggerFactory.getLogger();

	/**
	 * Logging Class Name
	 */
	private static final String LOGGING_CLASS_NAME = SortRecords.class.getName();

	/**
	 * Sorts the {@link DataMap} inside {@link DataStore} based on the keys
	 * provided
	 * 
	 * @param eventDataStore
	 * @param keyMap,
	 *            Key should be the value and it's dataType as value
	 * @return
	 */
	public static void sorter(DataStore eventDataStore, List<SortArgs> sortArgs) throws Exception {
		ComparatorChain comparatorChain = new ComparatorChain();
		for (SortArgs arg : sortArgs) {
			comparatorChain.addComparator(new DataMapComparator(arg.getKey(), arg.getDataType()), arg.isDecending());
		}

		if (comparatorChain.size() != 0)
			Collections.sort(eventDataStore.getDataMaps(), comparatorChain);
		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG,
				"Records sorted based on sort keys : " + eventDataStore);
	}
}
