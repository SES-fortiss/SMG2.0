package org.fortiss.smg.smgschemas.commands;

public class DoubleCommand extends AbstractCommand<Double> {


	protected DoubleCommand(){
		super();
	}
	
	public DoubleCommand(double d) {
		super(d);
	}
	
	
	@Override
	public String toString() {
		return "DoubleCommand [value=" + value + "]";
	}
	
	
}
