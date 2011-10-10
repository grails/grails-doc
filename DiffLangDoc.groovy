

def cli = new CliBuilder(usage: 'groovy DiffDoc.groovy -l "target lang" [-h] [-c]')

cli.h(longOpt:'help', 'Show usage information and quit')
cli.c(longOpt:'copy', required:false, 'copy the file to target lang dir if no exist')
cli.l(argName:'target language', longOpt:'lang', args:1, required:true, 'the target language in src, REQUIRED')

def opt = cli.parse(args)
def sep = File.separator

if (!opt) {
    println "\nInvalid command line, exiting..."
    System.exit(0)
} else if (opt.h) {
    cli.usage()
} else {
    def ant = new AntBuilder()
    def lanStr = "src${sep}${opt.l}${sep}"
    def lanLen = lanStr.size()
    def lanDir = new File(lanStr)
    if (!lanDir.exists()) {
        println "target language does not exist."
        System.exit(0)
    }
    def enStr = "src${sep}en${sep}"
    def enLen = enStr.size()
    def enDir = new File(enStr)
    
    def gdocMap = [:]
    enDir.traverse(nameFilter:~/.*\.gdoc/){
        gdocMap[it.path[enLen..-1]] = it
    }
    lanDir.traverse(nameFilter:~/.*\.gdoc/){
        gdocMap.remove(it.path[lanLen..-1])
    }
    println "files in en not ${opt.l}:"
    gdocMap.each {key,value->
        if (opt.c) {
            def lanFile = new File(lanStr+key)
            if (!lanFile.parentFile.exists()) {
                lanFile.parentFile.mkdirs()
            }
            ant.copy(file:value,todir:lanFile)
        }else {
            println "\t${key}"
        }
    }
}
