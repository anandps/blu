package com.utils.blu.dal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;

import com.mapr.org.apache.hadoop.hbase.util.Bytes;
import com.utils.blu.constants.BLUConstants;
import com.utils.blu.dal.utils.DALUtility;
import com.utils.blu.datastructures.DataMap;
import com.utils.blu.datastructures.DataStore;
import com.utils.blu.db.DB;
import com.utils.blu.exception.DBException;
import com.utils.blu.exception.InsufficientArgumentException;
import com.utils.blu.exception.UnsupportedDataTypeException;
import com.utils.blu.exception.UnsupportedObjectException;
import com.utils.blu.po.DeletePO;
import com.utils.blu.po.InsertPO;
import com.utils.blu.po.ReadPO;
import com.utils.blu.po.ScanPO;
import com.utils.blu.schema.Field;

public class HbaseDAL extends DB {

	private static final NotificationLogger LOGGER = NotificationLoggerFactory.getLogger();

	private static final String LOGGING_CLASS_NAME = HbaseDAL.class.getName();

	/**
	 * Hbase connection instance
	 */
	private static Connection connection = null;

	/**
	 * Lock object for connection
	 */
	private static final Object LOCK = new Object();

	/**
	 * Caches Hbase table instance
	 */
	private Table currentTable;

	/**
	 * Caches result instance for scan operations
	 */
	private Result resultCache;

	/**
	 * Caches scanned data to serve client
	 */
	private ResultScanner scannerCache;

	/**
	 * Caches selected scan parameters for intermittent scan operations
	 */
	private ScanPO scanParameterCache;

	/**
	 * Caches table name - decides whether to create new table instance or not
	 */
	private String tableName = "";

	// private long writeBufferSize = 1024 * 100;

	private HbaseDAL() throws DBException {
		// TODO Auto-generated constructor stub
		init();
	}

	public static HbaseDAL getInstance() throws DBException {
		return new HbaseDAL();
	}

	/*
	 * Initializes components required for the DAL ( Data Access Layer)
	 */
	@Override
	public void init() throws DBException {

		/*
		 * LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG,
		 * "Inside HbaseDAL init"); if (get_p().containsKey("writebuffersize"))
		 * { writeBufferSize =
		 * Long.parseLong(get_p().getProperty("writebuffersize")); }
		 */

		try {
			synchronized (LOCK) {
				if (connection == null) {
					LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.INFO, "Creating new HBASE connection");
					connection = ConnectionFactory.createConnection(HBaseConfiguration.create());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBException("Problem in creating Hbase connection.", e);
		}

	}

	/*
	 * Releases resources held by the DAL ( Data Access Layer)
	 */
	@Override
	public void cleanup() throws DBException {

		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.INFO,
				"Releasing table and connection used for the application.");
		try {
			// cleaning up hbase resources
			if (currentTable != null) {
				LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.INFO, "Closing Hbase table");
				currentTable.close();
			}

			synchronized (LOCK) {
				if (connection != null && !connection.isClosed()) {
					LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.INFO, "Closing Hbase connection");
					connection.close();
				}
			}

		} catch (IOException e) {
			// TODO: handle exception
			throw new DBException("Problem while releasing hbase resources", e);
		}

	}

	private DataMap readDataFromHbase(ReadPO readParams)
			throws DBException, InsufficientArgumentException, UnsupportedDataTypeException, IOException {
		// TODO Auto-generated method stub

		// Releasing previously held scanned cache
		releaseScanResources();

		// Declarations
		Result result;
		DataMap data = null;

		// Extractions from PO (Parameter Objects)
		int noOfVersions = readParams.getNoOfVersions();
		String table = readParams.getTable();
		String columnFamily = readParams.getColumnFamily();
		byte[] rowKey = readParams.getRowKey();
		Set<Field> fields = readParams.getFields();

		// Preliminary checks
		if (table == null || table.isEmpty()) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_TABLE);
		}

		if (rowKey == null || rowKey.length == 0) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_ROWKEY);
		}

		if (columnFamily == null && (fields == null || fields.size() == 0)) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_COLFAM_AND_FIELDSET);
		}

		if (noOfVersions == 0) {
			noOfVersions = 1;
		}

		if (!tableName.equals(table)) {
			try {
				getHtable(table);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new DBException("Problem in getting table:" + table, e);
			}
		}

		Get get = new Get(rowKey);
		// Setting no of versions
		get.setMaxVersions(noOfVersions);

		if (fields != null && fields.size() > 0) {
			for (Field field : fields) {
				get.addColumn(Bytes.toBytes(field.getColumnFamily()), Bytes.toBytes(field.getColumnQualifier()));
			}
		} else if (columnFamily != null) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Reading using columnfamily alone");
			get.addFamily(Bytes.toBytes(columnFamily));
		}

		try {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Reading data from hbase");
			result = currentTable.get(get);

			if (result != null && !result.isEmpty()) {
				LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Populating obtained result into the map");
				data = new DataMap();
				if (fields != null && fields.size() > 0) {
					data = DALUtility.putDataInDataMap(fields, result, data, noOfVersions);
				} else {
					data = DALUtility.putDataInDataMap(columnFamily, result, data, noOfVersions);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBException("Problem while reading data from hbase.", e);
		}

		return data;

	}

	@Override
	public DataMap read(ReadPO readParams)
			throws DBException, InsufficientArgumentException, UnsupportedDataTypeException, IOException {
		return readDataFromHbase(readParams);
	}

	private void scanDataInHbase(ScanPO scanParams) throws InsufficientArgumentException, DBException, IOException,
			UnsupportedDataTypeException, UnsupportedObjectException {
		// TODO Auto-generated method stub

		// Releasing previously held scanned cache
		releaseScanResources();

		// Declarations
		int startKeyLength;
		Scan scan;
		FilterList filters;

		// Extractions from PO (Parameter Objects)
		int noOfVersions = scanParams.getNoOfVersions();
		String table = scanParams.getTable();
		byte[] startKey = scanParams.getStartKey();
		byte[] endKey = scanParams.getEndKey();
		Set<Field> fields = scanParams.getFields();
		Set<String> columnFamilies = scanParams.getColumnFamilies();

		// Preliminary checks
		if (table == null || table.isEmpty()) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_TABLE);
		}

		if ((columnFamilies == null || columnFamilies.size() == 0) && (fields == null || fields.size() == 0)) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_COLFAM_AND_FIELDSET);
		}

		if (noOfVersions == 0) {
			noOfVersions = 1;
		}

		// Extracting filter parameter object
		try {
			filters = (FilterList) scanParams.getFilters();
		} catch (ClassCastException e) {
			// TODO: handle exception
			throw new UnsupportedObjectException("Problem in extracting filter paramter object", e);
		}

		if (!tableName.equals(table)) {
			try {
				getHtable(table);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new DBException("Problem in getting table:" + table, e);
			}
		}

		// Formulating endKey, if not given: Last bit in the byte array is
		// incremented
		// to stop the scan
		if (startKey !=  null && startKey.length>0) {
			if (endKey == null || endKey.length == 0) {
				LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG,
						"Formulating endKey, since din't recieve endKey for scan");
				startKeyLength = startKey.length;
				endKey = new byte[startKeyLength];
				System.arraycopy(startKey, 0, endKey, 0, startKeyLength);
				endKey[startKeyLength - 1] = (byte) (endKey[startKeyLength - 1] + 1);
			}

			// Setting startKey & endKey to the scan object
			scan = new Scan(startKey, endKey);
		}else{
			scan = new Scan();
		}
		
		// Setting no of versions
		scan.setMaxVersions(noOfVersions);

		// If both field set & columnFamily is given, by default takes field set
		if (fields != null && fields.size() > 0) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Scanning using field set");
			for (Field field : fields) {
				scan.addColumn(Bytes.toBytes(field.getColumnFamily()), Bytes.toBytes(field.getColumnQualifier()));
			}
		} else if (columnFamilies != null && !columnFamilies.isEmpty()) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Scanning using set of column families");
			System.out.println("column family set>>" + columnFamilies.toString());
			for (String family : columnFamilies) {
				scan.addFamily(Bytes.toBytes(family));
			}
		}

		// Applying filters
		if (scanParams.getFilters() != null) {
			scan.setFilter(filters);
		}

		// Scanning hbase
		try {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Scanning HBASE");
			// Caching the scanner instance & scan parameters (specific
			// parameters) for future retrievals
			scannerCache = currentTable.getScanner(scan);
			scanParameterCache = new ScanPO.PARAMBUILDER().columnFamilies(columnFamilies).noOfVersions(noOfVersions)
					.fields(fields).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBException("Problem while reading data from hbase.", e);
		}

	}

	@Override
	public boolean hasNextRecord() throws IOException {
		if (scannerCache != null) {
			if ((resultCache = scannerCache.next()) != null) {
				return true;
			}
		}
		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "No data in scanner cache!!");
		return false;
	}

	@Override
	public DataMap getNextRecord() throws UnsupportedDataTypeException {
		if (resultCache == null) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "No data in result cache!!");
			return null;
		}
		return extractDataFromResult(resultCache);
	}

	@Override
	public DataStore getAllRecords() throws UnsupportedDataTypeException, IOException {
		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Getting and returning all data in scanner cache");
		DataStore scannedDataStore = null;

		if (scannerCache != null) {
			scannedDataStore = new DataStore();
			Iterator<Result> resultIterator = scannerCache.iterator();
			while (resultIterator.hasNext()) {
				scannedDataStore.setDataMapList(extractDataFromResult(resultIterator.next()));
			}

			// cleaning
			scannerCache = null;
		}

		return scannedDataStore;
	}

	@Override
	public void scan(ScanPO scanParams) throws DBException, InsufficientArgumentException, UnsupportedDataTypeException,
			IOException, UnsupportedObjectException {
		scanDataInHbase(scanParams);
	}

	private void insertDataToHbase(InsertPO insertParams) throws Exception {
		// TODO Auto-generated method stub

		// Releasing previously held scanned cache
		releaseScanResources();

		// Declarations
		Put put = null;
		List<Put> puts = null;
		long versioningTimestatmp = 0;
		byte[] value, rowKey;
		boolean batchPut = false;

		// Extractions from PO (Parameter Objects)
		String table = insertParams.getTable();
		String versioning = insertParams.getVersioning();
		String configFileDescriptor = insertParams.getConfigFileDescriptor();
		DataStore data = insertParams.getDatastore();
		Set<Field> fields = insertParams.getFields();

		// Preliminary checks
		if (table == null || table.isEmpty()) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_TABLE);
		}

		if (data == null || data.size() == 0) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_DATASTORE);
		}

		if ((fields == null || fields.size() == 0)) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_FIELDSET);
		}

		if (configFileDescriptor != null && !configFileDescriptor.isEmpty()) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Creating new table using schema");
			DALUtility.createTableUsingSchema(configFileDescriptor);
		}
		if (!tableName.equals(table)) {
			try {
				getHtable(table);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new DBException("Problem in getting table:" + table, e);
			}
		}

		if (data.getDataMaps().size() > 1) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Forming batch put");
			batchPut = true;
			puts = new ArrayList<>();
		}

		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Iterating through datastore");
		for (DataMap dataMap : data.getDataMaps()) {
			if (!dataMap.containsKey(BLUConstants.ROWKEY))
				throw new InsufficientArgumentException("Record has no rowkey.");
			else
				rowKey = dataMap.getAsByteArray(BLUConstants.ROWKEY);

			if (dataMap.containsKey(versioning))
				versioningTimestatmp = Long.parseLong(dataMap.getAsString(versioning));

			put = new Put(rowKey);
			for (Field field : fields) {
				value = DALUtility.getFieldValueAsBytes(field, dataMap);

				if (value != null) {
					if (versioningTimestatmp > 0)
						put.addColumn(DALUtility.obtainColumnFamily(field), DALUtility.obtainColumnQualifier(field),
								versioningTimestatmp, value);
					else {
						put.addColumn(DALUtility.obtainColumnFamily(field), DALUtility.obtainColumnQualifier(field),
								value);
					}
				}
			}
			if (batchPut) {
				puts.add(put);
			}
		}

		try {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Persisting data in hbase");
			if (batchPut)
				currentTable.put(puts);
			else
				currentTable.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBException("Problem while persisting data into Hbase.", e);
		}

	}

	@Override
	public void insert(InsertPO insertParams) throws Exception {
		// TODO Auto-generated method stub

		insertDataToHbase(insertParams);
	}

	private void deleteDataFromHbase(DeletePO deleteParams) throws InsufficientArgumentException, DBException {
		// TODO Auto-generated method stub

		// Releasing previously held scanned cache
		releaseScanResources();

		// Declarations
		Delete delete = null;
		List<Delete> deletes = null;
		boolean batchDelete = false;

		// Extractions from PO (Parameter Objects)
		String table = deleteParams.getTable();
		byte[] rowKey = deleteParams.getRowKey();
		Set<byte[]> rowkeyList = deleteParams.getRowKeyList();

		// Preliminary checks
		if (table == null || table.isEmpty()) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_TABLE);
		}

		if ((rowKey == null || rowKey.length == 0) && (rowkeyList == null || rowkeyList.isEmpty())) {
			throw new InsufficientArgumentException(BLUConstants.EMPTY_ROWKEY_AND_ROWKEYLIST);
		}

		if (!tableName.equals(table)) {
			try {
				getHtable(table);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new DBException("Problem in getting table:" + table, e);
			}
		}

		if (rowkeyList != null && rowkeyList.size() > 1) {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Bulk delete operations");
			deletes = new ArrayList<>();
			batchDelete = true;

			for (byte[] key : rowkeyList) {
				delete = new Delete(key);
				deletes.add(delete);
			}
		} else {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Single delete operation");
			delete = new Delete(rowKey);
		}

		try {
			LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Deleting data from table:" + table);
			if (batchDelete) {
				currentTable.delete(deletes);
			} else {
				currentTable.delete(delete);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBException("Problem while deleting data in Hbase.", e);
		}

	}

	@Override
	public void delete(DeletePO deleteParams) throws InsufficientArgumentException, DBException {
		deleteDataFromHbase(deleteParams);
	}

	@Override
	public Set<String> list() throws IOException {

		Set<String> tableList = new HashSet<>();
		HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
		HTableDescriptor[] tables = admin.listTables();

		// Collecting table names
		for (int i = 0; i < tables.length; i++) {
			tableList.add(tables[i].getNameAsString());
		}

		return tableList;
	}

	private DataMap extractDataFromResult(Result result) throws UnsupportedDataTypeException {

		DataMap record = new DataMap();
		record.put(BLUConstants.ROWKEY, result.getRow());
		if (scanParameterCache.getFields() != null && scanParameterCache.getFields().size() > 0) {
			record = DALUtility.putDataInDataMap(scanParameterCache.getFields(), result, record,
					scanParameterCache.getNoOfVersions());
		} else if (scanParameterCache.getColumnFamilies() != null && scanParameterCache.getColumnFamilies().size() > 0) { 
			for(String columnFamily : scanParameterCache.getColumnFamilies()){
				record.putAll(DALUtility.putDataInDataMap(columnFamily, result, record,
						scanParameterCache.getNoOfVersions()));
			}
		}

		return record;
	}

	private void getHtable(String table) throws IOException {
		currentTable = null;

		TableName tableNameObj = TableName.valueOf(table);
		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG,
				"Creating new Table instance with table name:" + tableNameObj.getNameAsString());
		try {
			this.currentTable = connection.getTable(tableNameObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Retrying one more time to get the table instance
			this.currentTable = connection.getTable(tableNameObj);
		}
		this.tableName = table;
	}

	private void releaseScanResources() {

		LOGGER.postNotification(LOGGING_CLASS_NAME, LogLevel.DEBUG, "Releasing scan resources.");

		this.resultCache = null;
		this.scanParameterCache = null;
		if (this.scannerCache != null) {
			scannerCache.close();
			scannerCache = null;
		}

	}

}
