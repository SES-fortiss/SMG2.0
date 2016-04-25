package org.fortiss.smg.containermanager.api.devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Container {
	private static final Logger logger = LoggerFactory
			.getLogger(Container.class);
	
	
	String containerId;
	String hrName;
	ContainerType containerType = ContainerType.UNKNOWN;
	ContainerFunction containerFunction = ContainerFunction.NONE;

	/*
	 * will be forwarded to parent (does not include virtually added containers
	 */
	Map<SIDeviceType, ArrayList<Double>> deviceTypeValueMap = new ConcurrentHashMap<SIDeviceType, ArrayList<Double>>();

	public ArrayList<Double> getDeviceTypeValueMap(SIDeviceType type) {
		if (deviceTypeValueMap.containsKey(type)) {
			return deviceTypeValueMap.get(type);
		}
		return new ArrayList<Double>();

	}

	Map<SIDeviceType, SummaryStatistics> summaryStatistics = new ConcurrentHashMap<SIDeviceType, SummaryStatistics>();

	boolean virtualContainer;
	private int depth = -1;

	// TODO: clearancs for groups, users --> read,control,modify

	protected Container() {

	}

	public HashMap<String,Object> serialize(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("containerid", this.containerId);
		map.put("ContainerHRName", this.hrName);
		map.put("containerfunction", this.containerFunction);
		map.put("containertype", this.containerType);
		map.put("virtualcontainer", this.virtualContainer);
		return map;
	}
	
	public Container(String containerId, String hrName,
			ContainerType containerType, ContainerFunction containerFunction,
			boolean virtualContainer) {
		super();
		this.containerId = containerId;
		this.hrName = hrName;
		this.containerType = containerType;
		this.containerFunction = containerFunction;
		this.virtualContainer = virtualContainer;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public ContainerType getContainerType() {
		return containerType;
	}

	public void setContainerType(ContainerType containerType) {
		this.containerType = containerType;
	}

	public ContainerFunction getContainerFunction() {
		return containerFunction;
	}

	public void setContainerFunction(ContainerFunction containerFunction) {
		this.containerFunction = containerFunction;
	}

	public boolean isVirtualContainer() {
		return virtualContainer;
	}

	public void setVirtualContainer(boolean virtualContainer) {
		this.virtualContainer = virtualContainer;
	}

	/*
	 * Provide statistical information about the container
	 */

	public SummaryStatistics getSummaryStatistics(SIDeviceType type) {
		if (summaryStatistics.containsKey(type)) {
			return summaryStatistics.get(type);
		}
		return new SummaryStatistics();
	}

	public double getMean(SIDeviceType type) {
		return getSummaryStatistics(type).getMean();
	}

	public double getMax(SIDeviceType type) {
		return getSummaryStatistics(type).getMax();
	}

	public double getMin(SIDeviceType type) {
		return getSummaryStatistics(type).getMin();
	}

	public void onUpdateStatistics(SIDeviceType type,
			ContainerManagerInterface cint) throws TimeoutException {

		// our values
		String previous = null;
		
		if (summaryStatistics.get(type) != null) {
			previous = ""+summaryStatistics.get(type).getSum();
		}
		
		//if(!summaryStatistics.containsKey(type)){
			logger.debug("Create new SummaryStatistics for type: " + type + " (previous was " + previous + ")");
			summaryStatistics.put(type, new SummaryStatistics());
		//}
		
		SummaryStatistics sum = summaryStatistics.get(type);

		// forwards
		deviceTypeValueMap.put(type, new ArrayList<Double>());
		ArrayList<Double> forward = deviceTypeValueMap.get(type);

		List<Map.Entry<Container, EdgeType>> childrenWithEdgetypeList = cint.getChildrenWithEdgeTypes(this.containerId);
		
		logger.debug("going through childs");
		
		for (int i = 0; i < childrenWithEdgetypeList.size(); i++) {
			Container child = childrenWithEdgetypeList.get(i).getKey(); //con.getLeft();
			EdgeType edge = childrenWithEdgetypeList.get(i).getValue(); //con.getRight();

			
			if (child != null && edge != null) {
			
				logger.debug("child: " + child.getHrName() + " type: " + type + " isVirtual " + child.isVirtualContainer());
			// check for virtual

				if (edge.equals(EdgeType.VIRTUAL)) {
					if (!child.hasRealFather(this.containerId)) {
						continue;
					}
				}

				if (child instanceof DeviceContainer) {
					DeviceContainer devCon = (DeviceContainer) child;
					if (devCon.getDeviceType().equals(type)) {
						sum.addValue(devCon.getValue());
						/*
						 * if edge or deviceContainer is virtual do not forward
						 */
						if (edge.equals(EdgeType.REAL) && !devCon.isVirtualContainer()) {
							forward.add(devCon.getValue());
						}
					}
				} 
				else {
					ArrayList<Double> childList = child.getDeviceTypeValueMap(type);
					for (double value : childList) {
						sum.addValue(value);
					}
					if (edge.equals(EdgeType.REAL)) {
						forward.addAll(childList);
					}
				
				}
			}
			else {
				logger.debug("unknown child " + child);
			}
			
		

		}

			
	}

	private boolean hasRealFather(String containerId2) {
		Container parent = this.getParent();
		if (parent == null) {
			return false;
		}
		if (parent.containerId.equals(containerId2)) {
			return true;
		}
		return parent.hasRealFather(containerId2);

	}

	public List<Container> getChildren() {
		// TODO
		return null;

	}

	public Container getParent() {
		// TODO
		return null;

	}

	public double getSum(SIDeviceType deviceType) {
		return getSummaryStatistics(deviceType).getSum();
	}

	public int getDepth(ContainerManagerInterface cint) {
		if (depth == -1) {
			String parent;
			try {
				parent = cint.getRealParentContainer(containerId);
			
			if (parent == null) {
				depth = 0;
			} else {
				Container con = cint.getContainer(parent);
				if (con == null) {
					throw new RuntimeException("not found " + parent + ","
							+ this.containerId);
				}
				depth = con.getDepth(cint);
			}
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return depth;
	}

}
