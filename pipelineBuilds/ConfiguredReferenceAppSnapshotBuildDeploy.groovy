@Library('ACN_HPS_APF_Library')_
	
configMavenBuild {

	//TODO: Check maven build options. THere are some missing in the standart config. MAybe add parameter for custom options
	mavenOptions = ' -X -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -B -DdeployRepoUrl=http://172.31.22.80:8081/nexus/content/repositories/snapshots/ -DdeployRepo=snapshots' // specify the maven options
	pomDir = 'acn-hpsapf-referenceapplication-parent'// path to the directory of the pom - defaults to '.' [currentWS]
	repoURL = 'http://172.31.22.80/gitweb/hpsapf-referenceapplication.git' // specifies the url to the repository
	credentialsID = 'myID' // specifies the jenkins id for the repository cedentials
	branch = 'refs/heads/master' // specifies the branch to pull form the repo
	browser = 'GitWeb' // specifies the name of the repository browser
	browserURL = '/usr/share/gitweb' // specifies the URL for the repository browser e.g. gitlab
	browserVersion = '' // specifies the Version for the repo browser gitlab
	preStepsScript =  {} // steps to execute before the maven build
	postStepsScript = {
		if (currentBuild.result == 'SUCCESS' || currentBuild.result == 'UNSTABLE') {
			updateBuildName('acn-hpsapf-referenceapplication-parent')
			//withMavenSonarCheck('','acn-hpsapf-referenceapplication-parent')
			triggerParameterizedJob('hpsapf-referenceapplication-deployment-staticcontent', ['Version': env.POM_VERSION, 'ArtifactId': 'accenture-hpsapf-referenceapplication-gui', 'GroupId' : 'com.accenture.hpsapf', 'Stage' : 'developement', 'Repository' : 'snapshots'], false)
			triggerParameterizedJob('hpsapf-referenceapplication-deployment', ['Version': env.POM_VERSION, 'ArtifactId': 'accenture-hpsapf-referenceapplication-gui', 'GroupId' : 'com.accenture.hpsapf', 'Stage' : 'developement', 'Repository' : 'snapshots', 'ApplicationType' : 'Gui'])
			triggerParameterizedJob('hpsapf-referenceapplication-deployment', ['Version': env.POM_VERSION, 'ArtifactId': 'accenture-hpsapf-masterdatamanagement-webservices', 'GroupId' : 'com.accenture.hpsapf', 'Stage' : 'developement', 'Repository' : 'snapshots', 'ApplicationType' : 'Webservice'])
			triggerParameterizedJob('hpsapf referenceapp batches linux deploy', ['Version': env.POM_VERSION, 'Stage' : 'developement', 'Repository' : 'snapshots', 'BuildMode' : 'snapshot', 'ApplicationType' : 'Batch'])
			triggerParameterizedJob('hpsapf referenceapp batches windows deploy', ['Version': env.POM_VERSION, 'Stage' : 'developement', 'Repository' : 'snapshots', 'BuildMode' : 'snapshot', 'ApplicationType' : 'Batch'])
		} else {
			error('ERROR: Skiped post steps because currentBuild result is neither SUCCESS nor UNSTABLE')
		}
	} // steps to execute after the maven build
	postBuildActionsScript = {} // steps to execute after the build process

}

	