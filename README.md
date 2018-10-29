[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/mcont)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/mcont.svg)](https://vaadin.com/directory/component/mcont)

# MCont Add-on for Vaadin 7

MCont is a data source add-on for Vaadin 7 that provides bean subclass support for BeanContainer.

## Online demo

Try the add-on demo at http://hesara.app.fi/mcont-demo/

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/mcont

## Building and running demo

git clone http://github.com/hesara/mcont
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e (install it from Eclipse Marketplace if not included in Eclipse)
- command line git or EGit Eclipse plug-in (install it from Eclipse Marketplace if not included in Eclipse)
- Google Chrome

### Importing project

Choose File > Import... > Existing Maven Projects

### Debugging server-side

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your mcont-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the mcont-demo project and choose Debug As > Maven build... and use the goal jetty:run. Navigate to http://localhost:8080/mcont-demo/ to see the application.

 
## Release notes

### Version 0.1.0
- ExtensibleBeanContainer that supports multiple subclasses of a bean class with different properties
- NullProperty - a read-only Property implementation that always returns null as its value

See the features list below for more information

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases.
That said, the following features are planned for upcoming releases:
- More efficient handling of metadata
- Possibly filtering support

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Use the Vaadin core coding conventions and formatting settings - see http://dev.vaadin.com/wiki/CodingConventions
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

MCont is written by Henri Sara

# Developer Guide

## Getting started

See the unit tests and the demo mentioned above (src/main/java/org/vaadin/template/demo/DemoUI.java).

## Features

### ExtensibleBeanContainer

An alternative for BeanContainer that supports multiple bean subclasses which may have different properties.

The available properties are scanned for the base class and all subclasses given to the constructor of the container.
Although other subclasses of the bean base class can be used in the container, only the properties of the initially listed classes are available in the container.

Items whose beans do not have one of the properties of the container return null as the property value (from a read-only NullProperty).

Properties of items in the container are only instantiated when they are accessed, leading to a lower initial memory and CPU overhead than Bean(Item)Container.
However, the Property instances are not cleaned up after access so the memory usage can grow up to a size approaching that of a corresponding Bean(Item)Container if the whole container is accessed. Some metadata are shared between items/properties (Method instances etc.) to reduce memory consumption.

Either explicit item IDs or an ID generator can be used as with BeanContainer. 

### NullProperty

A read-only Property implementation that always returns null.
The instances of NullProperty are shared by data type and the property does not support listeners.
The method get(Class propertyType) should be used to obtain an instance of NullProperty.

See the javadoc for more information. 

