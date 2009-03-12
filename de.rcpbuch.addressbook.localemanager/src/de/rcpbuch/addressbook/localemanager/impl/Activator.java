package de.rcpbuch.addressbook.localemanager.impl;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.rcpbuch.addressbook.localemanager";

	// The shared instance
	private static Activator plugin;

	private static LocaleManagerImpl localeManager;

	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		localeManager = new LocaleManagerImpl();
		localeManager.startTracking();
		localeManager.dumpLocales();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		localeManager.stopTracking();
		localeManager = null;
	}

	public static Activator getDefault() {
		return plugin;
	}

}
