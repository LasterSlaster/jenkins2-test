@Library('ACN_HPS_APF_Library')_

configMavenBuild {
	mavenOptions = '-X -Plocal-build -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -Dmaven.test.skip=false' //Options for maven command deploy
	pomDir = 'com.accenture.hpsapf.dsl.parent'	// path to the directory of the pom - defaults to '.' [currentWS]
	repoURL = 'http://172.31.22.80/gitweb/hpsapf-tools.git' // specifies the url to the repository
	credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	branch = 'refs/heads/master' // specifies the branch to pull form the repo
	browser = 'GitLab' // specifies the name of the repository browser
	browserURL = 'http://172.31.22.80/gitweb/hpsapf-tools.git' // specifies the URL for the repository browser e.g. gitlab
	browserVersion = '8.2' // specifies the Version for the repo browser gitlab
	preStepsScript =  {} // steps to execute before the maven build
	postStepsScript = {
		if (currentBuild.result == 'SUCCESS') {
			echo 'INFO: Executing shell'
	        sh 'temp_dir_name=temp_zs_`date +%d-%m-%y_%H-%M-%S`\r\nmkdir /tmp/$temp_dir_name\r\nwget -S -O \"/tmp/$temp_dir_name/com.accenture.hpsapf.dsl.updatesite.zip\" \"http://172.31.22.80:8081/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com.accenture.hpsapf&a=com.accenture.hpsapf.dsl.updatesite&v=' + env.POM_VERSION + '&p=zip\"\r\nrm -rf /srv/www/updatesite/development/*\r\nunzip -o /tmp/$temp_dir_name/com.accenture.hpsapf.dsl.updatesite.zip -d /srv/www/updatesite/development\r\nrm -rf /tmp/$temp_dir_name'
		
			updateBuildName('com.accenture.hpsapf.dsl.parent/')

			echo 'INFO: Executing shell'
            sh 'temp_dir_name=temp_zs_`date +%d-%m-%y_%H-%M-%S`\r\nmkdir /tmp/$temp_dir_name\r\nwget -S -O \"/tmp/$temp_dir_name/com.accenture.hpsapf.dsl.helpdsl.updatesite.zip\" \"http://172.31.22.80:8081/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com.accenture.hpsapf&a=com.accenture.hpsapf.dsl.helpdsl.updatesite&v=' + env.POM_VERSION + '&p=zip\"'
		}
	} // steps to execute after the maven build
	postBuildActionsScript = {} // steps to execute after the build process
}	