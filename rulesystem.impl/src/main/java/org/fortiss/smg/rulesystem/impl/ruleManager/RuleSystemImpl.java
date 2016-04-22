package org.fortiss.smg.rulesystem.impl.ruleManager;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.fortiss.smg.rulesystem.api.Rule;
import org.fortiss.smg.rulesystem.api.RuleSystemInterface;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsImpl;
import org.fortiss.smg.rulesystem.impl.persistor.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.impl.executor.RuleSystemDroolsDrl;

public class RuleSystemImpl implements RuleSystemInterface{

	@Override
	public boolean createRule(Rule rule){

		try {
			if(RuleSystemDBImpl.insertIntoRuleTable(rule))
				if(RuleSystemDroolsDrl.writeRule(rule.toDRL()))
					return true;
			RuleSystemDroolsImpl.addRule(rule);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean modifyRule(Rule rule){

		try {
			if(RuleSystemDBImpl.updateRule(rule))
				if(RuleSystemDroolsDrl.deleteRule(rule.getName())&& RuleSystemDroolsDrl.writeRule(rule.toDRL()))
					return true;
			RuleSystemDroolsImpl.modifyRule(rule);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeRule(String ruleName){

		try {
			if(RuleSystemDBImpl.deleteFromRuleTable(ruleName))
				if(RuleSystemDroolsDrl.deleteRule(ruleName))
					return true;

			RuleSystemDroolsImpl.removeRule(ruleName);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return false;
	}
	
	public boolean createDrlFile(Rule rule) {
		String prefix = "package org.fortiss.smg.rulesystem.impl\n\n" +
				"import org.fortiss.smg.rulesystem.api.Events;\n" +
				"import org.fortiss.smg.actuatormaster.api.*;\n" +
				"import org.slf4j.Logger;\n" +
				"import org.fortiss.smg.containermanager.api.*;\n" +
				"import org.fortiss.smg.smgschemas.commands.*;\n" +
				"import org.fortiss.smg.rulesystem.impl.executor.*;\n" +
				"import accumulate org.fortiss.smg.rulesystem.impl.executor.Compare compare;\n" +
				"global org.slf4j.Logger logger;\n" +
				"global IActuatorMaster master;\n" +
				"global IActuatorListener eventListener;\n" +
				"global IActuatorClient actuatorClient;\n" +
				"global Commander commander;\n" +
				"global SendEmail mail;\n" +
				"dialect \"java\"\n\n" +
				"declare Events\n" +
				"\t@role(event)\n" +
				"\torigin : String\n" +
				"\tvalue: double\n" +
				"\tmaxAbsError : double\n" +
				"\tdevid: String\n" +
				"end\n\n";
		List<String> lines = Arrays.asList(prefix, rule.toDRL());
		String filePath = "/opt/felix/drools1.drl";	
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			filePath = "c:/opt/felix/drools1.drl";
		}
		
		try {
			File newFile = new File(filePath);  
			if (newFile.createNewFile()){
				System.out.println("File is created!");
			}else{
				System.out.println("File already exists.");
			}
			Path file = Paths.get(filePath);
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
}
