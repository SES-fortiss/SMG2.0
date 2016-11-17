package org.fortiss.smg.actuatorclient.ipswitch.test;

import org.fortiss.smg.informationbroker.impl.InformationBrokerActivator;
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
