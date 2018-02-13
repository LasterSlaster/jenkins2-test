@Library('ACN_HPS_APF_Library')_
	
configMavenBuild {

	mavenOptions = '-Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X' //Alternative maven repository for maven build 
	pomDir = 'acn-hpsapf-components-parent'	// path to the directory of the pom - defaults to '.' [currentWS]
	repoURL = 'http://172.31.22.80/gitweb/hpsapf-components.git' // specifies the url to the repository
	branch = 'refs/heads/master' // specifies the branch to pull form the repo
	browser = 'GitWeb' // specifies the name of the repository browser
	browserURL = '/usr/share/gitweb' // specifies the URL for the repository browser e.g. gitlab
	browserVersion = '' // specifies the Version for the repo browser gitlab
	credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	preStepsScript =  {
		updateBuildName('acn-hpsapf-components-parent')
		withMavenDeploy('-Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X', 'accenture-hpsapf-security-jboss-extension', 'accenture-hpsapf-security-jboss-extension')
	} // steps to execute before the maven build
	postStepsScript = {
		if (currentBuild.result == 'SUCCESS') {
			//TODO: Add settings file and global settings file to maven deploy as in jenkins job
			withMavenDeploy('-Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X', 'accenture-hpsapf-security-jboss-extension', 'accenture-hpsapf-security-jboss-extension')
			withMavenSonarCheck('', 'acn-hpsapf-components-parent')
		}
	} // steps to execute after the maven build
	postBuildActionsScript = {} // steps to execute after the build process
}

	