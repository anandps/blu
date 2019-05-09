package com.utils.blu.po;

import java.util.Set;

import com.utils.blu.schema.Field;

/**
 * Constructs parameter object for read operation
 * 
 * @author eanpart
 *
 */
public class ReadPO {

	private int noOfVersions;
	private String table, columnFamily;
	private byte[] rowKey;
	private Set<Field> fields;

	private ReadPO(PARAMBUILDER builder) {
		// TODO Auto-generated constructor stub
		this.noOfVersions = builder.noOfVersions;
		this.table = builder.table;
		this.rowKey = builder.rowKey;
		this.fields = builder.fields;
		this.columnFamily = builder.columnFamily;

	}

	public int getNoOfVersions() {
		return noOfVersions;
	}

	public String getTable() {
		return table;
	}

	public byte[] getRowKey() {
		return rowKey;
	}

	public Set<Field> getFields() {
		return fields;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEWLINE = System.lineSeparator();

		result.append(this.getClass().getName() + " Object {" + NEWLINE);
		result.append("table:" + table + NEWLINE);
		result.append("noOfVersions:" + noOfVersions + NEWLINE);
		result.append("rowKey:" + rowKey + NEWLINE);
		result.append("fields:" + fields.toString() + NEWLINE);
		result.append("}");

		return result.toString();
	}

	public static class PARAMBUILDER {

		private int noOfVersions;
		private String table, columnFamily;
		private byte[] rowKey;
		private Set<Field> fields;

		public PARAMBUILDER noOfVersions(int noOfVersions) {
			this.noOfVersions = noOfVersions;
			return this;
		}

		public PARAMBUILDER table(String table) {
			this.table = table;
			return this;
		}

		public PARAMBUILDER rowKey(byte[] rowKey) {
			this.rowKey = rowKey;
			return this;
		}

		public PARAMBUILDER fields(Set<Field> fields) {
			this.fields = fields;
			return this;
		}

		public PARAMBUILDER columnFamily(String columnFamily) {
			this.columnFamily = columnFamily;
			return this;
		}

		public ReadPO build() {
			return new ReadPO(this);
		}

	}

}
