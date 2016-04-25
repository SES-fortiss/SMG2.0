package org.fortiss.smg.smgschemas.commands;


//use JsonTypeInfo.Id.CLASS for class naming

//Specify sub-classes using @JsonSubTypes annotation
//without this, deserializer will not be able to locate sub-types to use
/*
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({  
 @Type(value=DoubleCommand.class, name="doubleCommand"),  
})
*/


/*
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
*/
public class AbstractCommand<E> {

	E value;

	protected AbstractCommand() {
		
	}
	
	public AbstractCommand(E b) {
		value = b ;
	}

	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}

}
