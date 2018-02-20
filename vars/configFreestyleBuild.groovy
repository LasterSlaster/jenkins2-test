package vars

def call (body) {
	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	config.branch = config.branch ?: ''
	config.credentials = config.credentials ?: ''
	config.repoURL = config.repoURL ?: ''
	config.buildStepsScript = config.buildStepsScript ?: {}
	config.buildStepsScript.resolveStrategy = Closure.DELEGATE_FIRST
	config.buildStepsScript.delegate = this
	config.postBuildActionsScript = config.postBuildActionsScript ?: {}
	config.postBuildActionsScript.resolveStrategy = Closure.DELEGATE_FIRST
	config.postBuildActionsScript.delegate = this
	
	def buildStepsStatus
	def postBuildActionStatus

	node {
		timestamps {
			try {
				cleanWs()
				currentBuild.result = 'SUCCESS'

				echo 'INFO: Checking-out repository'
				checkoutGitRepo(config.repoURL, config.credentials, config.branch)
				
				buildStepsStatus = executeBuildSteps(config.buildStepsScript, 'Build steps')

				postBuildActionStatus = executeBuildSteps(config.postBuildActionsScript, 'Post-build Actions')
			} catch (e) {
				echo 'ERROR: This job ended unexpectedly!\nStack trace:\n' + e.getMessage()
				currentBuild.result = 'FAILURE'
				throw e
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