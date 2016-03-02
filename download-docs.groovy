def ant = new AntBuilder()

def versions = [
            ['1.2.0', '1.2.1', '1.2.2', '1.2.3', '1.2.4', '1.2.5'],
            ['1.3.0', '1.3.1', '1.3.2', '1.3.3', '1.3.4', '1.3.5', '1.3.6', '1.3.7', '1.3.8', '1.3.9'],
            ['2.0.0', '2.0.1', '2.0.2', '2.0.3', '2.0.4'],
            ['2.1.0', '2.1.1', '2.1.2', '2.1.3', '2.1.4', '2.1.5'],
            ['2.2.0', '2.2.1', '2.2.2', '2.2.3', '2.2.4', '2.2.5'],
            ['2.3.0', '2.3.1', '2.3.2', '2.3.3', '2.3.4', '2.3.5', '2.3.6', '2.3.7', '2.3.8', '2.3.9', '2.3.10', '2.3.11'],
            ['2.4.0', '2.4.1', '2.4.2', '2.4.3', '2.4.4'] ]



int j = 0
for(group in versions) {
	j++
	int i = 0
	for(version in group) {
		i++
		def local = "${version}.zip"
		def url = new URL("https://github.com/grails/grails-core/releases/download/v${version}/grails-docs-${version}.zip")
		def conn = url.openConnection()
		conn.setInstanceFollowRedirects( false )
		conn.connect()
		def responseCode = conn.responseCode

		if(responseCode.toString().startsWith("3")) {
			url = conn.getHeaderField("Location")
			conn.disconnect()

			ant.get(src:url,
					dest:local)            	

			ant.unzip(src:local, dest:version)		
			ant.delete(file:local)
			if(i == group.size()) {
				def targetDir = version[0..3] + 'x'
				ant.mkdir(dir:targetDir)
				ant.copy(todir:targetDir) {
					fileset(dir:version)
				}
				
				if(j == versions.size()) {
					ant.mkdir(dir:"latest")
					ant.copy(todir:"latest") {
						fileset(dir:version)
					}												
				}
			}
		}
		else {
			if(version.endsWith('0')) {
				version = version[0..2]
			}
			url = new URL("https://github.com/grails/grails-core/releases/download/v${version}/grails-docs-${version}.0.zip")
			conn = url.openConnection()
			conn.setInstanceFollowRedirects( false )
			conn.connect()
			responseCode = conn.responseCode

			if(responseCode.toString().startsWith("3")) {
				url = conn.getHeaderField("Location")
				conn.disconnect()

				ant.get(src:url,
						dest:local)            	

				ant.unzip(src:local, dest:"${version}.0")		
				ant.delete(file:local)
	
			}
			else {
				if(version.endsWith('0')) {
					version = version[0..2]
				}
				println "WARNING: Not found $url"
			}			
		}

	}
}
