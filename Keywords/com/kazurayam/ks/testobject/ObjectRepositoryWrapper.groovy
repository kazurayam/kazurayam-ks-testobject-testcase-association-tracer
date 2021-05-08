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

	private List<Path> objectRepository

	ObjectRepositoryWrapper(Path repoDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Object Repository")) {
		objectRepository = Files.walk(repoDir)
		.filter {
			Path p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".rs")
		}
		.collect(Collectors.toList())
	}

	Boolean contains(String testObjectId) {
		List<String> candidates = objectRepository.findAll {
			Path p -> p.toString().contains(testObjectId)
		}
		return (candidates.size() > 0)
	}

}

