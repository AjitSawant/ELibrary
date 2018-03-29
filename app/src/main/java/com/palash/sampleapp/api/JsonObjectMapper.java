package com.palash.sampleapp.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class JsonObjectMapper {

	private static final String TAG = JsonObjectMapper.class.getSimpleName();
	ObjectMapper mapper = null;
	ByteArrayOutputStream outStream = null;
	String value = null;

	public <T> T map(String data, Class<?> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(data, mapper.getTypeFactory().constructCollectionType(List.class, Class.forName(clazz.getName())));
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String unMap(Object data) {
		try {
			mapper = new ObjectMapper();
			outStream = new ByteArrayOutputStream();
			mapper.writeValue(outStream, data);
			value = new String(outStream.toByteArray());
		} catch (JsonParseException e) {
			e.printStackTrace();
			value = null;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			value = null;
		} catch (IOException e) {
			e.printStackTrace();
			value = null;
		} catch (Exception e) {
			e.printStackTrace();
			value = null;
		}
		return value;
	}
}
