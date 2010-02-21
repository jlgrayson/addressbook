package com.example.addressbook.editing.internal;

import java.text.MessageFormat;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import com.example.addressbook.editing.AddressEditorMessages;

class ZipValidator implements IValidator {

	public IStatus validate(Object value) {
		String str = String.valueOf(value);
		if (str.matches("\\d{5}")) { //$NON-NLS-1$
			return ValidationStatus.ok();
		} else {
			return ValidationStatus.error(MessageFormat.format(AddressEditorMessages.ZipValidator_Message, str));
		}
	}

}