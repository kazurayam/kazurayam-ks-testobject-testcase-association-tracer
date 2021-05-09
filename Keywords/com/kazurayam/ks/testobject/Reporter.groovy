package com.kazurayam.ks.testobject

import java.nio.file.Path

public interface Reporter {

	public void report()

	public Path getOutputDir()
}
