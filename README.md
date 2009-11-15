# Address Book

`com.example.addressbook` is an example application for the Eclipse RCP 3.5 platform:

![Addressbook screenshot](/ralfebert/addressbook/raw/simple/doc/screenshots/addressbook_01.png)

I'm working on it for my [upcoming German book about Eclipse RCP](http://www.ralfebert.de/rcpbuch/) and my [Eclipse RCP training courses](http://www.ralfebert.de/seminare/rcp/).

The application is developed and tested on OpenJDK 6 and Eclipse RCP 3.5.

## Branch `eclipse`

The `eclipse` branch utilizes more advanced RCP features and makes use of other features provided by Eclipse projects. It shows:

* How JFace Data Binding can be utilized to bind a simple editor form (see `AddressEditorPart#createBindings`).
* How validators can be added to the binding and how controls are decorated when validation errors occur using the provisional `ControlDecorationSupport` (see `AddressEditorPart#createBindings` and `ZipValidator`).
* How the dirty flag for an editor can be set automatically from a `DataBindingContext` (see `AddressEditorPart#addDirtyOnModelChangeListeners`).

## Branch `simple`

The `simple` branch only utilizes Eclipse RCP as defined by the feature `org.eclipse.rcp` (Eclipse RCP). It is structured in a very simple fashion and only utilizes basic features of the Eclipse RCP platform. It shows:

* How views are declared using the extension point `org.eclipse.ui.views` (see `plugin.xml` and `AddressListViewPart` in `com.example.addressbook`).
* How perspectives are declared using the extension point `org.eclipse.ui.perspectives` and how views are added to perspectives programmatically (see `plugin.xml` and `AddressPerspective` in `com.example.addressbook`).
* How `TableViewer` and `TableColumnLayout` can be used to set up tables (see `AddressListViewPart` in `com.example.addressbook`).
* How a JFace `TableViewer` can be sorted and filtered by typing a search string in a text field (see `AddressListViewPart` in `com.example.addressbook`).
* How the extension point `org.eclipse.ui.menus` can be utilized to add workbench commands to the menu and toolbar (see `plugin.xml` and `ApplicationActionBarAdvisor` in `com.example.addressbook`)
* How a handler for general-purpose commands is declared by selection (see `AddressDeleteHandler` for command `org.eclipse.ui.edit.delete` in `com.example.addressbook`)
* How a handler for general-purpose commands is declared for a view by id (see `AddressListRefreshHandler` for command `org.eclipse.ui.file.refresh` in `com.example.addressbook`)
* How JFace selections can be iterated using Java 5 foreach loops (see `SelectionUtils` and `AddressDeleteHandler` in `com.example.addressbook`)
* How resources can be managed using `ResourceManager` and automatically disposed when a parent composite is disposed (see `AddressListViewPart` in `com.example.addressbook`).
* How a context menu is created which can be extended using the extension point `org.eclipse.ui.menus` (see `AddressListViewPart` in `com.example.addressbook`) and how this extension point is utilized to contribute commands to the menu (see `plugin.xml` in `com.example.addressbook.editing`).
* How applications can be translated using the Eclipse NLS mechanism (see `ÀddressBookMessages` in `com.example.addressbook`).
* How packages can be structured to keep implementation classes internal and how workbench ids, messages and resources like images can be shared (see `com.example.addressbook.internal` and `com.example.addressbook` packages in `com.example.addressbook`)
* The application utilizes a very simple `AddressBookService` which resembles how a service might look in a real application. Here, it just provides fake data (see `com.example.addressbook.services`).
* How custom `listeners` can be used in services to decouple workbench parts (see `AddressChangeListener` in `com.example.addressbook.services` and usage in `AddressListViewPart`).
* How bundles can be used to extend RCP applications: All editing functionality is provided as a separate bundle `com.example.addressbook.editing` and is completely optional.
* How editors are declared (see `plugin.xml` in `com.example.addressbook.editing`) and implemented with simple load, change and save functionality (see `AddressIdEditorInput` and `AddressEditorPart`).
* How editors are opened from a command (see `OpenAddressEditorHandler` in `com.example.addressbook.editing`)
* How own commands are declared and implemented using a handler (see `plugin.xml` in `com.example.addressbook.editing`).
* How simple forms can be layouted using SWT `GridLayout` (see `AddressEditorPart` in `com.example.addressbook.editing`).
* How a `ComboViewer` is used to choose from a list of objects (see `AddressEditorPart` in `com.example.addressbook`).
* How applications and feature-based products are declared using the extension points `org.eclipse.core.runtime.applications` and
 `org.eclipse.core.runtime.products` for RCP applications (see `plugin.xml`, `addressbook.product` in `com.example.addressbook` and `com.example.addressbook.feature`)
* How `ControlDecoration` can be used to decorate SWT controls and how auto-completion can be added to text fields (see `AddressEditorPart`).

## Running the application

* Use `Window > Plug-in Development > Target Platform` to set up a new target for Eclipse RCP development. It should contain only the `Eclipse RCP SDK` feature from the latest Galileo software site.
* Clone or download a copy of the application and import the projects into Eclipse using `File > Import > Existing projects into workspace`.
* Launch `addressbook.product` as `Eclipse application` from `com.example.addressbook`.

## License

Licensed under [Eclipse Public License - v1.0](http://www.eclipse.org/legal/epl-v10.html)

## Ralf Ebert

* I work as self-employed contractor offering consulting and training services around Eclipse and the Eclipse RCP platform.
* I'm blogging about software development and Eclipse: [http://www.ralfebert.de/blog/](http://www.ralfebert.de/blog/)
* Follow me on Twitter here: [http://twitter.com/ralfebert/](http://twitter.com/ralfebert/)
