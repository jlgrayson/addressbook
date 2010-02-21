package com.example.addressbook.services.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.example.addressbook.services.IAddressService;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		context.registerService(IAddressService.class.getName(), new RandomDataAddressService(), null);
	}

	public void stop(BundleContext context) throws Exception {

	}

}
