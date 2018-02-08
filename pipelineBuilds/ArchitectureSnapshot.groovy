package main

node {
    timestamps {
        try {
            currentBuild.result = 'SUCCESS'
            checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/master']], browser: [$class: 'AssemblaWeb', repoUrl: ''], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'myID', url: 'http://172.31.22.80/gitweb/hpsapf-arch.git']]])

            //Build
            try {
                withMaven {
                    //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                    sh "mvn clean deploy -Dmaven.buildmode=ci -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B"
                }
                echo 'INFO: Maven build was SUCCESSFULL'
            } catch (e) {
                //TODO: Check the right test result directory
                step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
                if (currentBuild.result == 'UNSTABLE') {
                    echo 'WARNING: Maven build is UNSTABLE'
                } else {
                    echo 'WARNING: Maveen build FAILED'
                    currentBuild.result = 'FAILURE'
                }
            }

            //Post Steps
            if (currentBuild.result == 'SUCCESS') {
                echo 'INFO: Setting build name'
                pom = readMavenPom file: 'pom.xml'
                currentBuild.displayName = "$pom.version($env.BUILD_NUMBER)"
                echo 'INFO: Executing sonar check'
                withMaven {
                    sh "mvn sonar:sonar -B"
                }
            }
        } finally {
            /*if (currentBuild.result == 'SUCCESS' || currentBuild.result == 'UNSTABLE') {
                def triggeredJob = build job: 'hpsapf-components.git snapshot'
            }*/
        }
    }
}
