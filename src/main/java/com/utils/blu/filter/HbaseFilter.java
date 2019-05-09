package com.utils.blu.filter;

import java.util.List;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

import com.mapr.org.apache.hadoop.hbase.util.Bytes;
import com.utils.blu.exception.InsufficientArgumentException;
import com.utils.blu.filter.utils.FilterParameterValidator;
import com.utils.blu.filter.utils.FilterUtility;
import com.utils.blu.po.SingleColumnValueFilterPO;

/**
 * Utility to build HBASE filters.
 * 
 * @author eanpart
 *
 */
public class HbaseFilter {

	private static Filter scvFilter(SingleColumnValueFilterPO scvfPO) throws InsufficientArgumentException {

		// Declarations
		CompareOp operator;

		// Primitive checks
		FilterParameterValidator.validateSCVFilterParameters(scvfPO);

		// Converting user defined operator to type ComapareOf
		operator = FilterUtility.decodeOperator(scvfPO.getOperator());

		// Building SingleColumnValueFilter
		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(scvfPO.getColumnFamily()),
				Bytes.toBytes(scvfPO.getColumnQualifier()), operator, scvfPO.getValue());

		// Setting necessary conditions
		filter.setFilterIfMissing(scvfPO.isFilterIfMissing());
		filter.setLatestVersionOnly(scvfPO.isLatestVersionOnly());

		return filter;
	}

	private static FilterList scvFilterBuilder(SingleColumnValueFilterPO scvfPO,
			List<SingleColumnValueFilterPO> filterList) throws InsufficientArgumentException {

		FilterList filters = new FilterList();

		if (scvfPO != null) {
			filters.addFilter(scvFilter(scvfPO));
		} else if (filterList != null && !filterList.isEmpty()) {
			for (SingleColumnValueFilterPO filterObj : filterList) {
				filters.addFilter(scvFilter(filterObj));
			}
		}

		return filters;
	}

	/**
	 * Builds Hbase Single column value filter. Use this method to form a single
	 * condition. Accepts input of type SingleColumnValueFilterPO - A single
	 * column value filter parameter object
	 * 
	 * @param scvfPO
	 * @return {@link Object} of type FilterList
	 * @throws InsufficientArgumentException
	 * @Example buildSingleColumnValueFilter(new
	 *          SingleColumnValueFilterPO.PARAMETERBUILDER().columnFamily("").
	 *          columnQualifier("").build());
	 */
	public static Object buildSingleColumnValueFilter(SingleColumnValueFilterPO scvfPO)
			throws InsufficientArgumentException {

		return scvFilterBuilder(scvfPO, null);
	}

	/**
	 * Builds Hbase Single column value filter. Use this method to form multiple
	 * conditions. Accepts input of type SingleColumnValueFilterPO - A single
	 * column value filter parameter object
	 * 
	 * @param filterList
	 * @return {@link Object} of type FilterList
	 * @throws InsufficientArgumentException
	 * @Example buildSingleColumnValueFilter(new
	 *          SingleColumnValueFilterPO.PARAMETERBUILDER().columnFamily("").
	 *          columnQualifier("").build());
	 */
	public static Object buildSingleColumnValueFilter(List<SingleColumnValueFilterPO> filterList)
			throws InsufficientArgumentException {

		return scvFilterBuilder(null, filterList);
	}

}
