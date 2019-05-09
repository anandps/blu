package com.utils.blu.po;

import com.utils.blu.constants.Operator;

/**
 * Constructs SingleColumnValueFilter parameter object for Hbase filter related
 * operations
 * 
 * @author eanpart
 *
 */
public class SingleColumnValueFilterPO {

	boolean filterIfMissing, latestVersionOnly;
	private String columnFamily, columnQualifier;
	private byte[] value;
	private Operator operator;

	private SingleColumnValueFilterPO(PARAMBUILDER builder) {
		// TODO Auto-generated constructor stub
		this.columnFamily = builder.columnFamily;
		this.columnQualifier = builder.columnQualifier;
		this.value = builder.value;
		this.operator = builder.operator;
		this.filterIfMissing = builder.filterIfMissing;
		this.latestVersionOnly = builder.latestVersionOnly;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public String getColumnQualifier() {
		return columnQualifier;
	}

	public byte[] getValue() {
		return value;
	}

	public Operator getOperator() {
		return operator;
	}

	public boolean isFilterIfMissing() {
		return filterIfMissing;
	}

	public boolean isLatestVersionOnly() {
		return latestVersionOnly;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEWLINE = System.lineSeparator();

		result.append(this.getClass().getName() + " Object {" + NEWLINE);
		result.append("columnFamily:" + columnFamily + NEWLINE);
		result.append("columnQualifier:" + columnQualifier + NEWLINE);
		result.append("value:" + value + NEWLINE);
		result.append("operator:" + operator.toString() + NEWLINE);
		result.append("filterIfMissing:" + filterIfMissing + NEWLINE);
		result.append("latestVersionOnly:" + latestVersionOnly + NEWLINE);
		result.append("}");

		return result.toString();
	}

	public static class PARAMBUILDER {

		boolean filterIfMissing, latestVersionOnly;
		private String columnFamily, columnQualifier;
		private byte[] value;
		private Operator operator;

		public PARAMBUILDER columnFamily(String columnFamily) {
			this.columnFamily = columnFamily;
			return this;
		}

		public PARAMBUILDER columnQualifier(String columnQualifier) {
			this.columnQualifier = columnQualifier;
			return this;
		}

		public PARAMBUILDER value(byte[] value) {
			this.value = value;
			return this;
		}

		public PARAMBUILDER operator(Operator operator) {
			this.operator = operator;
			return this;
		}

		public PARAMBUILDER setFilterIfMissing(boolean filterIfMissing) {
			this.filterIfMissing = filterIfMissing;
			return this;
		}

		public PARAMBUILDER setLatestVersionOnly(boolean latestVersionOnly) {
			this.latestVersionOnly = latestVersionOnly;
			return this;
		}

		public SingleColumnValueFilterPO build() {
			return new SingleColumnValueFilterPO(this);
		}

	}
}
