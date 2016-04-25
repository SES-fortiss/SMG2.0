package org.fortiss.smg.analyzer.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.impl.databaseConnection.DatabaseRequestor;
import org.fortiss.smg.analyzer.impl.databaseConnection.SQL;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestDatabaseConnection {

	private static MockOtherBundles mocker;
	private TestingDBUtil db;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		mocker = new MockOtherBundles();
	}

	@Before
	public void setUp() throws IOException, TimeoutException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		db = new TestingDBUtil();

		db.setTable("doubleevents");
		System.out.println("searching for informationbroker");
		DefaultProxy<InformationBrokerInterface> clientInfo = new DefaultProxy<InformationBrokerInterface>(
				InformationBrokerInterface.class,
				InformationBrokerQueueNames.getQueryQueue(), 300);

		InformationBrokerInterface broker = clientInfo.init();
		System.out.println("found informationbroker");

		System.out.println("Database set up " + db.isComponentAlive());
	}

	@Test
	public void fetchData_fewElements_size() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235682L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020343182L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
		assertEquals(12, actualDataSet.getDataList().size(), 0.0);
	}

	@Test
	public void fetchData_fewElements_firstTimestamp()
			throws IllegalArgumentException, TimeoutException, SQLException {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235682L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020343182L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
		assertEquals(1415020235682L, actualDataSet.getDataList().get(0)
				.getTime(), 0.0);
	}

	@Test
	public void fetchData_fewElements_lastTimestamp()
			throws IllegalArgumentException, TimeoutException, SQLException {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235682L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020343182L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
		assertEquals(
				1415020343182L,
				actualDataSet.getDataList()
						.get(actualDataSet.getDataList().size() - 1).getTime(),
				0.0);
	}

	@Test
	public void fetchData_fewElements() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235682L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020343182L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
		// building expected data set
		List<DoublePoint> expectedDoublePoints = new ArrayList<DoublePoint>();
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020235682L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020243129L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020253328L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020262922L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020273642L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020285211L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020293136L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020310309L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020313005L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020323439L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020335789L));
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1415020343182L));
		DataSet expected = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"),
				expectedDoublePoints);
		assertEquals(expected, actualDataSet);
	}

	@Test
	public void fetchData_oneElement_newest() throws IllegalArgumentException,
			TimeoutException, SQLException {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235682L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020235682L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
		// building expected Dataset
		// choosing from == to returns the newest element found in the Database
		List<DoublePoint> expectedDoublePoints = new ArrayList<DoublePoint>();
		expectedDoublePoints.add(new DoublePoint(0.0, 0.0, 1416215481632L));
		DataSet expected = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"),
				expectedDoublePoints);
		assertEquals(expected, actualDataSet);
	}

	@Test
	public void fetchData_negativTimeframe() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(SQLException.class);
		Calendar startDate = Calendar.getInstance();
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020235682L);
		startDate.setTimeInMillis(1415020343182L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
	}

	@Test
	public void fetchData_notInDatabase() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(SQLException.class);
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235600L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020235681L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, db);
	}
	
	@Test
	public void fetchData_dbInterfaceNull() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(IllegalArgumentException.class);
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(1415020235600L);
		Calendar stopDate = Calendar.getInstance();
		stopDate.setTimeInMillis(1415020235681L);
		DataSet actualDataSet = new DataSet(startDate, stopDate, new DeviceId(
				"fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:82bb.2", "hexabus.wrapper"));
		DatabaseRequestor.fetchesDataSet(actualDataSet, null);
	}
	
	@Test
	public void fetchData_dataSetNull() throws IllegalArgumentException, TimeoutException, SQLException {
		exception.expect(IllegalArgumentException.class);
		DatabaseRequestor.fetchesDataSet(null, db);
	}

	@Test
	public void sqlNewest_valid() throws IllegalArgumentException,
			TimeoutException, SQLException {
		// can only test on the new database as no mock up for old database is
		// available
		DoublePoint newest = SQL.newest(new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45", "hexabus.wrapper"),
				db);
		DoublePoint expected = new DoublePoint(21.3, 0.0, 1416215482089L);
		assertEquals(expected, newest);
	}

	@Test
	public void sqlNewest_noDataFound() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(SQLException.class);
		SQL.newest(new DeviceId("blub", "hello"), db);
	}

	@Test
	public void sqlNewest_dbInterfaceNull() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(IllegalArgumentException.class);
		SQL.newest(new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper"), null);
	}

	@Test
	public void sqlOldest_valid() throws IllegalArgumentException,
			TimeoutException, SQLException {
		// can only test on the new database as no mock up for old database is
		// available
		DoublePoint oldest = SQL.oldest(new DeviceId(
				"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45", "hexabus.wrapper"),
				db);
		DoublePoint expected = new DoublePoint(24.8, 0.0, 1415020223615L);
		assertEquals(expected, oldest);
	}

	@Test
	public void sqlOldest_noDataFound() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(SQLException.class);
		SQL.oldest(new DeviceId("hello", "blub"), db);
	}

	@Test
	public void sqlOldest_dbInterfaceNull() throws IllegalArgumentException,
			TimeoutException, SQLException {
		exception.expect(IllegalArgumentException.class);
		SQL.oldest(new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper"), null);
	}

	@Test
	public void allAvailableDataSets_valid_numberLists()
			throws IllegalArgumentException, TimeoutException, SQLException {
		List<DataSet> allMondays = DatabaseRequestor.allAvailableDataSets(
				Calendar.MONDAY, Calendar.DAY_OF_MONTH, 1, db, new DeviceId(
						"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
						"hexabus.wrapper"));
		assertEquals(3, allMondays.size(), 0.0);
	}

	@Test
	public void allAvailableDataSets_valid_stopTimeFirst()
			throws IllegalArgumentException, TimeoutException, SQLException {
		List<DataSet> allMondays = DatabaseRequestor.allAvailableDataSets(
				Calendar.MONDAY, Calendar.DAY_OF_MONTH, 1, db, new DeviceId(
						"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
						"hexabus.wrapper"));
		assertEquals(1415055600000L, allMondays.get(0).getStopDate()
				.getTimeInMillis(), 0.0);
	}

	@Test
	public void allAvailableDataSets_valid_startTimeSecond()
			throws IllegalArgumentException, TimeoutException, SQLException {
		List<DataSet> allMondays = DatabaseRequestor.allAvailableDataSets(
				Calendar.MONDAY, Calendar.DAY_OF_MONTH, 1, db, new DeviceId(
						"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
						"hexabus.wrapper"));
		assertEquals(1415574000000L, allMondays.get(1).getStartDate()
				.getTimeInMillis(), 0.0);
	}

	@Test
	public void allAvailableDataSets_valid_numberElementsSecond()
			throws IllegalArgumentException, TimeoutException, SQLException {
		List<DataSet> allMondays = DatabaseRequestor.allAvailableDataSets(
				Calendar.MONDAY, Calendar.DAY_OF_MONTH, 1, db, new DeviceId(
						"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
						"hexabus.wrapper"));
		assertEquals(8596, allMondays.get(1).getDataList().size(), 0.0);
	}

	@Test
	public void allAvailableDataSets_invalidDevice()
			throws IllegalArgumentException, TimeoutException, SQLException {
		exception.expect(SQLException.class);
		DatabaseRequestor.allAvailableDataSets(Calendar.MONDAY,
				Calendar.DAY_OF_MONTH, 1, db, new DeviceId("hello",
						"hexabus.wrapper"));
	}
	
	@Test
	public void allAvailableDataSets_nullDevice()
			throws IllegalArgumentException, TimeoutException, SQLException{
		exception.expect(IllegalArgumentException.class);
		DatabaseRequestor.allAvailableDataSets(Calendar.MONDAY,
				Calendar.DAY_OF_MONTH, 1, db, null);
	}

	@Test
	public void allAvailableDataSets_invalidDatabaseConnection()
			throws IllegalArgumentException, TimeoutException, SQLException {
		exception.expect(IllegalArgumentException.class);
		DatabaseRequestor.allAvailableDataSets(Calendar.MONDAY,
				Calendar.DAY_OF_MONTH, 1, null, new DeviceId(
						"fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
						"hexabus.wrapper"));
	}

}
