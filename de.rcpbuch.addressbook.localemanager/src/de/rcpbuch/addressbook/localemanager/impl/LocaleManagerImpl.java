package de.rcpbuch.addressbook.localemanager.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;

import de.rcpbuch.addressbook.localemanager.ILocaleHandler;

public class LocaleManagerImpl {

	private static final String EXTENSION_POINT_ID = "de.rcpbuch.addressbook.localemanager.locales";
	private ExtensionTracker tracker;

	public void startTracking() {
		stopTracking();
		tracker = new ExtensionTracker();
		tracker.registerHandler(new IExtensionChangeHandler() {

			public void addExtension(IExtensionTracker tracker, IExtension extension) {
				dumpLocales();
			}

			public void removeExtension(IExtension extension, Object[] objects) {
				dumpLocales();
			}

		}, ExtensionTracker.createExtensionPointFilter(getExtensionPoint()));

	}

	public void stopTracking() {
		if (tracker != null) {
			tracker.close();
			tracker = null;
		}
	}

	public void dumpLocales() {
		IExtensionPoint extpoint = getExtensionPoint();

		System.out.println("\n=======================================================================================");
		System.out.println("These locales have been contributed to de.rcpbuch.addressbook.localemanager:\n");
		for (IConfigurationElement configElement : extpoint.getConfigurationElements()) {
			if (!configElement.getName().equals("locale")) {
				throw new RuntimeException("Invalid configuration element: " + configElement);
			}

			try {
				String locale = configElement.getAttribute("locale");
				ILocaleHandler handler = (ILocaleHandler) configElement.createExecutableExtension("class");
				System.out.println("* Locale \"" + locale + "\", handled by " + handler.getClass().getName()
						+ " (from plug-in " + configElement.getContributor().getName() + ")");
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}

		}
		System.out.println("\n\nThanks for your contributions!");
		System.out.println("This plug-in could perform locale-specific services based on your contributions now!");
		System.out.println("\n=======================================================================================");
	}

	private IExtensionPoint getExtensionPoint() {
		return Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT_ID);
	}

}
