package com.utils.blu.po;

import java.util.Set;

import com.utils.blu.datastructures.DataStore;
import com.utils.blu.schema.Field;

/**
 * Constructs parameter object for insert operation
 * 
 * @author eanpart
 *
 */
public class InsertPO {

	private String table, versioning, configFileDescriptor;
	private DataStore datastore;
	private Set<Field> fields;

	public InsertPO(PARAMBUILDER builder) {
		// TODO Auto-generated constructor stub
		this.table = builder.table;
		this.versioning = builder.versioning;
		this.configFileDescriptor = builder.configFileDescriptor;
		this.datastore = builder.datastore;
		this.fields = builder.fields;
	}

	public String getTable() {
		return table;
	}

	public String getVersioning() {
		return versioning;
	}

	public String getConfigFileDescriptor() {
		return configFileDescriptor;
	}

	public DataStore getDatastore() {
		return datastore;
	}

	public Set<Field> getFields() {
		return fields;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEWLINE = System.lineSeparator();

		result.append(this.getClass().getName() + " Object {" + NEWLINE);
		result.append("table:" + table + NEWLINE);
		result.append("versioning:" + versioning + NEWLINE);
		result.append("configFileDescriptor:" + configFileDescriptor + NEWLINE);
		result.append("datastore:" + datastore.toString() + NEWLINE);
		result.append("fields:" + fields.toString() + NEWLINE);
		result.append("}");

		return result.toString();
	}

	public static class PARAMBUILDER {

		private String table, versioning, configFileDescriptor;
		private DataStore datastore;
		private Set<Field> fields;

		public PARAMBUILDER table(String table) {
			this.table = table;
			return this;
		}

		public PARAMBUILDER versioning(String versioning) {
			this.versioning = versioning;
			return this;
		}

		public PARAMBUILDER configFileDescriptor(String configFileDescriptor) {
			this.configFileDescriptor = configFileDescriptor;
			return this;
		}

		public PARAMBUILDER datastore(DataStore datastore) {
			this.datastore = datastore;
			return this;
		}

		public PARAMBUILDER fields(Set<Field> fields) {
			this.fields = fields;
			return this;
		}

		public InsertPO build() {
			return new InsertPO(this);
		}
	}

}
