package com.mcardy.mystic.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Command {

	public String label();
	public String description() default "";
	public String permission() default "";
	public String usage() default "";
	
}
