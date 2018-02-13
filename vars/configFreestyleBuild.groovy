package vars

def call (body) {
	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	def buildStepsScript = config.buildStepsScript ?: {}
	def postBuildActionsScript = config.postBuildActionsScript ?: {}
	
	def buildStepsStatus
	def postBuildActionStatus

	node {
		timestamps {
			try {
				cleanWs()
				currentBuild.result = 'SUCCESS'

				echo 'INFO: Checking-out repository'
				checkoutGitRepo(repoURL, credentials, branch)
				
				buildStepsStatus = executeBuildSteps(buildStepsScript, 'Build steps')

				postBuildActionStatus = executeBuildSteps(postBuildActionsScript, 'Post-build Actions')
			} catch (e) {
				echo 'ERROR: This job ended unexpectedly!\nStack trace:\n' + e
				error('ERROR: This job ended unexpectedly!\nStack trace:\n' + e)
			} finally {
				echo 'INFO: Sending email'
				emailBuildStatus()
				
				echo '\n'
				echo '************************************************'
				echo 'Build steps status: ' + buildStepsStatus
				echo 'Post build actions status: ' + postBuildActionStatus
				echo 'Job ends with current status: ' + currentBuild.result
				echo '************************************************'
			}
		}
	}
}