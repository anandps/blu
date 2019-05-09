package com.utils.blu.db;

import java.util.Properties;

import com.utils.blu.dal.HbaseDAL;
import com.utils.blu.exception.DBException;

public class DBFactory {

	private DBFactory() {
		// TODO Auto-generated constructor stub
	}

	private static DB createDBInstance(Properties props) throws DBException {
		DB dbinstance = HbaseDAL.getInstance();
		dbinstance.set_p(props);
		return dbinstance;
	}

	/**
	 * Generates instance to access database.
	 * 
	 * @param props
	 * @return
	 * @throws DBException
	 */
	public static DB getInstance(Properties props) throws DBException {
		return createDBInstance(props);
	}

	/**
	 * Generates instance to access database.
	 * 
	 * @return
	 * @throws DBException
	 */
	public static DB getInstance() throws DBException {
		return createDBInstance(new Properties());
	}

}
