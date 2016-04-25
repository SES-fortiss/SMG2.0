package org.fortiss.smg.containermanager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.fortiss.smg.containermanager.api.devices.Container;
import org.fortiss.smg.containermanager.api.devices.ContainerEdge;
import org.fortiss.smg.containermanager.api.devices.EdgeType;
import org.fortiss.smg.containermanager.api.devices.SingleContainerEdge;
import org.slf4j.LoggerFactory;

public class EdgeHandler {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(EdgeHandler.class);

	private ContainerManagerImpl impl;

	public EdgeHandler(ContainerManagerImpl containerManagerImpl) {
		this.impl = containerManagerImpl;
	}

	public void loadEdges() {
		/*
		 * Select according Edges
		 */

		String sql = "SELECT *  FROM " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES+ ";";
		List<Map<String, Object>> resultSet;
		try {
			resultSet = this.impl.database.getSQLResults(sql);
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
			return;
		}
		if (resultSet == null || resultSet.size() < 1) {
			EdgeHandler.logger.warn("No Edges found");
		} else {
			/*
			 * Go through results and create Edges
			 */
			ArrayList<ContainerEdge> edgestmp = new ArrayList<ContainerEdge>();
			for (Map<String, Object> result : resultSet) {
				EdgeType edgetype = EdgeType.REAL;
				Integer type = (Integer) result.get("virtualcontaineredge");
				if (type == 1) {
					edgetype = EdgeType.VIRTUAL;
				}

				ContainerEdge edge = new ContainerEdge(result.get(
						"containeridparent").toString(), result.get(
						"containeridchild").toString(), edgetype);
				EdgeHandler.logger
						.info("Edge from Parent: "
								+ result.get("containeridparent").toString()
								+ " to Child: "
								+ result.get("containeridchild").toString()
								+ " added.");
				edgestmp.add(edge);
			}

			for (ContainerEdge edge : edgestmp) {

				if (!impl.edges.containsKey(edge.getParent())) {
					this.impl.edges.put(edge.getParent(),
							new ArrayList<SingleContainerEdge>());
				}
				this.impl.edges.get(edge.getParent())
						.add(new SingleContainerEdge(edge.getChild(), edge
								.getType()));
				logger.debug("New Child (" + edge.getChild() + ") for Parent: "
						+ edge.getParent());
			}

		}
	}

	public EdgeType getEdgeType(String parent, String child) {
		// TODO Auto-generated method stub
		ArrayList<SingleContainerEdge> current = impl.edges.get(parent);

		if(current == null){
			return null;
		}
		
		for (SingleContainerEdge edge : current) {
			if (edge.getChild().equals(child)) {
				return edge.getType();
			}
		}
		logger.debug("no edge found for " + parent + " child "+ child);
		return null;
	}


	public boolean removeContainerEdge(String parentID, String childID) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ " `ContainerIDParent` =  '" + parentID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "DELETE FROM " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ " `ContainerIDParent` =  '" + parentID + "';";
			if (this.impl.database.executeQuery(sql)) {

				SingleContainerEdge toRemove = new SingleContainerEdge(childID,
						null);
				if (this.impl.edges.get(parentID).contains(toRemove)) {
					this.impl.edges.get(parentID).remove(toRemove);
					return true;
				}
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}
	
	public boolean updateRealContainerEdgeFixedChild(String newParentID,
			String childID) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ "`VirtualContainerEdge` = '0';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "UPDATE " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " SET " + " `ContainerIDParent` =  '" + newParentID + "'"
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ "`VirtualContainerEdge` = '0';";

			boolean sqlResult = this.impl.database.executeQuery(sql);

			if (sqlResult) {

				SingleContainerEdge toCopy = new SingleContainerEdge(childID,
						EdgeType.REAL);

				// loop over all edges and delete old parent
				for (String parent : this.impl.edges.keySet()) {

					ArrayList<SingleContainerEdge> edgeParent = this.impl.edges
							.get(parent);

					// we found an edge with our element
					if (edgeParent.contains(toCopy)) {

						// cleanup & exit
						edgeParent.remove(toCopy);
						break;
					}
				}

				// insert new parent - child relation
				if (!this.impl.edges.containsKey(newParentID)) {
					this.impl.edges.put(newParentID,
							new ArrayList<SingleContainerEdge>());
				}

				if (!this.impl.edges.get(newParentID).contains(toCopy)) {
					this.impl.edges.get(newParentID).add(toCopy);
				} else {
					logger.warn("new parent child relation already existed ");
				}

				return true;
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}
	
	public boolean updateRealContainerEdgeFixedParent(String parentID,
			String oldChildID, String newChildID) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDParent` = '" + parentID + "' AND"
					+ "`ContainerIDChild` = '" + oldChildID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "UPDATE " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " SET " + " `ContainerIDChild` =  '" + newChildID + "'"
					+ " WHERE `ContainerIDChild` = '" + oldChildID + "' AND"
					+ " `ContainerIDParent` =  '" + parentID + "';";
			if (this.impl.database.executeQuery(sql)) {
				if (this.impl.edges.get(parentID).contains(
						new SingleContainerEdge(oldChildID, EdgeType.REAL))
						&& !this.impl.edges.get(parentID).contains(
								new SingleContainerEdge(newChildID,
										EdgeType.REAL))) {
					for (SingleContainerEdge edge : this.impl.edges.get(parentID)) {
						if (edge.getChild().equals(oldChildID)) {
							edge.setChild(newChildID);
						}
					}
					return true;
				}
				return false;
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}
	
	public boolean updateVirtualContainerEdgeFixedChild(String oldParentID,
			String newParentID, String childID) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDParent` = '" + oldParentID + "' AND"
					+ "`ContainerIDChild` = '" + childID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "UPDATE " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " SET " + " `ContainerIDParent` =  '" + newParentID + "'"
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ " `ContainerIDParent` =  '" + oldParentID + "';";
			if (this.impl.database.executeQuery(sql)) {

				SingleContainerEdge toCopy = new SingleContainerEdge(childID,
						EdgeType.VIRTUAL);
				if (this.impl.edges.get(oldParentID).contains(toCopy)) {
					if (!this.impl.edges.containsKey(newParentID)) {
						this.impl.edges.put(newParentID,
								new ArrayList<SingleContainerEdge>());
					}
					if (!this.impl.edges.get(newParentID).contains(toCopy)) {
						this.impl.edges.get(newParentID).add(toCopy);
					} else {
						logger.warn("new parent child relation already existed ");
					}
					this.impl.edges.get(oldParentID).remove(toCopy);
					return true;
				} else {
					logger.debug("Did not find the child " + childID
							+ " in the old parent " + oldParentID);
					return false;
				}
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;

	}
	
	public boolean updateVirtualContainerEdgeFixedParent(String parentID,
			String newChildID, String oldChildID) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDParent` = '" + parentID + "' AND"
					+ "`ContainerIDChild` = '" + oldChildID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "UPDATE " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " SET " + " `ContainerIDChild` =  '" + newChildID + "'"
					+ " WHERE `ContainerIDChild` = '" + oldChildID + "' AND"
					+ " `ContainerIDParent` =  '" + parentID + "';";
			if (this.impl.database.executeQuery(sql)) {
				if (this.impl.edges.get(parentID).contains(
						new SingleContainerEdge(oldChildID, EdgeType.VIRTUAL))
						&& !this.impl.edges.get(parentID).contains(
								new SingleContainerEdge(newChildID,
										EdgeType.VIRTUAL))) {
					for (SingleContainerEdge edge : this.impl.edges.get(parentID)) {
						if (edge.getChild().equals(oldChildID)) {
							edge.setChild(newChildID);
						}
					}
					return true;
				}
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}

	
	public boolean updateContainerEdgeType(String parentID, String childID,
			int virtual) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDParent` = '" + parentID + "' AND"
					+ "`ContainerIDChild` = '" + childID + "';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "UPDATE " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " SET " + " `VirtualContainerEdge` =  '" + virtual + "'"
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ " `ContainerIDParent` =  '" + parentID + "';";
			if (this.impl.database.executeQuery(sql)) {

				EdgeType type = EdgeType.REAL;
				if (virtual != 0) {
					type = EdgeType.VIRTUAL;
				}
				SingleContainerEdge toUpdate = new SingleContainerEdge(childID,
						null);
				if (this.impl.edges.get(parentID).contains(toUpdate)) {
					this.impl.edges.get(parentID).remove(toUpdate);
					toUpdate.setType(type);
					this.impl.edges.get(parentID).add(toUpdate);
				}
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}
	
	public boolean removeRealContainerEdge(String childID) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ " `VirtualContainerEdge` = '1';";

			List<Map<String, Object>> resultSet;

			resultSet = this.impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {
				/**
				 * no Container to update return 0
				 */
				return false;
			}
			/**
			 * update/replace Container in DB
			 */
			sql = "DELETE FROM " + ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE `ContainerIDChild` = '" + childID + "' AND"
					+ " `VirtualContainerEdge` = '1';";
			if (this.impl.database.executeQuery(sql)) {
				SingleContainerEdge toCopy = new SingleContainerEdge(childID,
						EdgeType.REAL);
				for (String parent : this.impl.edges.keySet()) {
					if (this.impl.edges.get(parent).contains(toCopy)) {
						this.impl.edges.get(parent).remove(toCopy);
						return true;
					}
				}
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}
	
	public List<Entry<Container, EdgeType>> getChildrenWithEdgeTypes(String id) {

		ArrayList<SingleContainerEdge> current = impl.edges.get(id);
		
		
		List<Entry<Container, EdgeType>> arr = new ArrayList<Map.Entry<Container, EdgeType>>(); // =
																								// new

		if(current == null){
			logger.debug(id + "has no edges");
			return arr;
		}
		
		
		// ArrayList<Pair<Container,
																								// EdgeType>>();
		for (SingleContainerEdge edge : current) {
			ImmutablePair<Container, EdgeType> pair = new ImmutablePair<Container, EdgeType>(
					this.impl.getContainer(edge.getChild()), edge.getType());

			arr.add(pair);
		}
		return arr;
	}

	public boolean addContainerEdge(String parentID, String childID, int virtual) {
		try {
			/**
			 * Check if Container exists
			 */
			String sql = "SELECT *  FROM "
					+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
					+ " WHERE ContainerIDParent = '" + parentID
					+ "' AND ContainerIDChild = '" + childID + "';";
			List<Map<String, Object>> resultSet;

			resultSet = impl.database.getSQLResults(sql);

			if (resultSet == null || resultSet.size() < 1) {

				/**
				 * add ContainerEdge to DB
				 */
				sql = "INSERT INTO "
						+ ContainerManagerImpl.DB_NAME_CONTAINEREDGES
						+ " (`ContainerIDParent`, `ContainerIDChild`, `VirtualContainerEdge`)"
						+ "VALUES ('" + parentID + "', '" + childID + "', '"
						+ virtual + "')";
				if (impl.database.executeQuery(sql)) {

					/**
					 * add ContainerEdge to memory
					 */
					EdgeType type = EdgeType.REAL;
					if (virtual != 0) {
						type = EdgeType.VIRTUAL;
					}
					if (!impl.edges.containsKey(parentID)) {
						impl.edges.put(parentID,
								new ArrayList<SingleContainerEdge>());
					}

					if (!impl.edges.get(parentID).contains(
							new SingleContainerEdge(childID, type))) {
						impl.edges.get(parentID).add(
								new SingleContainerEdge(childID, type));
					} else {
						/**
						 * Edge already in memory
						 */
						return false;
					}

					EdgeHandler.logger.info("ContainerEdge added: from "
							+ parentID + " to " + childID + "(type " + type
							+ ")");

					return true;
				}
			} else {
				return false;
			}
		} catch (TimeoutException e) {
			EdgeHandler.logger.warn("SQL Statement error", e);
		}

		return false;
	}
}
