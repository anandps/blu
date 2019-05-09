package com.utils.blu.schema;

import com.mapr.org.apache.hadoop.hbase.util.Bytes;
import com.utils.blu.constants.BLUConstants;
import com.utils.blu.constants.DataType;

public class Field {

	private String column, columnFamily, columnQualifier;
	private byte[] columnFamilyAsBytes, columnQualifierAsBytes;
	private DataType dataType;

	public Field() {
	}

	public Field(String columnFamily, String columnQualifier, DataType dataType) {
		this.columnFamily = columnFamily;
		this.columnQualifier = columnQualifier;
		this.dataType = dataType;
		setColumn();
	}

	public Field(byte[] columnFamilyAsBytes, byte[] columnQualifierAsBytes, DataType dataType) {
		this.columnFamilyAsBytes = columnFamilyAsBytes;
		this.columnQualifierAsBytes = columnQualifierAsBytes;
		this.dataType = dataType;
		setColumn();
	}

	public String getColumn() {
		return column;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public String getColumnQualifier() {
		return columnQualifier;
	}

	public DataType getDataType() {
		return dataType;
	}

	public byte[] getColumnFamilyAsBytes() {
		return columnFamilyAsBytes;
	}

	public byte[] getColumnQualifierAsBytes() {
		return columnQualifierAsBytes;
	}

	private void setColumn() {
		if ((this.columnFamily != null && !this.columnFamily.isEmpty())
				&& (this.columnQualifier != null && !this.columnQualifier.isEmpty())) {
			column = columnFamily + BLUConstants.DELIMITER_COLON + columnQualifier;
		} else if ((this.columnFamilyAsBytes != null && this.columnFamilyAsBytes.length > 0)
				&& (this.columnQualifierAsBytes != null && this.columnQualifierAsBytes.length > 0)) {
			column = Bytes.toString(columnFamilyAsBytes) + BLUConstants.DELIMITER_COLON
					+ Bytes.toString(columnQualifierAsBytes);
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEWLINE = System.lineSeparator();

		result.append(this.getClass().getName() + " Object {" + NEWLINE);
		result.append("column:" + column + NEWLINE);
		result.append("columnFamily:" + columnFamily + NEWLINE);
		result.append("columnQualifier:" + columnQualifier + NEWLINE);
		result.append("columnFamilyAsBytes:" + columnFamilyAsBytes + NEWLINE);
		result.append("columnQualifierAsBytes:" + columnQualifierAsBytes + NEWLINE);
		result.append("dataType:" + dataType.toString() + NEWLINE);
		result.append("}");

		return result.toString();
	}
}
