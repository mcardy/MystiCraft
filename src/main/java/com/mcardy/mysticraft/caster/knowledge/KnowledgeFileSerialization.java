package com.mcardy.mysticraft.caster.knowledge;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

import com.google.gson.Gson;
import com.mcardy.mysticraft.MystiCraft;

public class KnowledgeFileSerialization extends KnowledgeSerialization {

	private File folder;
	
	protected KnowledgeFileSerialization() {
		this.folder = new File(MystiCraft.getInstance().getDataFolder(), "playerdata");
		if (!this.folder.exists()) {
			this.folder.mkdirs();
		}
	}
	
	@Override
	public Knowledge load(UUID casterId) {
		File file = getFile(casterId);
		Knowledge knowledge = null;
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				knowledge = new Gson().fromJson(reader, Knowledge.class);
				reader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			knowledge = Knowledge.createNewKnowledge();
		}
		return knowledge;
	}
	
	@Override
	public void save(UUID casterId, Knowledge knowledge) {
		try {
			FileWriter writer = new FileWriter(getFile(casterId));
			new Gson().toJson(knowledge, writer);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private File getFile(UUID casterId) {
		return new File(this.folder, casterId.toString() + ".json");
	}
	
}