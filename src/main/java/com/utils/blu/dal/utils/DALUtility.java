package com.utils.blu.dal.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Result;

import com.mapr.org.apache.hadoop.hbase.util.Bytes;
import com.utils.blu.constants.BLUConstants;
import com.utils.blu.datastructures.DataMap;
import com.utils.blu.exception.UnsupportedDataTypeException;
import com.utils.blu.schema.Field;

public class DALUtility {

	private static final NotificationLogger LOGGER = NotificationLoggerFactory.getLogger();

	private static final String LOGGING_CLASS_NAME = DALUtility.class.getName();

	/**
	 * Obtains columnFamily from the field object
	 * 
	 * @param field
	 * @return {@link byte[]}
	 */
	public static byte[] obtainColumnFamily(Field field) {

		if (field.getColumnFamily() != null && !field.getColumnFamily().isEmpty()) {
			return Bytes.toBytes(field.getColumnFamily());
		} else
			return field.getColumnFamilyAsBytes();

	}

	/**
	 * Obtains columnQualifier from the field object
	 * 
	 * @param field
	 * @return {@link byte[]}
	 */
	public static byte[] obtainColumnQualifier(Field field) {

		if (field.getColumnQualifier() != null && !field.getColumnQualifier().isEmpty()) {
			return Bytes.toBytes(field.getColumnQualifier());
		} else
			return field.getColumnQualifierAsBytes();

	}

	/**
	 * Converts given field value to bytes.
	 * 
	 * @param field
	 * @param data
	 * @return
	 * @throws UnsupportedDataTypeException
	 */
	public static byte[] getFieldValueAsBytes(Field field, DataMap data) throws UnsupportedDataTypeException {

		String column = field.getColumn();
		byte[] retVal = null;

		if (data.containsKey(column)) {

			try {
				switch (field.getDataType()) {
				case INT:
					Integer _i = null;
					_i = (Integer) data.getAsNumber(column);

					if (_i != null)
						retVal = Bytes.toBytes(_i);

					break;

				case LONG:
					Long _l = null;
					_l = (Long) data.getAsNumber(column);

					if (_l != null)
						retVal = Bytes.toBytes(_l);
					break;

				case DOUBLE:
					Double _d = null;
					_d = (Double) data.getAsNumber(column);

					if (_d != null)
						retVal = Bytes.toBytes(_d);
					break;

				case BOOLEAN:
					Boolean _b = null;
					_b = data.getAsBoolean(column);

					if (_b != null)
						retVal = Bytes.toBytes(_b);
					break;

				case STRING:
					String _s = null;
					_s = data.getAsString(column);

					if (_s != null)
						retVal = Bytes.toBytes(_s);
					break;

				case BYTES:
					retVal = data.getAsByteArray(column);
					break;

				default:
					throw new UnsupportedDataTypeException("Unsupported data type for a field: " + column);
				}

			} catch (Exception e) {
				// TODO: handle exception
				throw new UnsupportedDataTypeException("Unsupported data type for a field: " + column, e);
			}
		}

		return retVal;

	}

	/**
	 * converts field value from bytearray to field's datatype
	 * 
	 * @param field
	 * @param value
	 * @return
	 * @throws UnsupportedDataTypeException
	 */
	public static Object getFieldValueFromByteArray(Field field, byte[] value) throws UnsupportedDataTypeException {

		Object retVal = null;
		try {
			switch (field.getDataType()) {
			case INT:
				retVal = Bytes.toInt(value);
				break;

			case DOUBLE:
				retVal = Bytes.toDouble(value);
				break;

			case LONG:
				retVal = Bytes.toLong(value);
				break;

			case BOOLEAN:
				retVal = Bytes.toBoolean(value);
				break;

			case STRING:
				retVal = Bytes.toString(value);
				break;

			case BYTES:
				retVal = value;
				break;
				
			case CUSTOM:
				retVal = value;
				break;

			default:
				throw new UnsupportedDataTypeException("Unsupported data type for a field: " + field.getColumn());
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new UnsupportedDataTypeException("Unsupported data type for a field: " + field.getColumn(), e);
		}

		return retVal;

	}

	/**
	 * Forms a datamap from the set of fields , which is equivalent to one
	 * record.
	 * <p />
	 * If noOfVersions is provided, then the single column key contains list of
	 * values with latest version as first item in the list
	 * 
	 * @param fields
	 * @param result
	 * @param data
	 * @param noOfVersions
	 * @return {@link DataMap}
	 * @throws UnsupportedDataTypeException
	 */
	public static DataMap putDataInDataMap(Set<Field> fields, Result result, DataMap data, int noOfVersions)
			throws UnsupportedDataTypeException {

		String columnFamily, columnQualifier;

		for (Field field : fields) {
			columnFamily = field.getColumnFamily();
			columnQualifier = field.getColumnQualifier();

			if (result.containsColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier))) {
				if (noOfVersions == 1) {
					Cell cell = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier));
					data.put(field.getColumn(),
							DALUtility.getFieldValueFromByteArray(field, CellUtil.cloneValue(cell)));
				} else {
					List<Object> values = new ArrayList<>();
					List<Cell> cells = result.getColumnCells(Bytes.toBytes(columnFamily),
							Bytes.toBytes(columnQualifier));
					
					if (cells.size() > noOfVersions) {
						cells = cells.subList(0, noOfVersions - 1);
					}
					for (Cell versionedCell : cells) {
						values.add(DALUtility.getFieldValueFromByteArray(field, CellUtil.cloneValue(versionedCell)));
					}
					data.put(field.getColumn(), values);
				}

			}

		}
		return data;
	}

	/**
	 * Forms a datamap from all the qualifiers of a family , which is equivalent
	 * to one record
	 * <p />
	 * If noOfVersions is provided, then the single column key contains list of
	 * values with latest version as first item in the list
	 * 
	 * @param columnFamily
	 * @param result
	 * @param data
	 * @param noOfVersions
	 * @return
	 */
	public static DataMap putDataInDataMap(String columnFamily, Result result, DataMap data, int noOfVersions) {

		String key;
		NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyMap = result.getMap();
		if (noOfVersions == 1) {
			
			for (Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyEntry : familyMap.entrySet()) {
				String family = Bytes.toString(familyEntry.getKey());
				if (!columnFamily.equals(family)) {
					continue;
				}
				for (Entry<byte[], NavigableMap<Long, byte[]>> qualifierEntry : familyEntry.getValue().entrySet()) {
					key = columnFamily + BLUConstants.DELIMITER_COLON + Bytes.toString(qualifierEntry.getKey());
					data.put(key, qualifierEntry.getValue().firstEntry().getValue());
				}
			}
			
			/*while (result.advance()) {
				final Cell cell = result.current();
				key = columnFamily + BLUConstants.DELIMITER_COLON + Bytes.toString(CellUtil.cloneQualifier(cell));
				data.put(key, CellUtil.cloneValue(cell));
			}*/
		} else {
			List<Object> values = new ArrayList<>();

			for (Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyEntry : familyMap.entrySet()) {
				for (Entry<byte[], NavigableMap<Long, byte[]>> qualifierEntry : familyEntry.getValue().entrySet()) {
					key = columnFamily + BLUConstants.DELIMITER_COLON + Bytes.toString(qualifierEntry.getKey());
					for (Entry<Long, byte[]> valueEntry : qualifierEntry.getValue().descendingMap().entrySet()) {
						values.add(valueEntry.getValue());
						if (values.size() == noOfVersions) {
							break;
						}
					}
					data.put(key, values);
				}
			}
		}

		return data;
	}

	/**
	 * Creates table with the given schema file.
	 * 
	 * @param schemaDefFileName
	 * @throws ConfigurationException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void createTableUsingSchema(String schemaDefFileName)
			throws ConfigurationException, IOException, InterruptedException {
		HBaseSchemaCreator schemaCreator = new HBaseSchemaCreator(schemaDefFileName);
		try {
			schemaCreator.createSchema();
		} catch (TableExistsException e) {
			// TODO Auto-generated catch block
			// Do nothing
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Table already exists.. Reusing the same...");
		}
	}
}
