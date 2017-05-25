package com.mcardy.mysticraft.util;

import com.google.gson.JsonObject;

public interface Configurable {

	public void setDefaults(JsonObject root);
	
	public void load(JsonObject root);
	
}
