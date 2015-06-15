/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.config.lib;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.ops4j.pax.exam.Option;

public class Ops4JTestTime {


	public static Option[] getOptions(){
    	return  options(mavenBundle("org.fortiss.smartmicrogrid", "usermanager.api", "1.0-SNAPSHOT"),
    			mavenBundle("org.fortiss.smartmicrogrid", "ambulance.api", "1.0-SNAPSHOT"),
                mavenBundle("org.fortiss.smartmicrogrid", "remoteframework.lib", "1.0-SNAPSHOT"),
                mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", "2.3.0"), 
                mavenBundle("com.fasterxml.jackson.core", "jackson-core", "2.3.0"),
                 mavenBundle("com.fasterxml.jackson.core", "jackson-databind", "2.3.0"),
                 junitBundles(),
                 mavenBundle("org.hamcrest", "hamcrest-all", "1.3"),
                 mavenBundle("org.objenesis", "objenesis", "1.3"),
                 mavenBundle("org.mockito", "mockito-core", "1.9.5"));
	}
	

	
}
