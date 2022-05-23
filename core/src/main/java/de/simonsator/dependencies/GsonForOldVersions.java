package de.simonsator.dependencies;

/**
 * @author simonbrungs
 * @version 1.0.0 28.06.17
 */
public class GsonForOldVersions extends Dependency {
	public GsonForOldVersions() {
		super("Gson-for-1-7-10", "http://simonsator.de/plugins/Gson-for-17-1.0-SNAPSHOT.jar");
	}

	@Override
	public boolean needed() {
		try {
			Class.forName("com.google.gson.Gson");
			return false;
		} catch (ClassNotFoundException e) {
			return true;
		}
	}
}
