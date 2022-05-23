package de.simonsator.partyandfriendsgui.communication.tasks.hidemodes;

import de.simonsator.partyandfriendsgui.api.hide.HideMode;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.api.hide.contexts.PartyHideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ShowOnlyPartyMembers extends HideMode {
	public ShowOnlyPartyMembers(Hider pHider) {
		super(5, pHider, new PartyHideContext());
	}

	@Override
	public void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext) {
		PartyHideContext partyHideContext = (PartyHideContext) pContext;
		List<String> partyMembers = partyHideContext.PARTY;
		assert partyMembers != null;
		if (partyMembers.contains(pPlayer2.getName()))
			HIDER.showPlayer(pPlayer1, pPlayer2);
		else
			HIDER.hidePlayer(pPlayer1, pPlayer2);
	}

	@Override
	public void updateInformation(HideContext pContext) {
		PartyHideContext partyHideContext = (PartyHideContext) pContext;
		assert partyHideContext.UPDATE_TYPE != null;
		if (partyHideContext.UPDATE_TYPE.equals("PartyChanged")) {
			Player joinedPlayer = Bukkit.getPlayer(partyHideContext.JOINED_PLAYER);
			for (String partyMemberString : partyHideContext.PARTY) {
				if (partyMemberString.equals(partyHideContext.JOINED_PLAYER))
					continue;
				Player partyMember = Bukkit.getPlayer(partyMemberString);
				if (getInThisHideMode().contains(partyMember))
					executeHide(partyMember, joinedPlayer, pContext);
				if (getInThisHideMode().contains(joinedPlayer)) {
					if (partyHideContext.PARTY.contains(partyHideContext.JOINED_PLAYER))
						executeHide(joinedPlayer, partyMember, pContext);
					else
						HIDER.hidePlayer(joinedPlayer, partyMember);
				}
			}
		}
	}
}
