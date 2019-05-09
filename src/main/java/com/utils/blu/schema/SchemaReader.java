package com.utils.blu.schema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.blu.constants.BLUConstants;
import com.utils.blu.constants.DataType;
import com.utils.blu.exception.SchemaReaderException;
import com.fasterxml.jackson.core.type.TypeReference;

public class SchemaReader {

	private static ObjectMapper objMap = new ObjectMapper();
	private static Map<Object, Object> schemaMap;

	/**
	 * Gets the schema in list of fields
	 * 
	 * @param jsonAsFile
	 * @param usage
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws SchemaReaderException
	 */
	@SuppressWarnings("unchecked")
	public static Set<Field> getSchemaFields(File jsonAsFile, String moduleMarker) throws SchemaReaderException {
		try {
			schemaMap = objMap.readValue(jsonAsFile, new TypeReference<Map<Object, Object>>() {
			});
			Set<Field> schemaOutput = new HashSet<>();
			for (Map<Object, Object> columnMap : (List<Map<Object, Object>>) schemaMap.get(BLUConstants.FIELDS)) {

				if (((String) columnMap.get(BLUConstants.MODULE_NAME)).equalsIgnoreCase(moduleMarker)
						|| ((String) columnMap.get(BLUConstants.MODULE_NAME)).equalsIgnoreCase(BLUConstants.ALL)) {
					String[] columnFamilyAndQualifier = columnMap.get(BLUConstants.COLUMN).toString()
							.split(BLUConstants.DELIMITER_COLON);
					Field field = new Field(columnFamilyAndQualifier[0], columnFamilyAndQualifier[1],
							DataType.valueOf(columnMap.get(BLUConstants.DATATYPE).toString().toUpperCase()));
					schemaOutput.add(field);
				}
			}
			return schemaOutput;
		} catch (Exception ex) {
			throw new SchemaReaderException("Exception while reading schema for : " + jsonAsFile, ex);
		}
	}
}
