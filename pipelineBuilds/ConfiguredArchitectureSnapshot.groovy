@Library('ACN_HPS_APF_Library')_
	
configMavenBuild {

	mavenOptions = '-Dmaven.buildmode=ci -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B' //Alternative maven repository for maven build 
	pomDir = 'acn-hpsapf-arch-parent'	// path to the directory of the pom - defaults to '.' [currentWS]
	repoURL = 'http://172.31.22.80/gitweb/hpsapf-arch.git' // specifies the url to the repository
	credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	branch = 'refs/heads/master' // specifies the branch to pull form the repo
	browser = 'AssemblaWeb' // specifies the name of the repository browser
	browserURL = '' //specifies the URL for the repository browser e.g. gitlab
	browserVersion = '' // specifies the Version for the repo browser gitlab
	preStepsScript =  {} // steps to execute before the maven build
	postStepsScript = {
		updateBuildName('acn-hpsapf-arch-parent/')
		withMavenSonarCheck('', 'acn-hpsapf-arch-parent')
	} // steps to execute after the maven build
	postBuildActionsScript = {} // steps to execute after the build process

}

	