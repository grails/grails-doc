Grails 用户参考手册
===========================

This is the project for generating the [Grails user & reference guide][Grails Documentation] that explains how to build applications with the [Grails][Grails] framework.

这个项目是作为 [Grails user & reference guide][Grails Documentation] 的文档建立而准备的。文档将说明 [Grails][Grails] 框架实施。

[Grails Documentation]: http://grails.org/doc/latest
[Grails]: http://grails.org

筑建文档
------------------

简单输入:

    ./gradlew docs

注意：这个命令将消耗大量内存来筑建整个内容，它也会依赖你设置Gradle的内存设置。
你可以直接设置，类似于：

    export GRADLE_OPTS="-Xmx512m -XX:MaxPermSize=384m"

幸运的是，你可以通过参数来运行此命令。例如一下参数将定位你的grails目录：

    ./gradlew -Dgrails.home=/home/user/projects/grails-core docs 

Grails代码是作为链接API文档的基础，它在筑建时必须生成。如果你没有设置 @grails.home@ 值，那么它会自动下载Grails代码，一个十几兆的内容，且还会花一些时间进行编译。

也可以通过设置 local.preperties 文件来设置这个值：

    grails.home=/home/user/projects/grails-core

或

    grails.home=../grails-core

另一个有用的参数将禁止你生成API文档，如果你确认只要某些东东：

    ./gradlew -Ddisable.groovydocs=true docs

它将节省你大量的时间和内存开销。

英语用户生成的文件在 `build/docs` 中。 `guide`  子目录包括用户说明部分， `ref` 目录包含参考材料。只要直接打开 `build/docs/index.html` 就可查阅文档。

如果你不需要翻译其他语言，你只要这样使用：

    ./gradlew -Den.only=true docs

贡献文档
--------------------------

用户手册文档的发布和 [the one for Grails projects|[1] 项目类似。你按照gdoc wiki 格式写，它最终会相应转换成 html文件。每个章节的顶层是 gdoc 文件，在 @src/<lang>/guide@ 目录中。子章节以及后续章节将按照相同文件名的方式存放在对应子目录下，注意没有前缀。

用户文档的结构定义在 `src/<lang>/guide/toc.yml` 文件中，一个 [YAML][2] 格式文件。它定义每个章节的标题。如果你有增减 gdoc文件，你别忘修改它，把它作为文档的索引文件为好。

`src/<lang>/ref`目录中包括了参考源。每个目录名就是分类的名称，也就是出现文档的名字。因此，不同的目录名就是不同语言。在进入目录中的gdoc文件中，也对应相应的方法，命令，属性或描述性的文字。

翻译
------------

本项目的文档可以支持多语言的翻译，留意 `src/en` 作为主入口。如要添加新语言，你只要在 `src` 目录中建立一个新语种目录，并将 `src/en` 的文件都复制过去，作为翻译的模板文件。筑建会自动处理其他部分。

有了基础语言的文件，你可以开始着手翻译了。一边翻译，一边你可以使用 @\{hidden\}@ 宏来定义英语部分，它不会显示出来的。完成一部分后你可以逐步去掉那些 `{hidden}` 宏——当然是把英语部分整体去掉。这对于你的翻译工作很有益，即可一边看一边翻译，也可以保留一下，为以后做调整做参考。

    {hidden}
    When you create a Grails application with the [create-app|commandLine] command,
    Grails doesn't automatically create an Ant @build.xml@ file but you can generate
    one with the [integrate-with|commandLine] command:
    {hidden}

    Quando crias uma aplicação Grails com o comando [create-app|commandLine], Grails
    não cria automaticamente um ficheiro de construção Ant @build.xml@ mas podes gerar
    um com o comando [integrate-with|commandLine]:

因为英文的gdoc文件作为原始基础， diff 将显示你翻译的语言和英语的不同之处。
总之， `{hidden}` 能禁止将内容输出给浏览器，然而你可以使用 `javascript:toggleHidden();` 加入 URL 作为标签(必须高于 Grails 2.0 M2 及以后版本)。

另外，你还可以使用 在项目根目录中的 `left_to_do.groovy` 脚本来看一下还有那些需要翻译。
运行方式：

    ./left_to_do.groovy es


它会输出翻译与英语翻译手册中的不同。任何在 {hidden} 块中不会因为翻译而变化。也就说，所有的 {code} 将被忽略，所以你不需要将他们放入都 {hidden} 中。

对于翻译中的头部中的一些文字，类似用户文档标题和子标题，请加在 “resources/doc.properties” 文件中：

    es.title=El Grails Framework
    es.subtitle=...

每个语言的翻译，其属性由 @<lang>@ 决定。上面的例子中用户文档标题 将变成 对应的中文翻译。它的格式 就是 '<lang>.translators' 这样的。你当然可以自己来定义，并在翻译文档中体现出来：

    fr.translators=Stéphane Maldini

如果有多个人，可以通过逗号分开(很自然吧)，它会显示在 "Translated by" 的位置上。

你可以方便的通过命令参数 `publishGuide_\*` 和 `publishPdf_\*` 指定任务。例如, 下面的能实现PDF的输出。

    ./gradlew publishPdf_fr

每个翻译的内容都有自己的目录，例如法国版本文档的在 `build/docs/fr` 下。你可以通过 `build/docs/<lang>/index.html` 找到那个目录，并打开访问。

所有的翻译都属于 [Hudson CI build for the grails-doc][2] 项目的一部分，所以你不需要自己去建立文档，而只要递交你修改的gdoc即可。

[1]: http://grails.org/doc/2.0.0.M1/guide/conf.html#docengine
[2]: http://hudson.grails.org/job/grails_docs_2.0.x/lastSuccessfulBuild/artifact/build/docs/
