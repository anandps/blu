package com.utils.blu.constants;

public interface BLUConstants {

	String ROWKEY = "rowKey";
	String DELIMITER_COLON = ":";

	// Json keys in schema (Used in Json schema parsing)
	String FIELDS = "fields";
	String MODULE_NAME = "module";
	String ALL = "ALL";
	String COLUMN = "column";
	String DATATYPE = "datatype";

	// Exception messages
	String EMPTY_TABLE = "Table name is empty.";
	String EMPTY_ROWKEY = "Row key is empty.";
	String EMPTY_FIELDSET = "Field set is empty.";
	String EMPTY_STARTKEY = "Start rowkey is empty.";
	String EMPTY_COLFAM_AND_FIELDSET = "ColumnFamily and Field set is empty.";
	String EMPTY_DATASTORE = "Datastore is empty.";
	String EMPTY_ROWKEY_AND_ROWKEYLIST = "Rowkey and rowkey list are empty.";
}
