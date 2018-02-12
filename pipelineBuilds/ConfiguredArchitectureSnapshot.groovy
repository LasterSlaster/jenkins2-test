@Library('ACN_HPS_APF_Library')_
	
configMavenBuild {

	def mavenOptions = '-Dmaven.buildmode=ci -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B' //Alternative maven repository for maven build 
	def pomDir = 'acn-hpsapf-arch-parent'	// path to the directory of the pom - defaults to '.' [currentWS]
	def repoURL = 'http://172.31.22.80/gitweb/hpsapf-arch.git' // specifies the url to the repository
	def credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	def branch = 'refs/heads/master' // specifies the branch to pull form the repo
	def browser = 'AssemblaWeb' // specifies the name of the repository browser
	def browserURL = '' //specifies the URL for the repository browser e.g. gitlab
	def browserVersion = '' // specifies the Version for the repo browser gitlab
	def preStepsScript =  {} // steps to execute before the maven build
	def postStepsScript = {
		updateBuildName('acn-hpsapf-arch-parent/')
		withMavenSonarCheck('', 'acn-hpsapf-arch-parent')
	} // steps to execute after the maven build
	def postBuildActionsScript = {} // steps to execute after the build process

}

	