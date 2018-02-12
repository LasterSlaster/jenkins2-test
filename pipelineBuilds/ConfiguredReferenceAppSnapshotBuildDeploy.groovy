@Library(ACN_HPS_APF_Library)_
	
configMavenBuild {

	//TODO: Check maven build options. THere are some missing in the standart config. MAybe add parameter for custom options
	def mavenOptions = ' -X -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -DdeployRepoUrl=http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -DdeployRepo=snapshots' // specify the maven options
	def pomDir = 'acn-hpsapf-referenceapplication-parent'// path to the directory of the pom - defaults to '.' [currentWS]
	def repoURL = 'http://172.31.22.80/gitweb/hpsapf-referenceapplication.git' // specifies the url to the repository
	def branch = 'refs/heads/master' // specifies the branch to pull form the repo
	def browser = 'GitWeb' // specifies the name of the repository browser
	def browserURL = '/usr/share/gitweb' // specifies the URL for the repository browser e.g. gitlab
	def browserVersion = '' // specifies the Version for the repo browser gitlab
	def credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	def preStepsScript =  {} // steps to execute before the maven build
	def postStepsScript = {
		if (currentBuild.result == 'SUCCESS' || currentBuild.result == 'UNSTABLE') {
			updateBuildName('acn-hpsapf-referenceapplication-parent')
			triggerParameterizedJob()
		}
	} // steps to execute after the maven build
	def postBuildActionsScript = {} // steps to execute after the build process

}

	