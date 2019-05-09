package com.utils.blu.filter.utils;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;

import com.utils.blu.constants.Operator;

public class FilterUtility {

	/**
	 * Converts user defined operator to type CompareOP
	 * 
	 * @param operator
	 * @return {@link CompareOp}
	 */
	public static CompareOp decodeOperator(Operator operator) {
		CompareOp op = null;
		switch (operator) {
		case EQUAL:
			op = CompareOp.EQUAL;
			break;

		case GREATER:
			op = CompareOp.GREATER;
			break;

		case GREATER_OR_EQUAL:
			op = CompareOp.GREATER_OR_EQUAL;
			break;

		case LESS:
			op = CompareOp.LESS;
			break;

		case LESS_OR_EQUAL:
			op = CompareOp.LESS_OR_EQUAL;
			break;

		case NOT_EQUAL:
			op = CompareOp.NOT_EQUAL;
			break;

		default:
			break;
		}
		return op;

	}

}
