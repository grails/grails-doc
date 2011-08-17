Grails User/Reference Guide
===========================

This is the project for generating the [Grails user & reference guide][Grails Documentation] that explains how to build applications with the [Grails][Grails] framework.

[Grails Documentation]: http://grails.org/doc/latest
[Grails]: http://grails.org

Building the Guide
------------------

To build the documentation, simply type:

    ./gradlew docs

Be warned: this command can take a while to complete and you should probably increase your Gradle memory settings by giving the `GRADLE_OPTS` environment variable a value like

    export GRADLE_OPTS="-Xmx512m -XX:MaxPermSize=384m"

Fortunately, you can reduce the overall build time with a couple of useful options. The first allows you to specify the location of the Grails source to use:

    ./gradlew -Dgrails.home=/home/user/projects/grails-core docs 

The Grails source is required because the guide links to its API documentation and the build needs to ensure it's generated. If you don't specify a `grails.home` property, then the build will fetch the Grails source - a download of 10s of megabytes. It must then compile the Grails source which can take a while too.

Additionally you can create a local.properties file with this variable set:

    grails.home=/home/user/projects/grails-core

or

    grails.home=../grails-core

The other useful option allows you to disable the generation of the API documentation, since you only need to do it once:

    ./gradlew -Ddisable.groovydocs=true docs

Again, this can save a significant amount of time and memory.

The main English user guide is generated in the `build/docs` directory, with the `guide` sub-directory containing the user guide part and the `ref` folder containing the reference material. To view the user guide, simply open `build/docs/index.html`.

Contributing Documentation
--------------------------

The publishing system for the user guide is the same as [the one for Grails projects][1]. You write your chapters and sections in the gdoc wiki format which is then converted to HTML for the final guide. Each chapter is a top-level gdoc file in the `src/<lang>/guide` directory. Sections and sub-sections then go into directories with the same name as the chapter gdoc but without the suffix.

The structure of the user guide is defined in the `src/<lang>/guide/toc.yml` file, which is a [YAML][2] file. This file also defines the (language-specific) section titles. If you add or remove a gdoc file, you must update the TOC as well!

The `src/<lang>/ref` directory contains the source for the reference sidebar. Each directory is the name of a category, which also appears in the docs. Hence the directories need different names for the different languages. Inside the directories go the gdoc files, whose names match the names of the methods, commands, properties or whatever that the files describe.

Translations
------------

This project can host multiple translations of the user guide, with `src/en` being the main one. To add another one, simply create a new language directory under `src` and copy into it all the files under `src/en`. The build will take care of the rest.

You can build specific translations very easily using the `publishGuide_*` and `publishPdf_*` tasks. For example, to build both the French HTML and PDF user guides, simply execute

    ./gradlew publishPdf_fr

Each translation is generated in its own directory, so for example the French guide will end up in `build/docs/fr`. You can then view the translated guide by opening `build/docs/<lang>/index.html`.

[1]: http://grails.org/doc/2.0.0.M1/guide/conf.html#docengine
