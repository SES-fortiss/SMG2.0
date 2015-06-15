/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
/**
 * 
 */
package org.fortiss.smg.webrest.impl.front;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fortiss.smg.gamification.api.QuizQuestion;
import org.fortiss.smg.gamification.api.SingleGamificationUser;
import org.fortiss.smg.gamification.api.GamificationGroup;
import org.fortiss.smg.webrest.impl.BundleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Theodor Plop
 *
 */
@Path("/gamification")
public class GamificationStatistics {

	private static Logger logger = LoggerFactory
			.getLogger(GamificationStatistics.class);

	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	@Path("/doSomething")
	public String doSomething(@QueryParam("name") String s) {
		String test = "";
		try {
			test = BundleFactory.getGamification().doSomething(s);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return "From Gamification: " + test;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/question/{userID}")
	public QuizQuestion getQuestion(@PathParam("userID") int userID) {

		QuizQuestion question;

		question = BundleFactory.getGamification()
				.getNewQuestionForUser(userID);

		return question;

	}

	@POST
	@Path("/users/points/{userID}/{pts}")
	public boolean addPointsToGamificationUser(
			@PathParam("userID") String userID, @PathParam("pts") String pts) {

		boolean isSuccessful;

		int userIDInt = Integer.parseInt(userID);
		int ptsInt = Integer.parseInt(pts);
		isSuccessful = BundleFactory.getGamification()
				.addPointsToGamificationUser(userIDInt, ptsInt);
		return isSuccessful;
	}

	@POST
	@Path("/question/{userID}/{questionID}/{answer}")
	public boolean answerOpenQuestionForUser(@PathParam("userID") int userID,
			@PathParam("questionID") int questionID,
			@PathParam("answer") int answer) {

		boolean isAnswered;
		isAnswered = BundleFactory.getGamification().answerOpenQuestionForUser(
				userID, questionID, answer);

		return isAnswered;
	}

	@GET
	@Path("/createSingleGamificationUser")
	public String createSingleGamificationUser(@QueryParam("uid") int userID,
			@QueryParam("name") String name) {

		BundleFactory.getGamification().createSingleGamificationUser(userID,
				name);

		return "Created";

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.TEXT_PLAIN })
	@Path("/removeGamificationGroup")
	public String removeGamificationGroup(@QueryParam("groupID") int id) {

		boolean isRemoved;

		isRemoved = BundleFactory.getGamification().removeGamificationGroup(id);

		if (isRemoved == true) {
			return "Group removed";
		} else {
			return "Group could not be removed";
		}
	}

	@GET
	@Path("/createGamificationGroup")
	public String createGamificationGroup(@QueryParam("groupID") int id,
			@QueryParam("name") String name) {

		BundleFactory.getGamification().createGamificationGroup(id, name);
		return "Created";
	}

	@GET
	@Path("/groups/points")
	public boolean addPointsToGamificationGroup(@QueryParam("gid") int groupID,
			@QueryParam("points") int points) {

		boolean addPoints;

		addPoints = BundleFactory.getGamification()
				.addPointsToGamificationGroup(groupID, points);

		return addPoints;

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/onlin2")
	public String user2() {

		// QuizQuestion test = new QuizQuestion(3, "Who am I?", 0, "Tyler",
		// "Marla", "Bob", "Jack", 1);
		return "Hello World";
	}

	@POST
	@Path("/foo")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response foo(@QueryParam("name") String name) {

		String result = "Hello " + name;
		return Response.status(201).entity(result).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/users")
	public Map<Integer, SingleGamificationUser> getUsers() {

		Map<Integer, SingleGamificationUser> users = BundleFactory
				.getGamification().getGamificationUsers();
		return users;

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/groups")
	public Map<Integer, GamificationGroup> getGroups() {

		Map<Integer, GamificationGroup> groups = BundleFactory
				.getGamification().getGamificationGroups();
		return groups;

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/groups/users/{groupID}")
	public Map<Integer, SingleGamificationUser> getUsersFromGroup(
			@PathParam("groupID") int groupID) {

		Map<Integer, SingleGamificationUser> users = BundleFactory
				.getGamification().getMemberUsersOfGamificationGroup(groupID);
		return users;

	}

	@POST
	@Path("/groups/add/{userID}/{groupID}")
	public boolean joinGroup(@PathParam("userID") int userID,
			@PathParam("groupID") int groupID) {

		boolean isAdded;
		isAdded = BundleFactory.getGamification().addGamificationUserToGroup(
				userID, groupID);
		return isAdded;
	}

	@POST
	@Path("/groups/{userID}/{groupID}")
	public boolean removeGamificationUserFromGroup(
			@PathParam("userID") int userID, @PathParam("groupID") int groupID) {

		boolean isRemoved;

		isRemoved = BundleFactory.getGamification()
				.removeGamificationUserFromGroup(userID, groupID);

		return isRemoved;
	}

}
