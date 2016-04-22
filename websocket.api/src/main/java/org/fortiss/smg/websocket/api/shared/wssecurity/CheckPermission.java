package org.fortiss.smg.websocket.api.shared.wssecurity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.jws.WebService;

/**
 * This permission <b>must</b> be attached to each method of a
 * {@link WebService} with the wished {@link TypeOfPermission}
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
	TypeOfPermission[] value();
}
