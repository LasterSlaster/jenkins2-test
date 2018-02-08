package main

node {
    timestamps {
        cleanWs()
        currentBuild.result = 'SUCCESS'
        checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/master']], browser: [$class: 'GitWeb', repoUrl: '/usr/share/gitweb'], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'myID', url: 'http://172.31.22.80/gitweb/hpsapf-referenceapplication.git']]])

        //Build
        dir ('acn-hpsapf-referenceapplication-parent') {
            try {
                //Build
                echo 'INFO: Executing Maven build'
                withMaven {
                    //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                    sh "mvn clean deploy -X -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -DdeployRepoUrl=http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -DdeployRepo=snapshots"
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
        }

        //Post Steps
        if (currentBuild.result == 'SUCCESS' || currentBuild.result == 'UNSTABLE') {
            dir ('acn-hpsapf-referenceapplication-parent') {
                echo 'INFO: Setting build name'
                pom = readMavenPom file: 'pom.xml'
                currentBuild.displayName = "$pom.version($env.BUILD_NUMBER)"
            
                try {
                    echo 'INFO: Executing Maven sonar check'
                    withMaven {
                        //Parent POM location com.accenture.hpsapf.dsl.parent/pom.xml
                        sh "mvn sonar:sonar -B"
                    }
                } catch (e) {
                    echo 'WARNING: Maveen sonar FAILED'
                    currentBuild.result = 'FAILURE'
                }
            }
            echo 'INFO: Triggering job hpsapf-referenceapplication-deployment-staticcontent'
            build job: 'hpsapf-referenceapplication-deployment-staticcontent', parameters: [text(name: 'Version', value: '$pom.version'), text(name: 'ArtifactId', value: 'accenture-hpsapf-referenceapplication-gui'), text(name: 'GroupId', value: 'com.accenture.hpsapf'), text(name: 'Stage', value: 'development'), text(name: 'Repository', value: 'snapshots')], wait: false

            
            echo 'INFO: Triggering job hpsapf-referenceapplication-deployment'
            def triggeredJobReferenceAppGUI = build job: 'hpsapf-referenceapplication-deployment', parameters: [text(name: 'Version', value: "$pom.version"), text(name: 'ArtifactId', value: 'accenture-hpsapf-referenceapplication-gui'), text(name: 'GroupId', value: 'com.accenture.hpsapf'), text(name: 'Stage', value: 'development'), text(name: 'Repository', value: 'snapshots'), text(name: 'ApplicationType', value: 'Gui')], propagate: false
            
            if (triggeredJobReferenceAppGUI.result == 'FAILURE') {
                currentBuild.result = 'FAILURE'
                //Similar to throwing an error but no stack trace is printed
                error('ERROR: Triggered Job hpsapf-referenceapplication-deployment FAILED')
            } else if (triggeredJobReferenceAppDeployment.result == 'UNSTABLE') {
                currentBuild.result = 'UNSTABLE'
                echo 'WARNING: Triggered Job hpsapf-referenceapplication-deployment is UNSTABLE'
            }
            
            
            echo 'INFO: Triggering job hpsapf-referenceapplication-deployment'
            def triggeredJobReferenceAppWebservices = build job: 'hpsapf-referenceapplication-deployment', parameters: [text(name: 'Version', value: "$pom.version"), text(name: 'ArtifactId', value: 'accenture-hpsapf-masterdatamanagement-webservices'), text(name: 'GroupId', value: 'com.accenture.hpsapf'), text(name: 'Stage', value: 'development'), text(name: 'Repository', value: 'snapshots'), text(name: 'ApplicationType', value: 'Webservice')], propagate: false
        
            if (triggeredJobReferenceAppWebservices.result == 'FAILURE') {
                currentBuild.result = 'FAILURE'
                error('ERROR: Triggered Job hpsapf-referenceapplication-deployment FAILED')
            } else if (triggeredJobReferenceAppDeployment.result == 'UNSTABLE') {
                currentBuild.result = 'UNSTABLE'
                echo 'WARNING: Triggered Job hpsapf-referenceapplication-deployment is UNSTABLE'
            }
            
            
            echo 'INFO: Triggering job hpsapf referenceapp batches linux deploy'
            def triggeredJobReferenceAppBatchesLinux = build job: 'hpsapf referenceapp batches linux deploy', parameters: [text(name: 'Version', value: "$pom.version"), text(name: 'Stage', value: 'development'), text(name: 'Repository', value: 'snapshots'), text(name: 'BuildMode', value: 'snapshot'), text(name: 'ApplicationType', value: 'Batch')], propagate: false
        
            if (triggeredJobReferenceAppBatchesLinux.result == 'FAILURE') {
                currentBuild.result = 'FAILURE'
                error('ERROR: Triggered Job hpsapf referenceapp batches linux deploy FAILED')
            } else if (triggeredJobReferenceAppDeployment.result == 'UNSTABLE') {
                currentBuild.result = 'UNSTABLE'
                echo 'WARNING: Triggered Job hpsapf referenceapp batches linux deploy is UNSTABLE'
            }
            
            
            echo 'INFO: Triggering job hpsapf referenceapp batches windows deploy'
            def triggeredJobReferenceAppBatchesWindows = build job: 'hpsapf referenceapp batches windows deploy', parameters: [text(name: 'Version', value: "$pom.version"), text(name: 'Stage', value: 'development'), text(name: 'Repository', value: 'snapshots'), text(name: 'BuildMode', value: 'snapshot'), text(name: 'ApplicationType', value: 'Batch')], propagate: false
        
            if (triggeredJobReferenceAppBatchesWindows.result == 'FAILURE') {
                currentBuild.result = 'FAILURE'
                error('ERROR: Triggered Job hpsapf referenceapp batches windows deploy FAILED')
            } else if (triggeredJobReferenceAppDeployment.result == 'UNSTABLE') {
                currentBuild.result = 'UNSTABLE'
                echo 'WARNING: Triggered Job hpsapf referenceapp batches windows deploy is UNSTABLE'
            }  
        }
    }
}