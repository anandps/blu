package com.utils.blu.db;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.hadoop.hbase.util.Bytes;

import com.utils.blu.datastructures.DataMap;
import com.utils.blu.datastructures.DataStore;
import com.utils.blu.exception.DBException;
import com.utils.blu.exception.InsufficientArgumentException;
import com.utils.blu.exception.UnsupportedDataTypeException;
import com.utils.blu.exception.UnsupportedObjectException;
import com.utils.blu.po.DeletePO;
import com.utils.blu.po.InsertPO;
import com.utils.blu.po.ReadPO;
import com.utils.blu.po.ScanPO;

public abstract class DB {

	/**
	 * Properties to be set to pass to DAL (Data Access Layer)
	 */
	Properties _p = new Properties();

	public Properties get_p() {
		return _p;
	}

	public void set_p(Properties _p) {
		this._p = _p;
	}

	/**
	 * Initializes components required to access database.
	 * 
	 * @throws DBException
	 */
	public void init() throws DBException {

	}

	/**
	 * Releases resources held by the database instance. Application should take
	 * care of releasing the resources after being used.
	 * 
	 * @throws DBException
	 */
	public void cleanup() throws DBException {

	}

	/**
	 * To read data from DB.
	 * <p />
	 * Accepts input of type ReadPO - A read parameter
	 * object
	 * 
	 * @param readParams
	 * @return {@link DataMap}
	 * @throws DBException
	 * @throws InsufficientArgumentException
	 * @throws UnsupportedDataTypeException
	 * @throws IOException 
	 * @Note - Use Scan method if partial rowkey has to be used
	 * @Example read(new ReadPO.PARAMBUILDER().table("").build());
	 */
	public abstract DataMap read(ReadPO readParams)
			throws DBException, InsufficientArgumentException, UnsupportedDataTypeException, IOException;

	/**
	 * Scans the database and returns multiple records. Use getAllReecords() or
	 * getNextRecord() to retrive scanned records. 
	 * <p />
	 * Accepts input of type ScanPO
	 * - A scan parameter object
	 * 
	 * @param scanParams
	 * @throws DBException
	 * @throws InsufficientArgumentException
	 * @throws UnsupportedDataTypeException
	 * @throws IOException
	 * @throws UnsupportedObjectException 
	 * @Example scan(new ScanPO.PARAMBUILDER().table("").build());
	 */
	public abstract void scan(ScanPO scanParams)
			throws DBException, InsufficientArgumentException, UnsupportedDataTypeException, IOException, UnsupportedObjectException;

	/**
	 * This Method should be used in par with scan method. Returns true if next
	 * record is found else returns false.
	 * 
	 * @return {@link Boolean}
	 * @throws IOException
	 */
	public abstract boolean hasNextRecord() throws IOException;

	/**
	 * This Method should be used in par with scan method. Returns all the
	 * scanned records.
	 * 
	 * @return {@link DataStore}
	 * @throws UnsupportedDataTypeException
	 * @throws IOException
	 */
	public abstract DataStore getAllRecords() throws UnsupportedDataTypeException, IOException;

	/**
	 * This Method should be used in par with scan method and hasNextRecord()
	 * methods. Returns single record of type {@link DataMap}. Check for next
	 * record using hasNextRecord() method.
	 * 
	 * @return {@link DataMap}
	 * @throws UnsupportedDataTypeException
	 */
	public abstract DataMap getNextRecord() throws UnsupportedDataTypeException;

	/**
	 * Persists data into DB. 
	 * <p />
	 * Accepts input of type InsertPO - An insert
	 * parameter object
	 * 
	 * @param insertParams
	 * @throws Exception
	 * @Note Datastore should contain rowkey keyname as "rowKey".
	 * @Example insert(new InsertPO.PARAMBUILDER().table("").build());
	 */
	public abstract void insert(InsertPO insertParams) throws Exception;

	/**
	 * Deletes data from DB. 
	 * <p />
	 * Accepts input of type DeletePO - A delete parameter
	 * object
	 * 
	 * @param deleteParams
	 * @throws InsufficientArgumentException
	 * @throws DBException
	 * @Example delete(new DeletePO.PARAMBUILDER().table("").build());
	 */
	public abstract void delete(DeletePO deleteParams) throws InsufficientArgumentException, DBException;
	
	/** Return list of tables from DB.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract Set<String> list() throws IOException;

}
