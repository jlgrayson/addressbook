package com.example.addressbook;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Public constants for IDs from com.example.addressbook.
 */
public class AddressBook {

	public static final String PLUGIN_ID = "com.example.addressbook"; //$NON-NLS-1$

	// Views
	public static final String VIEW_ADDRESS_LIST = PLUGIN_ID + ".AddressList"; //$NON-NLS-1$

	// Perspectives
	public static final String PERSPECTIVE_ADDRESS = PLUGIN_ID + ".AddressPerspective"; //$NON-NLS-1$

	// Commands
	public static final String COMMAND_OPEN = PLUGIN_ID + ".open"; //$NON-NLS-1$

	// Icons
	public static final ImageDescriptor ICON_MAGNIFIER = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID,
			"/icons/magnifier.png"); //$NON-NLS-1$

}