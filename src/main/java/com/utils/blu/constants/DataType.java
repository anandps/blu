package com.utils.blu.constants;

public enum DataType {

	INT {
		public String toString() {
			return "int";
		}
	},
	FLOAT {
		public String toString() {
			return "float";
		}
	},
	DOUBLE {
		public String toString() {
			return "double";
		}
	},
	LONG {
		public String toString() {
			return "long";
		}
	},
	STRING {
		public String toString() {
			return "string";
		}
	},
	DATE {
		public String toString() {
			return "date";
		}
	},
	BOOLEAN {
		public String toString() {
			return "boolean";
		}
	},
	BYTES {
		public String toString() {
			return "bytes";
		}
	},
	OBJECT {
		public String toString() {
			return "object";
		}
	},
	CUSTOM {
		public String toString() {
			return "custom";
		}
	}

}
