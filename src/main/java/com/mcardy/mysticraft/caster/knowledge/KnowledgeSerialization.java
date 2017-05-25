package com.mcardy.mysticraft.caster.knowledge;

import java.util.UUID;

public abstract class KnowledgeSerialization {
	
	public static KnowledgeSerialization getSerialization() {
		// TODO Detect mysql and save using database
		return new KnowledgeFileSerialization();
	}
	
	public abstract Knowledge load(UUID casterId);
	public abstract void save(UUID casterId, Knowledge knowledge);
	
}