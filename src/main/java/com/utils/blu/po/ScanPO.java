package com.utils.blu.po;

import java.util.Set;

import com.utils.blu.schema.Field;

/**
 * Constructs parameter object for scan operation
 * 
 * @author eanpart
 *
 */
public class ScanPO {

	private int noOfVersions;
	private String table;
	private Set<String> columnFamilies;
	private byte[] startKey, endKey;
	private Set<Field> fields;
	private Object filters;

	private ScanPO(PARAMBUILDER builder) {
		// TODO Auto-generated constructor stub
		this.table = builder.table;
		this.startKey = builder.startKey;
		this.endKey = builder.endKey;
		this.fields = builder.fields;
		this.noOfVersions = builder.noOfVersions;
		this.filters = builder.filters;
		this.columnFamilies = builder.columnFamilies;
	}

	public int getNoOfVersions() {
		return noOfVersions;
	}

	public String getTable() {
		return table;
	}

	public Set<String> getColumnFamilies() {
		return columnFamilies;
	}

	public byte[] getStartKey() {
		return startKey;
	}

	public byte[] getEndKey() {
		return endKey;
	}

	public Set<Field> getFields() {
		return fields;
	}

	public Object getFilters() {
		return filters;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEWLINE = System.lineSeparator();

		result.append(this.getClass().getName() + " Object {" + NEWLINE);
		result.append("table:" + table + NEWLINE);
		result.append("columnFamilies:" + columnFamilies + NEWLINE);
		result.append("startKey:" + startKey + NEWLINE);
		result.append("endKey:" + endKey + NEWLINE);
		result.append("noOfVersions:" + noOfVersions + NEWLINE);
		result.append("fields:" + fields.toString() + NEWLINE);
		result.append("}");

		return result.toString();
	}

	public static class PARAMBUILDER {

		private int noOfVersions;
		private String table;
		private byte[] startKey, endKey;
		private Set<Field> fields;
		private Set<String> columnFamilies;
		private Object filters;

		public PARAMBUILDER noOfVersions(int noOfVersions) {
			this.noOfVersions = noOfVersions;
			return this;
		}

		public PARAMBUILDER table(String table) {
			this.table = table;
			return this;
		}

		public PARAMBUILDER startKey(byte[] startKey) {
			this.startKey = startKey;
			return this;
		}

		public PARAMBUILDER endKey(byte[] endKey) {
			this.endKey = endKey;
			return this;
		}

		public PARAMBUILDER fields(Set<Field> fields) {
			this.fields = fields;
			return this;
		}
		
		public PARAMBUILDER columnFamilies(Set<String> columnFamilies) {
			this.columnFamilies = columnFamilies;
			return this;
		}

		public PARAMBUILDER filters(Object filters) {
			this.filters = filters;
			return this;
		}

		public ScanPO build() {
			return new ScanPO(this);
		}
	}

}
