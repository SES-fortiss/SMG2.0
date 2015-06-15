/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.containermanager.test;

import org.osgi.framework.BundleActivator;

public class OSGIRunner implements Runnable {

	private FakedBundleContext context;
	private BundleActivator act;


	public OSGIRunner(FakedBundleContext context, BundleActivator act) {
		this.context = context;
		this.act = act;
	}


	@Override
	public void run() {
		try {
			act.start(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
