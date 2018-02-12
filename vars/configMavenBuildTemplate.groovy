@Library(ACN_HPS_APF_Library)_
	
configMavenBuild {

	def mavenOptions = '' //Alternative maven repository for maven build 
	def pomDir = ''	// path to the directory of the pom - defaults to '.' [currentWS]
	def repoURL = '' // specifies the url to the repository
	def branch = '' // specifies the branch to pull form the repo
	def browser = '' // specifies the name of the repository browser
	def browserURL = '' // specifies the URL for the repository browser e.g. gitlab
	def browserVersion = '' // specifies the Version for the repo browser gitlab
	def credentialsID = '' // specifies the jenkins id for the repository cedentials
	def preStepsScript =  {} // steps to execute before the maven build
	def postStepsScript = {} // steps to execute after the maven build
	def postBuildActionsScript = {} // steps to execute after the build process

}

	