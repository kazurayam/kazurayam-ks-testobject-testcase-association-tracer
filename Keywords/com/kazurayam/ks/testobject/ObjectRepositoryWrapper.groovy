package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

/**
 *
 */
class ObjectRepositoryWrapper {

	private List<Path> rsFiles

	ObjectRepositoryWrapper(Path repoDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Object Repository")) {
		rsFiles = Files.walk(repoDir)
				.filter { Path p ->
					Files.isRegularFile(p) && p.getFileName().toString().endsWith(".rs")
				}
				.collect(Collectors.toList())
	}

	/**
	 * Supposing rsFiles contains
	 * - <projectDir>/Object Repository/Page_CURA Healthcare Service/a_Make Appointment.rs
	 * 
	 * Then,
	 * | Given testObjectId | result |
	 * | ------------------ | ------ |
	 * | Page_CURA Healthcare Service/a_Make Appointment | true |
	 * | Appointment | false |
	 * | foo_bar_baz | false |
	 * | Visit Date  | false |
	 * 
	 * @param testObjectId
	 * @return
	 */
	Boolean includes(String testObjectId) {
		String dirname = "Object Repository"
		String id = (!testObjectId.startsWith(dirname)) ? "${dirname}/${testObjectId}" : testObjectId
		List<String> candidates = rsFiles.findAll { Path p ->
			p.toString().contains(id)
		}
		println "testObjectId=\"${id}\", candidates=${candidates}"
		return (candidates.size() > 0)
	}
}

