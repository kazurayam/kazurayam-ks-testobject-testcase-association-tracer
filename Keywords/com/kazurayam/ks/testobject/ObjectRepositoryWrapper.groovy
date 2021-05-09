package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.regex.Pattern
import java.util.regex.Matcher
import com.kms.katalon.core.configuration.RunConfiguration

/**
 *
 */
class ObjectRepositoryWrapper {

	private Path repoDir
	private List<Path> rsFiles

	ObjectRepositoryWrapper(Path repoDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Object Repository")) {
		this.repoDir = repoDir
		rsFiles = Files.walk(this.repoDir)
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
		//println "testObjectId=\"${id}\", candidates=${candidates}"
		return (candidates.size() > 0)
	}

	/**
	 * 
	 * @return scans the "Object Repository" and returns a list of all "Test Object IDs" found there
	 */
	Set<String> getAllTestObjectIDs() {
		Set<String> list = new HashSet<String>()
		Pattern ptn = Pattern.compile('^(.+)\\.rs$')
		this.rsFiles.forEach { p ->
			String testObjectId = repoDir.relativize(p).toString()
			Matcher m = ptn.matcher(testObjectId)
			if (m.matches()) {
				list.add(m.group(1))
			} else {
				throw new IllegalStateException("testObjectId=\'${testObjectId}\' does not match regex ${ptn.toString()}")
			}
		}
		return list
	}
}

