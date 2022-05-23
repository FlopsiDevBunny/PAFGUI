package de.simonsator.partyandfriendsgui.api.hide.contexts;

import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.utilities.ToList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartyHideContext extends HideContext {
	public final List<String> PARTY;
	public final String UPDATE_TYPE;
	public final String JOINED_PLAYER;

	public PartyHideContext() {
		this(null);
	}

	private PartyHideContext(List<String> pParty) {
		super("PartyHideContext", null);
		PARTY = pParty;
		UPDATE_TYPE = null;
		JOINED_PLAYER = null;
	}

	private PartyHideContext(List<String> pParty, String pUpdateType, String pPartyPlayer) {
		super("PartyHideContext", null);
		PARTY = pParty;
		UPDATE_TYPE = pUpdateType;
		JOINED_PLAYER = pPartyPlayer;
	}

	@Override
	public HideContext toHideContext(JsonObject pJObj, Map<String, HideContext> pPreviousHideContexts) {
		List<String> partyMembers;
		if (pJObj.has("partyMembers"))
			partyMembers = ToList.toList(pJObj.get("partyMembers").getAsJsonArray());
		else partyMembers = new ArrayList<>();
		if (pJObj.has("updateType")) {
			String partyPlayer = null;
			if (pJObj.has("partyPlayer"))
				partyPlayer = pJObj.get("partyPlayer").getAsString();
			return new PartyHideContext(partyMembers, pJObj.get("updateType").getAsString(), partyPlayer);
		}
		return new PartyHideContext(partyMembers);
	}
}
