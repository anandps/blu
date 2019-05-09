package com.utils.blu.utils;

import java.util.List;

import com.utils.blu.datastructures.DataMap;
import com.utils.blu.datastructures.DataStore;

public final class Filter {

	private DataStore matchedDataStore;

	private DataStore unMatchedDataStore;

	/**
	 * @return the matchedDataStore
	 */
	public DataStore getMatchedDataStore() {
		return matchedDataStore;
	}

	/**
	 * @return the unMatchedDataStore
	 */
	public DataStore getUnMatchedDataStore() {
		return unMatchedDataStore;
	}

	public enum LogicalOperator {
		AND {
			public String toString() {
				return "AND";
			}
		},
		OR {
			public String toString() {
				return "OR";
			}
		}
	};

	public void applyFilter(DataStore inputDataStore, LogicalOperator logicalOperator, List<ConditionArgs> arguments) {

		matchedDataStore = new DataStore();
		unMatchedDataStore = new DataStore();

		for (DataMap datamap : inputDataStore.getDataMaps()) {
			boolean isMatched = true;
			condition: for (final ConditionArgs argument : arguments) {
				if (!isMatched && logicalOperator.equals(LogicalOperator.AND))
					break condition;

				switch (argument.getOperator()) {
				case EQUAL:
					isMatched = Condition.equal(datamap, argument.getKey(), argument.getDataType(),
							argument.getValue());
					break;
				case LESS:
					isMatched = Condition.lessThan(datamap, argument.getKey(), argument.getDataType(),
							argument.getValue());
					break;
				case LESS_OR_EQUAL:
					isMatched = Condition.lessThanEqual(datamap, argument.getKey(), argument.getDataType(),
							argument.getValue());
					break;
				case GREATER:
					isMatched = Condition.greaterThan(datamap, argument.getKey(), argument.getDataType(),
							argument.getValue());
					break;
				case GREATER_OR_EQUAL:
					isMatched = Condition.greaterThanEqual(datamap, argument.getKey(), argument.getDataType(),
							argument.getValue());
					break;
				case CONTAINS:
					isMatched = Condition.contains(datamap, argument.getKey(), argument.getValue().toString());
					break;
				case CONTAINSKEY:
					isMatched = Condition.containsKey(datamap, argument.getKey());
					break;
				case STARTSWITH:
					isMatched = Condition.startsWith(datamap, argument.getKey(), argument.getValue().toString());
					break;
				case ENDSWITH:
					isMatched = Condition.endsWith(datamap, argument.getKey(), argument.getValue().toString());
					break;
				case NOT_EQUAL:
					isMatched = Condition.notEqual(datamap, argument.getKey(), argument.getDataType(),
							argument.getValue().toString());
					break;
				case EMPTY:
					isMatched = Condition.empty(datamap, argument.getKey());
					break;
				case NOTEMPTY:
					isMatched = !Condition.empty(datamap, argument.getKey());
					break;
				}

				if (isMatched && logicalOperator.equals(LogicalOperator.OR))
					break condition;
			}

			if (isMatched) {
				matchedDataStore.setDataMapList(datamap);
			} else {
				unMatchedDataStore.setDataMapList(datamap);
			}

			inputDataStore = null;
		}
	}
}
