package com.kazurayam.ks.testobject

import java.util.HashSet;
import java.util.Set;

public class Association {

	private final String caller;
	private final String callee;

	public Association(String caller, String  callee) {
		this.caller = caller;
		this.callee = callee;
	}

	public String getCaller() {
		return caller;
	}

	public String getCallee() {
		return callee;
	}

	@Override
	public boolean equals(Object o) {
		/* Check if o is an instance of Complex or not
		 "null instanceof [type]" also returns false */
		if (!(o instanceof Association)) {
			return false;
		}

		// typecast o to Complex so that we can compare data members
		Association other = (Association) o;

		// Compare the data members and return accordingly
		return this.getCaller().equals(other.getCaller()) &&
				this.getCallee().equals(other.getCallee());
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.getCaller().hashCode();
		result = 31 * result + this.getCallee().hashCode();
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"caller\"");
		sb.append("\"");
		sb.append(caller);
		sb.append("\"");
		sb.append(":");
		sb.append("\"callee\"");
		sb.append("\"");
		sb.append(callee);
		sb.append("\"");
		sb.append("}");
		return sb.toString();
	}
}
