package org.fortiss.smg.websocket.test.utils;
import static org.junit.Assert.assertEquals;

import org.fortiss.smg.websocket.impl.utils.SocketEventHandlerHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */

/**
 * @author xsebi
 *
 */
public class SocketEventHandlerHelperTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.fortiss.smartmicrogrid.api.socket.utils.SocketEventHandlerHelper#getProperty(java.util.List, java.lang.String)}.
	 */
	@Test
	public void testGetProperty() {
		/*
		List<Tuple> test_list1= new ArrayList<Tuple>();
		String test1 = SocketEventHandlerHelper.getProperty(test_list1,  "");
		assertEquals(test1, null);
		
		//null case
		assertEquals(SocketEventHandlerHelper.getProperty(null,  ""), null);
		
		List<Tuple> test_list2= new ArrayList<Tuple>();
		Tuple tuple1 = new Tuple();
		tuple1.setTupleKey("test");
		tuple1.setValue("value");
		test_list2.add(tuple1);
		assertEquals(SocketEventHandlerHelper.getProperty(test_list2,  "test"), "value");*/
	}

	/**
	 * Test method for {@link org.fortiss.smartmicrogrid.api.socket.utils.SocketEventHandlerHelper#getProxyForWrapper(java.lang.String)}.
	 */
	@Test
	public void testGetProxyForWrapper() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.fortiss.smartmicrogrid.api.socket.utils.SocketEventHandlerHelper#getQueue(java.lang.String)}.
	 */
	@Test
	public void testGetQueue() {
		assertEquals(SocketEventHandlerHelper.getQueue("foo?bar"), "foo");
	}

	/**
	 * Test method for {@link org.fortiss.smartmicrogrid.api.socket.utils.SocketEventHandlerHelper#getDev(java.lang.String)}.
	 */
	@Test
	public void testGetDev() {
		assertEquals(SocketEventHandlerHelper.getDev("foo?bar"), "bar");
	}

}
