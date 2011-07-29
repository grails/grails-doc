Grails Documentation
===

[Grails Documentation][Grails Documentation] is the reference user guide for building applications with the [Grails][Grails] framework.
[Grails Documentation]: http://grails.org/doc/latest
[Grails]: http://grails.org

Getting Started
---

To build the documentation, simply type:

    gradlew docs

Building the documentation relies on access to a copy of the Grails source code.  By default the build will download a copy of the source code from Github.  The archive downloaded will be snaphshot of the latest version of the project, not the entire project history.  Because of that, the download is smaller than a full clone of the repository but is still fairly large (approximately 40mb).  To avoid the download you may take advantage of a local copy of the source tree by setting the grails.home system property and assigning it a value that points to a copy of the grails project:

    gradlew docs -Dgrails.home=/home/user/projects/grails-core

Additionally you can create a local.properties file with this variable set:

    grails.home=/home/user/projects/grails-core

or

    grails.home=../grails-core
