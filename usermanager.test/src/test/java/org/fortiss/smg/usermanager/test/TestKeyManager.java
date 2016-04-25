package org.fortiss.smg.usermanager.test;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.fortiss.smg.usermanager.api.Key;
import org.fortiss.smg.usermanager.api.KeyManagerInterface;
import org.fortiss.smg.usermanager.api.Tuple;
import org.fortiss.smg.usermanager.api.User;
import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.fortiss.smg.usermanager.dbutil.UserManagerDBUtil;
import org.fortiss.smg.usermanager.impl.UserManagerImpl;
import org.fortiss.smg.usermanager.impl.key.KeyManagerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class TestKeyManager {
	private KeyManagerInterface keymanager;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(TestKeyManager.class);
	private UserManagerImpl userImpl;

	@Before
	public void setUp() {
		String dburl =    "jdbc:"+  TestingDatabase.getDBUrl();
		String dbuser = TestingDatabase.getDBUser();
		String dbpassword = TestingDatabase.getDBPassword();

		System.out.println("Statement created...");
		String sql = "TRUNCATE UserManager_Devices";
		TestingDBUtil db = new TestingDBUtil();
		db.executeQuery(sql);
		sql = "TRUNCATE UserManager_Users";
		db.executeQuery(sql);

		//UserManagerDBUtil dbUtil = new UserManagerDBUtil(dburl, dbuser, dbpassword);
		userImpl = new UserManagerImpl(db); //, logger);
		keymanager = new KeyManagerImpl(db); //, userImpl); //,logger);
	}


		@Test
		public void testCalcSignature() throws TimeoutException{
			Assert.assertEquals("RrTsWGEXFU2s1J1mTl1j/ciO+1E=",keymanager.calcSignature("foo", "bar"));
		}
		
		
		@Test
		public void testCheckSignature() throws TimeoutException {
	
				
			Tuple keys = keymanager.generateKeys(16,1);
	
			String key1 = keys.getValue();
			String key1_public = keys.getTupleKey();
	
			Tuple keys2 = keymanager.generateKeys(17,2);
			String key2 = keys2.getValue();
			String key2_public = keys2.getTupleKey();
	
			String text1 = "adaes54cyy5vubdxv6";
			String text2 = "ac46v67vxeusxvus";
	
			
			Assert.assertTrue(keymanager.checkSignature(key1_public, text1,
					keymanager.calcSignature(key1, text1)));
	
			Assert.assertTrue(keymanager.checkSignature(key2_public, text2,
					keymanager.calcSignature(key2, text2)));
	
			Assert.assertFalse(keymanager.checkSignature(key1_public, text1,
					keymanager.calcSignature(key1, text2)));
	
			Assert.assertFalse(keymanager.checkSignature(key2_public, text1,
					keymanager.calcSignature(key1, text2)));
	
			Assert.assertFalse(keymanager.checkSignature(key1_public, text2,
					keymanager.calcSignature(key1, text1)));
	
		}

	@Test
	public void testGenerateKeys() throws TimeoutException {
		int userid = 0;
		for (int i = 1; i < 20; i++) {
			userid = i;

			Tuple keys = keymanager.generateKeys(userid,i);
			
			Assert.assertTrue(keys.getTupleKey().length() > 0);
			Assert.assertTrue(keys.getValue().length() > 0);

			List<String> keys_db = keymanager.getKeys(userid);
			System.out.println(keys_db.toString() + " --- " + keys.getTupleKey());
			Assert.assertTrue(keys_db.contains(keys.getTupleKey()));
		}
		
		Assert.assertTrue(keymanager.removeAllKeys(userid));
	}

	@Test
	public void testGenerateSignature() throws TimeoutException {
		String text1 = "Lorem ipsum 972hjssdd klkfod";
		String key1 = "foo-bar";

		String sig1 = keymanager.calcSignature(key1, text1);

		// same signature
		Assert.assertTrue(keymanager.calcSignature(key1, text1).equals(sig1));

		// wrong signature
		Assert.assertFalse(keymanager.calcSignature("foo-bar2", text1).equals(
				sig1));
		Assert.assertFalse(keymanager.calcSignature(key1, "Lorem ipsum")
				.equals(sig1));

	}


	@Test
	public void testkeyManagerMethods() throws Exception {
		User user = new User();
		user.setUserName("foo");
		String password = "test";
		user.setName("foo");
		user.setEmail("foo@test.org");
		userImpl.createUser(user, password);
		user = userImpl.getUserByName("foo");

		Tuple key = keymanager.generateKeys((int)user.getId(),1);
		User userFromDB = keymanager.getUser(key.getTupleKey());
		Assert.assertEquals(user.getEmail(), userFromDB.getEmail());

		Assert.assertTrue(keymanager.setDevId(key.getTupleKey(), 2));

		Assert.assertTrue(keymanager.setOS(key.getTupleKey(), "iOS"));

		Assert.assertTrue(keymanager.removeKey(key.getTupleKey()));

	}

}
