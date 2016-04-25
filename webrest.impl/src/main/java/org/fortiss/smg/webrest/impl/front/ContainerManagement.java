
package org.fortiss.smg.webrest.impl.front;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.webrest.impl.BundleFactory;

/**
 * 
 * @author Sajjad Taheri
 *
 */
@Path("/container")
public class ContainerManagement {

    @Context
    HttpHeaders headers;

    @Context
    UriInfo uriInfo;

    @Context
    Request request;
    
    public static final int TIMEOUTLONG = 5000;
    //ContainerManagerInterface container;
    
    /*public ContainerManagement() {
    	DefaultProxy<ContainerManagerInterface> clientContainer = new DefaultProxy<ContainerManagerInterface>(
				ContainerManagerInterface.class,
				ContainerManagerQueueNames.getContainerManagerInterfaceQueryQueue(), TIMEOUTLONG);
		try {
			container = clientContainer.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("containerid/{containerId}/type/{type}")
    public double getMean(@PathParam("containerId") String containerId, @PathParam("type") SIDeviceType type)
    		throws TimeoutException {
    	return BundleFactory.getContainerManager().getMeanByType(containerId, type);
    }
    
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("containerid/{containerId}/type/{type}")
    public double getMin(@PathParam("containerId") String containerId, @PathParam("type") SIDeviceType type)
    		throws TimeoutException {
        return BundleFactory.getContainerManager().getMinByType(containerId, type);
    }
    
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("containerid/{containerId}/type/{type}")
    public double getSum(@PathParam("containerId") String containerId, @PathParam("type") SIDeviceType type)
    		throws TimeoutException {
        return BundleFactory.getContainerManager().getSumByType(containerId, type);
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("containerid/{containerId}/type/{type}")
    public double getMax(@PathParam("containerId") String containerId, @PathParam("type") SIDeviceType type)
    		throws TimeoutException {
        return BundleFactory.getContainerManager().getMaxByType(containerId, type);
    }
    
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    @Path("containerid/{containerId}/type/{type}")
    public double getCurrentValueByType(@PathParam("containerId") String containerId, @PathParam("type") SIDeviceType type)
    		throws TimeoutException {
        return BundleFactory.getContainerManager().getCurrentValueByType(containerId, type);
    }
    
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("containerid/{containerId}/{type}")
    public String getValueAsString(@PathParam("containerId") String containerId, @PathParam("type") SIDeviceType type)
    		throws TimeoutException {
    	
    	String tmp =""+BundleFactory.getContainerManager().getCurrentValueByType(containerId, type);
    	return tmp;
    }
}
