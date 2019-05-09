package com.utils.blu.utils;

import com.utils.blu.constants.DataType;
import com.utils.blu.constants.Operator;
import com.utils.blu.exception.InsufficientArgumentException;

public final class ConditionArgs{

	private String key;
	private Operator operator;
	private DataType dataType;
	private Object value;

	public ConditionArgs(String key, Operator operator, DataType dataType, Object value)
			throws InsufficientArgumentException {
		if (key == null || operator == null || dataType == null || value == null) {
			throw new InsufficientArgumentException("Arguments can't have null value");
		}
		this.key = key;
		this.operator = operator;
		this.dataType = dataType;
		this.value = value;
	}

	public ConditionArgs(String key, Operator operator) throws InsufficientArgumentException {
		if (key == null || operator == null) {
			throw new InsufficientArgumentException("Arguments can't have null value");
		}
		this.key = key;
		this.operator = operator;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}
}
