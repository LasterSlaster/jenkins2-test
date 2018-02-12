package vars

def call (body) {
	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	def buildStepsScript = (config.buildStepsScript) ?: {}
	def postBuildActionsScript = (config.postBuildActionsScript) ?: {}
	
	def buildStepsStatus
	def postBuildActionStatus

	node {
		timestamps {
			try {
				cleanWS()
				currentBuild.result = 'SUCCESS'

				echo 'INFO: Checking-out repository'
				checkoutGitRepo(repoURL, credentials, branch)
				
				buildStepsStatus = executeBuildSteps(buildStepsScript, 'Build steps')

				postBuildActionStatus = executeBuildSteps(postBuildActionsScript, 'Post-build Actions')
			} finally {
				echo 'INFO: Sending email'
				emailBuildStatus()
				
				echo '\n'
				echo '************************************************'
				echo 'Build steps status: ' + buildStepsStatus
				echo 'Post build actions status: ' + postBuildActionStatus
				echo 'Job ends with status: ' + currentBuild.result
				echo '************************************************'
			}
		}
	}
}