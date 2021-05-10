package com.kazurayam.ks.testobject

import java.nio.file.Path

public interface Reporter {

	public String report()

	public Path getOutputDir()

	public String getOutputFilename()
	
	public void write()
}
