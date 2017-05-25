package com.mcardy.mysticraft.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Arguments {

	public Argument[] args();
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Argument {
		
		public String label();
		public String[] options() default {};
		public boolean required() default false;
		public ArgumentType type();

	}
	
	public enum ArgumentType {
		
		STRING, BOOLEAN, INTEGER, FLOAT, OPTION, PLAYER; // More?
		
	}
	
}
