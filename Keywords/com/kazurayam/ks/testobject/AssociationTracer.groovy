package com.kazurayam.ks.testobject

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class AssociationTracer {

	private static AssociationTracer singleton = null;

	private final Set<Association> associations;

	private AssociationTracer() {
		associations = new HashSet<Association>();
	}

	public static AssociationTracer getInstance() {
		if (singleton == null) {
			singleton = new AssociationTracer();
		}
		return singleton;
	}

	public void trace(String caller, String callee) {
		if (caller == callee) {
			throw new IllegalArgumentException("caller and callee must not be equal");
		}
		//println "AssociationTracer::trace(\'${caller}\', \'${callee}\') was invoked"
		associations.add(new Association(caller, callee));
	}

	public List<String> calleesAll() {
		List<String> callees = new ArrayList<String>();
		for (Association assoc : associations) {
			callees.add(assoc.getCallee());
		}
		return callees.toSorted();
	}

	public List<String> callersAll() {
		List<String> callers = new ArrayList<String>();
		for (Association assoc : associations) {
			callers.add(assoc.getCaller());
		}
		return callers.toSorted();
	}

	public List<String> callersOf(String callee) {
		List<String> callers = new ArrayList<String>();
		for (Association assoc : associations) {
			if (assoc.getCallee() == callee) {
				callers.add(assoc.getCaller());
			}
		}
		return callers.toSorted();
	}

	public List<String> calleesBy(String caller) {
		List<String> callees = new ArrayList<String>();
		for (Association assoc : associations) {
			if (assoc.getCaller() == caller) {
				callees.add(assoc.getCallee());
			}
		}
		return callees.toSorted();
	}
}
