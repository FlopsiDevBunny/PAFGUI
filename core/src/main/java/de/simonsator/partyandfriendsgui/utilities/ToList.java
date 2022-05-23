package de.simonsator.partyandfriendsgui.utilities;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 00pfl
 * @version 1.0.0 on 07.07.2016
 */
public class ToList {
	public static List<String> toList(JsonArray pJsonArray) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < pJsonArray.size(); i++)
			list.add(pJsonArray.get(i).getAsString());
		return list;
	}
}
