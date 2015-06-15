/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.prophet.impl;import org.fortiss.smg.prophet.api.ProphetInterface;import org.fortiss.smg.prophet.api.ProphetQueueNames;import org.osgi.framework.BundleActivator;import org.osgi.framework.BundleContext;import org.osgi.framework.ServiceRegistration;import org.fortiss.smg.remoteframework.lib.DefaultServer;import org.slf4j.Logger;import org.slf4j.LoggerFactory;public class ProphetActivator implements BundleActivator {    DefaultServer<ProphetInterface> server;    ProphetImpl impl;    // Logger from sl4j    private static Logger logger = LoggerFactory.getLogger(ProphetActivator.class);    @Override    public void start(BundleContext context) throws Exception {        // register here your services etc.        // DO NOT start heavy operations here - use threads        impl = new ProphetImpl();        server = new DefaultServer<ProphetInterface>(ProphetInterface.class, impl, ProphetQueueNames.getProphetInterfaceQueue());        server.init();        logger.info("Prophet is alive");    }    @Override    public void stop(BundleContext context) throws Exception {        // REMEMBER to destroy all resources, threads and do cleanup        server.destroy();        logger.info("Prophet is dead");    }}