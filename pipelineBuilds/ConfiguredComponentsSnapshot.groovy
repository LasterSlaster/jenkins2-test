@Library(ACN_HPS_APF_Library)_
	
configMavenBuild {

	def mavenAltDeployRepo = 'snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/' //Alternative maven repository for maven build 
	def pomDir = 'acn-hpsapf-components-parent'	// path to the directory of the pom - defaults to '.' [currentWS]
	def repoURL = 'http://172.31.22.80/gitweb/hpsapf-components.git' // specifies the url to the repository
	def branch = 'refs/heads/master' // specifies the branch to pull form the repo
	def browser = 'GitWeb' // specifies the name of the repository browser
	def browserURL = '/usr/share/gitweb' // specifies the URL for the repository browser e.g. gitlab
	def browserVersion = '' // specifies the Version for the repo browser gitlab
	def credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	def preStepsScript =  {
		updateBuildName('acn-hpsapf-components-parent')
		withMavenDeploy('snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/', 'accenture-hpsapf-security-jboss-extension', branch, browserURL, browserVersion)
	} // steps to execute before the maven build
	def postStepsScript = {
		if (currentBuild.result == 'SUCCESS') {
			withMavenDeploy('snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/', 'accenture-hpsapf-security-jboss-extension', branch, browserURL, browserVersion)
			withMavenSonarCheck('acn-hpsapf-components-parent')
		}
	} // steps to execute after the maven build
	def postBuildActionsScript = {} // steps to execute after the build process

}

	