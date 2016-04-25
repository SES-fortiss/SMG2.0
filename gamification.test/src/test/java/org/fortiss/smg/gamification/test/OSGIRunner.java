/**
 * 
 */
package org.fortiss.smg.gamification.test;

import org.osgi.framework.BundleActivator;

/**
 * @author Pahlke
 *
 */
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
