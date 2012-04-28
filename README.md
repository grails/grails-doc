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

If you want to disable building of translations, you can use:

    ./gradlew -Den.only=true docs

Contributing Documentation
--------------------------

The publishing system for the user guide is the same as [the one for Grails projects][1]. You write your chapters and sections in the gdoc wiki format which is then converted to HTML for the final guide. Each chapter is a top-level gdoc file in the `src/<lang>/guide` directory. Sections and sub-sections then go into directories with the same name as the chapter gdoc but without the suffix.

The structure of the user guide is defined in the `src/<lang>/guide/toc.yml` file, which is a [YAML][2] file. This file also defines the (language-specific) section titles. If you add or remove a gdoc file, you must update the TOC as well!

The `src/<lang>/ref` directory contains the source for the reference sidebar. Each directory is the name of a category, which also appears in the docs. Hence the directories need different names for the different languages. Inside the directories go the gdoc files, whose names match the names of the methods, commands, properties or whatever that the files describe.

Translations
------------

This project can host multiple translations of the user guide, with `src/en` being the main one. To add another one, simply create a new language directory under `src` and copy into it all the files under `src/en`. The build will take care of the rest.

Once you have a copy of the original guide, you can use the `{hidden}` macro to wrap the English text that you have replaced, rather than remove it. This makes it easier to compare changes to the English guide against your translation. For example:

    {hidden}
    When you create a Grails application with the [create-app|commandLine] command,
    Grails doesn't automatically create an Ant @build.xml@ file but you can generate
    one with the [integrate-with|commandLine] command:
    {hidden}

    Quando crias uma aplicação Grails com o comando [create-app|commandLine], Grails
    não cria automaticamente um ficheiro de construção Ant @build.xml@ mas podes gerar
    um com o comando [integrate-with|commandLine]:

Because the English text remains in your gdoc files, 'diff' will show differences on the English lines. You can then use the output of 'diff' to see which bits of your translation need updating. On top of that, the `{hidden}` macro ensures that the text inside it is not displayed in the browser, although you can display it by adding this URL as a bookmark: `javascript:toggleHidden();` (requires you to build the user guide with Grails 2.0 M2 or later).

Even better, you can use the `left_to_do.groovy` script in the root of the project to see what still needs translating. You run it like so:

    ./left_to_do.groovy es

This will then print out a recursive diff of the given translation against the reference English user guide. Anything in {hidden} blocks that hasn't changed since being translated will _not_ appear in the diff output. In other words, all you will see is content that hasn't been translated yet and content that has changed since it was translated. Note that {code} blocks are ignored, so you _don't_ need to include them inside {hidden} macros.

To provide translations for the headers, such as the user guide title and subtitle, just add language specific entries in the `resources/doc.properties` file like so:

    es.title=El Grails Framework
    es.subtitle=...

For each language translation, properties beginning '<lang>.' will override the standard ones. In the above example, the user guide title will be El Grails Framework for the Spanish translation. Also, translators can be credited by adding a '<lang>.translators' property:

    fr.translators=Stéphane Maldini

This should be a comma-separated list of names (or the native language equivalent) and it will be displayed as a "Translated by" header in the user guide itself.

You can build specific translations very easily using the `publishGuide_*` and `publishPdf_*` tasks. For example, to build both the French HTML and PDF user guides, simply execute

    ./gradlew publishPdf_fr

Each translation is generated in its own directory, so for example the French guide will end up in `build/docs/fr`. You can then view the translated guide by opening `build/docs/<lang>/index.html`.

All translations are created as part of the [Hudson CI build for the grails-doc][2] project, so you can easily see what the current state is without having to build the docs yourself.

[1]: http://grails.org/doc/2.0.0.M1/guide/conf.html#docengine
[2]: http://hudson.grails.org/job/grails_docs_2.0.x/lastSuccessfulBuild/artifact/build/docs/
