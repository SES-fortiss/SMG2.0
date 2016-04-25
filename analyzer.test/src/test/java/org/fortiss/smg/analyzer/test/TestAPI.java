package org.fortiss.smg.analyzer.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.analyzer.api.DataSet;
import org.fortiss.smg.analyzer.api.NoDataFoundException;
import org.fortiss.smg.analyzer.impl.AnalyzerImpl;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.informationbroker.api.InformationBrokerQueueNames;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestAPI {

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
	public void getSum() throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(65.1, impl.getSum(1416215372031L, 1416215392031L, dev), 0.0);
	}

	@Test
	public void getSum_Null() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
		
		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getSum(1416215372032L, 1416215372033L, dev);
	}

	@Test
	public void getSum_CalendarNull() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
	
		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getSum(0, 1416215392031L, dev);
	}

	@Test
	public void getSum_DeviceNull() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {


		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getSum(1416215372031L, 1416215392031L, dev);
	}

	@Test
	public void getArithmeticMean() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(21.7, impl.getArithmeticMean(1416215372031L, 1416215392031L, dev), 0.0);
	}

	@Test
	public void getArithmeticMean_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		
		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getArithmeticMean(1416215372032L, 1416215372033L, dev);
	}

	@Test
	public void getArithmeticMean_CalendarNull()
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMean(0, 1416215392031L, dev);
	}

	@Test
	public void getArithmeticMean_DeviceNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
 		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMean(1416215372031L, 1416215392031L, dev);
	}

	@Test
	public void getArithmeticMeanByTime() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(0.12055,
				impl.getArithmeticMeanByTime(1416215372031L, 1416215392031L, dev), 0.00001);
	}

	@Test
	public void getArithmeticMeanByTime_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		
		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getArithmeticMeanByTime(1416215372032L, 1416215372033L, dev);
	}

	@Test
	public void getArithmeticMeanByTime_CalendarNull()
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		
		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMeanByTime(0, 1416215392031L, dev);
	}

	@Test
	public void getArithmeticMeanByTime_DeviceNull()
			throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getArithmeticMeanByTime(1416215372031L, 1416215392031L, dev);
	}

	@Test
	public void getMax() throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(22.5, impl.getMax(1416215372031L, 1416215392031L, dev), 0.0);
	}

	@Test
	public void getMax_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {

		// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getMax(1416215372032L, 1416215372033L, dev);
	}

	@Test
	public void getMax_CalendarNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMax(0, 1416215392031L, dev);
	}

	@Test
	public void getMax_DeviceNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {

		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMax(1416215372031L, 1416215392031L, dev);
	}

	@Test
	public void getMin() throws IllegalArgumentException, TimeoutException,
			NoDataFoundException {
		
		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		assertEquals(21.3, impl.getMin(1416215372031L, 1416215392031L, dev), 0.0);
	}

	@Test
	public void getMin_Null() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
			// no data points

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(NoDataFoundException.class);
		impl.getMin(1416215372032L, 1416215372033L, dev);
	}

	@Test
	public void getMin_CalendarNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {


		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMin(0, 1416215372031L, dev);
	}

	@Test
	public void getMin_DeviceNull() throws IllegalArgumentException,
			TimeoutException, NoDataFoundException {
		
		// 3 data points - values:
		// 21.3
		// 21.3
		// 22.5

		DeviceId dev = null;
		AnalyzerImpl impl = new AnalyzerImpl(db);
		exception.expect(IllegalArgumentException.class);
		impl.getMin(1416215372031L, 1416215392031L, dev);
	}

	@Test
	public void getConsumptionRating_low() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
	
		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416215372031L, 1416215392031L , dev);
		DataSet reference = new DataSet(1416176588571L, 1416176608630L, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = true;
		expectedArray[1] = false;
		expectedArray[2] = false;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 30.0));
	}

	@Test
	public void getConsumptionRating_normal() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
	

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L, 1416176608630L, dev);
		DataSet reference = new DataSet(1416175158602L, 1416215372031L, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = true;
		expectedArray[2] = false;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 2));
	}

	@Test
	public void getConsumptionRating_normalAndExtreme()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
	

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L, 1416176608630L, dev);
		DataSet reference = new DataSet(1416215372031L, 1416215392031L, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = true;
		expectedArray[2] = false;
		expectedArray[3] = true;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 30.0));
	}

	@Test
	public void getConsumptionRating_high() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {


		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L, 1416176608630L, dev);
		DataSet reference = new DataSet(1416175158602L, 1416215372031L, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = false;
		expectedArray[2] = true;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 0.1));
	}

	@Test
	public void getConsumptionRating_extreme() throws IllegalArgumentException,
			NoDataFoundException, TimeoutException {
				
		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L,1416176608630L, dev);
		DataSet reference = new DataSet(1416215372031L, 1416215392031L, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = false;
		expectedArray[2] = false;
		expectedArray[3] = true;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 1.0));
	}

	@Test
	public void getConsumptionRating_allValuesSame()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {

		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L,1416176608630L, dev);
		DataSet reference = new DataSet(1416176588571L, 1416176608630L, dev);
		

		AnalyzerImpl impl = new AnalyzerImpl(db);

		boolean expectedArray[] = new boolean[4];
		expectedArray[0] = false;
		expectedArray[1] = true;
		expectedArray[2] = false;
		expectedArray[3] = false;

		assertArrayEquals(expectedArray,
				impl.getConsumptionRating(current, reference, 1.0));
	}

	@Test
	public void getConsumptionRating_oneDataSetEmpty()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		
		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L, 1416176608630L, dev);
		DataSet reference = new DataSet(1416176588572L, 1416176588573L, dev);

		AnalyzerImpl impl = new AnalyzerImpl(db);

		exception.expect(NoDataFoundException.class);
		impl.getConsumptionRating(current, reference, 3.0);
	}

	@Test
	public void getConsumptionRating_oneDataSetNull()
			throws IllegalArgumentException, NoDataFoundException,
			TimeoutException {
		
		DeviceId dev = new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.45",
				"hexabus.wrapper");
		DataSet current = new DataSet(1416176588571L, 1416176608630L, dev);
		DataSet reference = null;

		AnalyzerImpl impl = new AnalyzerImpl(db);

		exception.expect(IllegalArgumentException.class);
		impl.getConsumptionRating(current, reference, 3.0);
	}

	@Test
	public void getCorrelationTwoDevices() throws TimeoutException, NoDataFoundException{
		AnalyzerImpl impl = new AnalyzerImpl(db);
			
		DataSet xSet = new DataSet(1416215347498L, 1416215422097L, new DeviceId("fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:8404.2", "hexabus.wrapper"));
		DataSet ySet = new DataSet(1416215347498L, 1416215422097L, new DeviceId("fdfb:8f9f:4c8:bb4b:b5:5aff:fe0b:53a.3", "hexabus.wrapper"));
		assertEquals(-0.999, impl.getCorrelationTwoDevices(xSet, ySet, 10), 0.001);
	}
	
	@Test
	public void getCorrelationTwoDevices_dataSetNull() throws TimeoutException, NoDataFoundException{
		AnalyzerImpl impl = new AnalyzerImpl(db);
				
		DataSet xSet = new DataSet(1416215347498L, 1416215422097L, new DeviceId("fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:8404.2", "hexabus.wrapper"));
		DataSet ySet = null;
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("DataSet == null is not valid!");
		assertEquals(-0.999, impl.getCorrelationTwoDevices(xSet, ySet, 10), 0.001);
	}
	
	public void getCorrelationTwoDevices_noDataFound() throws TimeoutException, NoDataFoundException{
		AnalyzerImpl impl = new AnalyzerImpl(db);
			
		DataSet xSet = new DataSet(1416215347498L, 1416215422097L, new DeviceId("fdfb:8f9f:4c8:bb4b:50:c4ff:fe04:8404.2", "hexabus.wrapper"));
		DataSet ySet = null;
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("no data found in the database");
		assertEquals(-0.999, impl.getCorrelationTwoDevices(xSet, ySet, 10), 0.001);
	}

	
	@Test
	public void testGetWeeklyConsumption () throws Exception{
		
	
//		long [] timeStamps = { 1409833327598L,
//				1409833335674L,
//				1409833346272L,
//				1409833354537L,
//				1409833368636L,
//				1409833385049L,
//				1409833404930L,
//				1409833414602L,
//				1409833484648L,
//				1409833487349L,
//				1409833494580L,
//				1409833504686L,
//				1409833514562L,
//				1409833574597L,
//				1409833604570L,
//				1409833644596L,
//				1409833657422L,
//				1409833664671L,
//				1409833687412L,
//				1409833707414L,
//				1409833734605L,
//				1409833764576L,
//				1409833804572L,
//				1409833824575L,
//				1409833847369L,
//				1409833854520L,
//				1409833857366L,
//				1409833874555L,
//				1409833884613L,
//				1409833894696L,
//				1409833904629L,
//				1409833917388L,
//				1409833937358L,
//				1409833957476L,
//				1409833997366L,
//				1409834014565L,
//				1409834064539L,
//				1409834074540L,
//				1409834084560L,
//				1409834127375L,
//				1409834194584L,
//				1409834277363L,
//				1440078953971L,
//				1440078973852L,
//				1440079003920L,
//				1440079093965L,
//				1440079123864L,
//				1440079143847L,
//				1440079163827L,
//				1440079173835L,
//				1440079193839L,
//				1440079213841L,
//				1440079243828L,
//				1440079263841L,
//				1440079283830L,
//				1440079293863L,
//				1440079313836L,
//				1440079333829L,
//				1440079363866L,
//				1440079383835L,
//				1440079403843L,
//				1440079413839L,
//				1440079433838L,
//				1440079453833L,
//				1440079483841L,
//				1440079503850L,
//				1440079523842L,
//				1440079533837L,
//				1440079553868L,
//				1440079573841L,
//				1440079603867L,
//				1440079623880L,
//				1440079643858L,
//				1440079653886L,
//				1440079673864L,
//				1440079693866L,
//				1440079723875L,
//				1440079743843L,
//				1440079763849L,
//				1440079773849L,
//				1440079793856L,
//				1440079813870L,
//				1440079844730L,
//				1440079864021L,
//				1440079883852L,
//				1440079893846L,
//				1440079913843L,
//				1440079933890L,
//				1440079963848L,
//				1440079983860L,
//				1440080033884L,
//				1440080063888L,
//				1440080083866L,
//				1440080113910L,
//				1440080133894L,
//				1440080163906L,
//				1440080213912L,
//				1440080243928L,
//				1440080263904L,
//				1440080293873L,
//				1440080443927L,
//				1440080473867L,
//				1440080493896L,
//				1440080523937L,
//				1440080573888L,
//				1440080603908L,
//				1440080673919L,
//				1440080703903L,
//				1440080753896L,
//				1440080783918L,
//				1440080853877L,
//				1440080883886L,
//				1440080933874L,
//				1440080963876L,
//				1440080983886L,
//				1440081013886L,
//				1440081164255L,
//				1440081193895L,
//				1440081213906L};
		

//		for (int i = 0; i < timeStamps.length; i++) {
//			Calendar c = Calendar.getInstance();
//
//			Date y = new Date();
//			long x = timeStamps[i+1] - timeStamps[i];
//			c.setTimeInMillis(timeStamps[i]);
//			
//			System.out.println(c.getTime() + "  " +y.getTime());
//		}
//		
		AnalyzerImpl impl = new AnalyzerImpl(db);
		
		String devId = "solar_generator_watt";
		long t = Long.parseLong("1409833335674");
		String wrapperTag = "solarlog.wrapper";
		impl.getWeeklyConsumption(devId,wrapperTag, t);
		impl.getDailyCunsumptionOfWeek(devId, wrapperTag, t);
		impl.getMonthlyConsumption(devId, wrapperTag, t);
		impl.getDailyCunsumptionOfMonth(devId, wrapperTag, t);
		impl.getMonthlyCunsumptionOfYear(devId, wrapperTag, t);
		impl.getYearlyConsumption(devId, wrapperTag, t);
				
	}
}
