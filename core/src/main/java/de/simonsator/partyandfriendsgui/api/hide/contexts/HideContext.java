package de.simonsator.partyandfriendsgui.api.hide.contexts;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public abstract class HideContext implements Comparable<HideContext> {
	public final String CONTEXT_TYPE;
	private final List<Class<HideContext>> SOFT_DEPENDENCIES;

	protected HideContext(String pContextType, List<Class<HideContext>> pSoftDependencies) {
		CONTEXT_TYPE = pContextType;
		SOFT_DEPENDENCIES = pSoftDependencies;
	}

	@Override
	public int compareTo(HideContext o) {
		if (o == null)
			return -1;
		if (SOFT_DEPENDENCIES == null)
			return 0;
		for (Class<HideContext> softDependencies : SOFT_DEPENDENCIES)
			if (o.getClass().equals(softDependencies)) {
				return 1;
			}
		if (o.compareTo(this) > 0)
			return -1;
		return 0;
	}

	public abstract HideContext toHideContext(JsonObject pJObj, Map<String, HideContext> pPreviousHideContexts);
}
