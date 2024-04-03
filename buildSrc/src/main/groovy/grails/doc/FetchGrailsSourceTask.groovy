/**
 * Copyright 2024 The Unity Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.doc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Task to fetch the Grails source code.
 *
 * @author Puneet Behl
 * @since 6.2.0
 */
abstract class FetchGrailsSourceTask extends DefaultTask {

    @Optional
    @Input
    String explicitGrailsHome = project.findProperty('grails.home') ?: null

    @Input
    String grailsVersion = System.getenv('TARGET_GRAILS_VERSION')

    @OutputDirectory
    def checkOutDir = project.findProperty('checkOutDir') ?: project.layout.buildDirectory.dir("checkout/grails-source")

    @TaskAction
    void fetchGrailsSource() {
        if (!explicitGrailsHome) {
            ant.mkdir(dir: checkOutDir)

            println "Downloading Grails source code. If you already have a copy " +
                    "of the Grails source code checked out you can avoid this download " +
                    "by setting the grails.home system property to point to your local " +
                    "copy of the source. See README.md for more information."

            def zipFile = "${checkOutDir}/grails-src.zip"
            if (grailsVersion) {
                ant.get(src: "https://github.com/grails/grails-core/archive/refs/tags/v${grailsVersion}.zip", dest: zipFile, verbose: true)
            } else {
                ant.get(src: "https://github.com/grails/grails-core/zipball/${project.githubBranch}", dest: zipFile, verbose: true)
            }

            ant.unzip(src: zipFile, dest: checkOutDir) {
                mapper(type: "regexp", from: "(grails-core-\\S*?/)(.*)", to: "grails-src/\\2")
            }

            ant.chmod(file: "${checkOutDir}/grails-src/gradlew", perm: 700)

            println "Grails source code has been downloaded to ${checkOutDir}"
        } else {
            println "GRAILS HOME=${explicitGrailsHome}"
        }
    }
}

