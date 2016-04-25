package org.fortiss.smg.webrest.test.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIUnitType;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.webrest.impl.BundleFactory;
import org.fortiss.smg.webrest.test.server.MockWrapperServerControl;
import org.fortiss.smg.webrest.test.util.ClientHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class TestStatistics {

    private static MockWrapperServerControl server;
    private static InformationBrokerInterface persistenceHandler;
	private int port;

    @Before
    public void startServer() {
        TestStatistics.server = new MockWrapperServerControl();
        port =server.start();

        TestStatistics.persistenceHandler = Mockito
                .mock(InformationBrokerInterface.class);
        BundleFactory.setInformationBrokerHandler(TestStatistics.persistenceHandler);
    }

    @After
    public void stopServer() {
        TestStatistics.server.stop();
    }

    /*
    @Test
    public void testGetBoolValue() throws TimeoutException {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date dateFrom = cal.getTime();
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date dateTo = cal.getTime();

        String dev = "foo";

        List<BooleanPoint> devs = new ArrayList<BooleanPoint>();
        devs.add(new  BooleanPoint(true, new Date().getTime() - 36000 ));
        devs.add(new  BooleanPoint(false,new Date().getTime()));

        Mockito.when(
                TestStatistics.persistenceHandler.getBoolValue(
                        Matchers.anyString(), Matchers.anyLong(),
                        Matchers.anyLong())) .thenReturn(devs);

        String requestURL = "statistics/getBooleanValue/" + dev + "?from="
                + dateFrom.getTime() + "&to=" + dateTo.getTime();

        Assert.assertTrue(ClientHelper.checkResponse(requestURL, port));
        BooleanPoint[] rDummy = new BooleanPoint[1];
        BooleanPoint[] response = ClientHelper.checkEquality(requestURL,
        		rDummy.getClass(), port);

        Mockito.verify(TestStatistics.persistenceHandler, Mockito.atLeastOnce())
                .getBoolValue(Matchers.anyString(), Matchers.anyLong(),
                        Matchers.anyLong());

        Assert.assertNotNull(response);
        Arrays.deepEquals(response, devs.toArray());

    }
*/
    
    @Test
    public void testGetDoubleValue() throws TimeoutException {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date dateFrom = cal.getTime();
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date dateTo = cal.getTime();

        DeviceId dev = new DeviceId("foo", "bar");

        List<DoublePoint> devs = new ArrayList<DoublePoint>();
        devs.add(new DoublePoint(20.0,  1.0 ,dateTo.getTime()));
        devs.add( new DoublePoint(22.0,  1.0, dateFrom.getTime()));

        Mockito.when(
                TestStatistics.persistenceHandler.getDoubleValue(
                		Matchers.any(DeviceId.class),
                        Matchers.anyLong(), Matchers.anyLong()))
                .thenReturn(devs);

        String requestURL = "statistics/getDoubleValue/" + dev.getDevid() + "?from="
                + dateFrom.getTime() + "&to=" + dateTo.getTime() + "&wrapperid="+ dev.getWrapperId();

        System.out.println("Calling:" +requestURL);
        
        Assert.assertTrue(ClientHelper.checkResponse(requestURL, port));
      
        DoublePoint[] foo = new DoublePoint[1];
        DoublePoint[] response = ClientHelper.checkEquality(requestURL,	foo.getClass(), port);

        Mockito.verify(TestStatistics.persistenceHandler, Mockito.atLeastOnce())
                .getDoubleValue(Matchers.any(DeviceId.class), Matchers.anyLong(),
                        Matchers.anyLong());

        Assert.assertNotNull(response);
        Arrays.deepEquals(response, devs.toArray());

    }
}
