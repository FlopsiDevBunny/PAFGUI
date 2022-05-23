package de.simonsator.partyandfriendsgui.api.hide.contexts;

import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.utilities.ToList;

import java.util.List;
import java.util.Map;

public class FriendHideContext extends HideContext {
	public final List<String> FRIENDS;
	public final String UPDATE_TYPE;
	public final String FRIEND1;
	public final String FRIEND2;

	public FriendHideContext() {
		this(null);
	}

	private FriendHideContext(List<String> pFriends) {
		super("FriendHideContext", null);
		FRIENDS = pFriends;
		UPDATE_TYPE = null;
		FRIEND1 = null;
		FRIEND2 = null;
	}

	private FriendHideContext(String pUpdateType, String pFriend1, String pFriend2) {
		super("FriendHideContext", null);
		FRIENDS = null;
		UPDATE_TYPE = pUpdateType;
		FRIEND1 = pFriend1;
		FRIEND2 = pFriend2;
	}

	@Override
	public HideContext toHideContext(JsonObject pJObj, Map<String, HideContext> pPreviousHideContexts) {
		if (pJObj.has("updateType")) {
			String friend1 = null;
			if (pJObj.has("friend1"))
				friend1 = pJObj.get("friend1").getAsString();
			String friend2 = null;
			if (pJObj.has("friend2"))
				friend2 = pJObj.get("friend2").getAsString();
			return new FriendHideContext(pJObj.get("updateType").getAsString(), friend1, friend2);
		}
		return new FriendHideContext(ToList.toList(pJObj.get("friends").getAsJsonArray()));
	}
}
