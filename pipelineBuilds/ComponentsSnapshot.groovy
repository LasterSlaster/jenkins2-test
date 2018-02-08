package main

node { 
    try {
        timestamps {
            cleanWs()
            currentBuild.result = 'SUCCESS'
            checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/master']], browser: [$class: 'GitWeb', repoUrl: '/usr/share/gitweb'], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'myID', url: 'http://172.31.22.80/gitweb/hpsapf-components.git']]])

            //Pre Steps
            //TODO: Check if a failure in Pre Steps fails the build
            // This step is defined twice in the ui once without any parameters
            echo 'INFO: Setting build name'
            dir ('acn-hpsapf-components-parent') {
                pom = readMavenPom file: 'pom.xml'
                currentBuild.displayName = "$pom.version($env.BUILD_NUMBER)"

                //Invoke top-level Maven targets
                echo 'INFO: Executing Maven build'
                //TODO: Check if test results must be archived
                withMaven {
                    //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                    sh "mvn clean deploy -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X"
                }

                //Build
                try {
                    echo 'INFO: Executing Maven build'
                    withMaven {
                        //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                        sh "mvn clean deploy -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X"
                    }
                } catch (e) {
                    echo 'WARNING: Maven build threw exception'
                } finally {
                    //TODO: Check the right test result directory
                    echo 'INFO: Archiving build artifacts'
                    step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
                    step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
                    if (currentBuild.result == 'UNSTABLE') {
                        echo 'WARNING: Maven build is UNSTABLE'
                    } else if (currentBuild.result == 'SUCCESS'){
                        echo 'WARNING: Maveen build FAILED'
                        currentBuild.result = 'FAILURE'
                    }
                }

                //Post Steps
                //TODO: Check if a Post Step failure immediately fails the build
                if (currentBuild.result == 'SUCCESS') {
                    //Maven artifacts?
                    echo 'INFO: Executing Maven deploy'
                    withMaven {
                        //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                        sh "mvn clean deploy -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X"
                    }
                    echo 'INFO: Executing Maven sonar check'
                    withMaven {
                        //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                        sh "mvn sonar:sonar -B"
                    }
                }
            }
        }
    } finally {
        //Check if the Post-build Actions should always be reachable
        /*if (currentBuild.result == 'SUCCESS' || currentBuild.result == 'UNSTABLE') {
            def triggeredJob = build job: 'hpsapf referenceapp snapshot build'
        }*/
    }
}
