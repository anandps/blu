package com.utils.blu.po;

import java.util.Set;

/**
 * Constructs parameter object for delete operation
 * 
 * @author eanpart
 *
 */
public class DeletePO {

	private String table;
	private byte[] rowKey;
	private Set<byte[]> rowKeyList;

	private DeletePO(PARAMBUILDER builder) {
		// TODO Auto-generated constructor stub
		this.table = builder.table;
		this.rowKey = builder.rowKey;
		this.rowKeyList = builder.rowKeyList;
	}

	public String getTable() {
		return table;
	}

	public byte[] getRowKey() {
		return rowKey;
	}

	public Set<byte[]> getRowKeyList() {
		return rowKeyList;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEWLINE = System.lineSeparator();

		result.append(this.getClass().getName() + " Object {" + NEWLINE);
		result.append("table:" + table + NEWLINE);
		result.append("rowKey:" + rowKey + NEWLINE);
		result.append("rowKeyList:" + rowKeyList.toString() + NEWLINE);
		result.append("}");

		return result.toString();
	}

	public static class PARAMBUILDER {

		private String table;
		private byte[] rowKey;
		private Set<byte[]> rowKeyList;

		public PARAMBUILDER table(String table) {
			this.table = table;
			return this;
		}

		public PARAMBUILDER rowKey(byte[] rowKey) {
			this.rowKey = rowKey;
			return this;
		}

		public PARAMBUILDER rowKeyList(Set<byte[]> rowKeyList) {
			this.rowKeyList = rowKeyList;
			return this;
		}

		public DeletePO build() {
			return new DeletePO(this);
		}

	}

}
