@Library('ACN_HPS_APF_Library')_
	
configMavenBuild {

	def mavenOptions = '-Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X' //Alternative maven repository for maven build 
	def pomDir = 'acn-hpsapf-components-parent'	// path to the directory of the pom - defaults to '.' [currentWS]
	def repoURL = 'http://172.31.22.80/gitweb/hpsapf-components.git' // specifies the url to the repository
	def branch = 'refs/heads/master' // specifies the branch to pull form the repo
	def browser = 'GitWeb' // specifies the name of the repository browser
	def browserURL = '/usr/share/gitweb' // specifies the URL for the repository browser e.g. gitlab
	def browserVersion = '' // specifies the Version for the repo browser gitlab
	def credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	def preStepsScript =  {
		updateBuildName('acn-hpsapf-components-parent')
		withMavenDeploy('-Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X', 'accenture-hpsapf-security-jboss-extension')
	} // steps to execute before the maven build
	def postStepsScript = {
		if (currentBuild.result == 'SUCCESS') {
			//TODO: Add settings file and global settings file to maven deploy as in jenkins job
			withMavenDeploy('-Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -X', 'accenture-hpsapf-security-jboss-extension')
			withMavenSonarCheck('', 'acn-hpsapf-components-parent')
		}
	} // steps to execute after the maven build
	def postBuildActionsScript = {} // steps to execute after the build process
}

	