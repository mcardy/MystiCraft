package com.mcardy.mysticraft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonConfig {

	private File file;
	private JsonObject root;
	
	public JsonConfig(File file) {
		this.file = file;
	}
	
	public JsonConfig(File folder, String name) {
		this(new File(folder, name));
	}
	
	public JsonObject getConfig() {
		if (this.root == null) {
			loadConfig();
		}
		return this.root;
	}
	
	public void saveConfig() {
		if (this.root == null) {
			loadConfig();
		}
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveDefaultConfig() {
		if (!this.file.exists()) {
			FileWriter writer;
			try {
				writer = new FileWriter(file);
				writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonObject()));
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadConfig() {
		if (!this.file.exists()) {
			saveDefaultConfig();
		}
		try {
			this.root = new JsonParser().parse(new FileReader(this.file)).getAsJsonObject();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
