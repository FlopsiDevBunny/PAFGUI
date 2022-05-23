package de.simonsator.dependencies;

import de.simonsator.partyandfriendsgui.Main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Simonsator
 * @version 1.0.0 28.06.17
 */
public abstract class Dependency {
	private final String NAME;
	private final String FILE_URL;

	public Dependency(String pName, String pURL) {
		NAME = pName;
		FILE_URL = pURL;
	}

	public String getName() {
		return NAME;
	}

	public boolean download() {
		if (!Main.getInstance().getConfig().getBoolean("General.AutoDownloadDependencies"))
			return false;
		URL website;
		try {
			website = new URL(FILE_URL);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream("plugins/" + NAME + ".jar");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public abstract boolean needed();

	public String getUrl() {
		return FILE_URL;
	}
}
