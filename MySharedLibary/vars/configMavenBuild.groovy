def call (body) {

	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	
	def mavenAltDeployRepo = (config.mavenAltDeployRepo == null) ? '' :config.mavenAltDeployRepo
	def pomDir = (config.pomDir == null) ? '' : config.pomDir	
	def branch = (config.branch == null) ? '' : config.branch
	def browserURL = (config.browserURL == null) ? '' : config.browserURL
	def browserVersion = (config.browserVersion == null) ? '' : config.browserVersion
	def preStepsScript = (config.preStepsScript == null) ? {} : config.preStepsScript
	def postStepsScript = (config.postStepsScript == null) ? {} : config.postStepsScript
	def postBuildActionsScript = (config.postBuildActionsScript == null) ? {} : config.postBuildActionsScript

	def preStepsStatus
	def mavenBuildStatus
	def postStepsStatus
	def postBuildActionStatus

	node {
		timestamps {
			try {	
				cleanWS()
				currentBuild.result = 'SUCCESS'

				echo 'INFO: Checking-out repository'
				checkoutGitRepo(repoURL, credentials, branch)
				
				preStepsStatus = executeBuildSteps(preStepsScript, 'Pre Steps')

				if (preStepsStatus != 'FAILURE') {
					mavenBuildStatus = executeBuildSteps({mavenDeploy(mavenAltDeployRepo, pomDir, branch, browserURL, browserVersion)}, 'Maven build')

					postStepsStatus = executeBuildSteps(postStepsScript, 'Post Steps')
				}

				postBuildActionStatus = executeBuildSteps(postBuildActionsScript, 'Post-build Actions')
			} finally {
				echo 'INFO: Sending email'
				emailBuildStatus()

				echo '\n'
				echo '************************************************'
				echo 'Pre steps status: ' + preStepsStatus
				echo 'Maven build status: ' + mavenBuildStatus
				echo 'Post steps status: ' + postStepsStatus
				echo 'Post build actions status: ' + postBuildActionStatus
				echo 'Job ends with status: ' + currentBuild.result
				echo '************************************************'
			}
		}
	}
}