/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.webrest.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.AnalyzerInterface;
import org.fortiss.smg.analyzer.api.AnalyzerQueueNames;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.gamification.api.GamificationInterface;
import org.fortiss.smg.gamification.api.GamificationQueueNames;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.RabbitRPCProxy;
import org.fortiss.smg.usermanager.api.KeyManagerInterface;
import org.fortiss.smg.usermanager.api.KeyManagerInterface;
import org.fortiss.smg.usermanager.api.UserManagerQueueNames;

public class BundleFactory {

	private static InformationBrokerInterface broker;
	private static KeyManagerInterface keyManager;
	private static ContainerManagerInterface containerManager;
	private static AnalyzerInterface analyzer;
	private static GamificationInterface gamification;

	
	/**
	 * @author reviewed by Sergiu Soima
	 * I think here we start some rpc Servers.
	 */
	public static void activate() throws IOException, TimeoutException {

		RabbitRPCProxy<InformationBrokerInterface> clientInfo = new RabbitRPCProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 5000);

		broker = clientInfo.init();

		RabbitRPCProxy<KeyManagerInterface> clientKeyInfo = new RabbitRPCProxy<KeyManagerInterface>(
				KeyManagerInterface.class,
				UserManagerQueueNames.getKeyManagerInterfaceQueue(), 5000);

		keyManager = clientKeyInfo.init();

		RabbitRPCProxy<ContainerManagerInterface> containerManagerInfo = new RabbitRPCProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class,
				ContainerManagerQueueNames
						.getContainerManagerInterfaceQueryQueue(), 5000);

		containerManager = containerManagerInfo.init();
		
		RabbitRPCProxy<AnalyzerInterface> analyzerInfo = new RabbitRPCProxy<AnalyzerInterface>(
				AnalyzerInterface.class,
				AnalyzerQueueNames
						.getAnalyzerInterfaceQueue(), 5000);

		analyzer = analyzerInfo.init();
		
		RabbitRPCProxy<GamificationInterface> gamificationInfo = new RabbitRPCProxy<GamificationInterface>(
				GamificationInterface.class,
				GamificationQueueNames
						.getGamificationInterfaceQueue(), 5000);

		gamification = gamificationInfo.init();

	}

	public static void setInformationBrokerHandler(
			InformationBrokerInterface persistenceHandler) {
		BundleFactory.broker = persistenceHandler;
	}

	public static void setKeyManager(KeyManagerInterface keyManager) {
		BundleFactory.keyManager = keyManager;
	}

	public static void setContainerManager(
			ContainerManagerInterface containerManager) {
		BundleFactory.containerManager = containerManager;
	}
	
	public static void setAnalyzer(
			AnalyzerInterface analyzer) {
		BundleFactory.analyzer = analyzer;
	}

	public static ContainerManagerInterface getContainerManager() {
		return BundleFactory.containerManager;
	}

	public static KeyManagerInterface getIKeyManager() {
		return BundleFactory.keyManager;
	}

	public static InformationBrokerInterface getInformationBroker() {
		return BundleFactory.broker;
	}
	
	public static AnalyzerInterface getAnalyzer() {
		return BundleFactory.analyzer;
	}
	
	public static GamificationInterface getGamification() {
		return BundleFactory.gamification;
	}

	public static void setGamification(GamificationInterface gamification) {
		BundleFactory.gamification = gamification;
	}

}
