package main

node {   
    try {
        timestamps {
            currentBuild.result = 'SUCCESS'
            checkout changelog: true, poll: true, scm: [$class: 'GitSCM', branches: [[name: 'refs/heads/master']], browser: [$class: 'GitLab', repoUrl: 'http://172.31.22.80/gitweb/hpsapf-tools.git', version: '8.2'], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'myID', url: 'http://172.31.22.80/gitweb/hpsapf-tools.git']]]

            //Build
            dir ('com.accenture.hpsapf.dsl.parent') {
                echo 'INFO: Executing maven build'
                try {
                    withMaven {
                        sh 'mvn clean deploy -X -Pjenkins-build -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -Dmaven.test.skip=false'
                    }
                } catch (e) {
                    echo 'WARNING: Maven build FAILED'
                    currentBuild.result = 'FAILURE'
                } 
            }

            //Post Steps
            if (currentBuild.result == 'SUCCESS') {
                echo 'INFO: Executing shell'
                sh 'temp_dir_name=temp_zs_`date +%d-%m-%y_%H-%M-%S`\nmkdir /tmp/$temp_dir_name\nwget -S -O \"/tmp/$temp_dir_name/com.accenture.hpsapf.dsl.updatesite.zip\" \"http://172.31.22.80:8081/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com.accenture.hpsapf&a=com.accenture.hpsapf.dsl.updatesite&v=$POM_VERSION&p=zip\"\nrm -rf /srv/www/updatesite/development/*\nunzip -o /tmp/$temp_dir_name/com.accenture.hpsapf.dsl.updatesite.zip -d /srv/www/updatesite/development\nrm -rf /tmp/$temp_dir_name'
                
                echo 'INFO: Setting build name'
                dir ('com.accenture.hpsapf.dsl.parent') {
                    pom = readMavenPom file: 'pom.xml'
                    currentBuild.displayName = "${pom.version}(${env.BUILD_NUMBER})"
                }
                
                echo 'INFO: Executing shell'
                sh 'temp_dir_name=temp_zs_`date +%d-%m-%y_%H-%M-%S`\nmkdir /tmp/$temp_dir_name\nwget -S -O \"/tmp/$temp_dir_name/com.accenture.hpsapf.dsl.helpdsl.updatesite.zip\" \"http://172.31.22.80:8081/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com.accenture.hpsapf&a=com.accenture.hpsapf.dsl.helpdsl.updatesite&v=$POM_VERSION&p=zip\"'
            }
        }
    } finally {
        /*if (currentBuild.result == 'SUCCESS') {
            def triggeredJob = build job: 'hpsapf-arch.git snapshot', wait: false
        }*/
    }
}
