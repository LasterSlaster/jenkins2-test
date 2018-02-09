package main

node {    
    try {
        timestamps {
            cleanWs()
            currentBuild.result = 'SUCCESS'
            checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/master']], browser: [$class: 'AssemblaWeb', repoUrl: ''], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'myID', url: 'http://172.31.22.80/gitweb/hpsapf-arch.git']]])

            //Build
            dir ('acn-hpsapf-arch-parent') {
                echo 'INFO: Executing maven build'
                try {
                    withMaven {
                         sh "mvn clean deploy -Dmaven.buildmode=ci -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B"
                    }
                } catch (e) {
                    echo 'WARNING: Maven build FAILED'
                    currentBuild.result = 'FAILURE'
                }

                //Post Steps 
                echo 'INFO: Setting build name'
                pom = readMavenPom file: 'pom.xml'
                currentBuild.displayName = "${pom.version}(${env.BUILD_NUMBER})"
                echo 'INFO: Executing sonar check'
                withMaven {
                    sh "mvn sonar:sonar -B"
                }
            }
        }
    } finally {
        /*if (currentBuild.result == 'SUCCESS' || currentBuild.result == 'UNSTABLE') {
            def triggeredJob = build job: 'hpsapf-components.git snapshot'
        }*/
    }
}
