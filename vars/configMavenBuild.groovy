package vars

def call (body) {

	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	
	def mavenOptions = config.mavenOptions ?: ''
	def pomDir = config.pomDir ?: ''
	def repoURL = config.repoURL ?: ''	
	def branch = config.branch ?: ''
	def browser = config.browser ?: ''
	def browserURL = config.browserURL ?: ''
	def browserVersion = config.browserVersion ?: ''
	def credentialsID	= config.credentialsID ?: ''
	def preStepsScript = config.preStepsScript ?: {}
	def postStepsScript = config.postStepsScript ?: {}
	def postBuildActionsScript = config.postBuildActionsScript ?: {}

	def preStepsStatus
	def mavenBuildStatus
	def postStepsStatus
	def postBuildActionStatus

	node {
		timestamps {
			try {	
				cleanWs()
				currentBuild.result = 'SUCCESS'

				echo 'INFO: Checking-out repository'

				checkoutGitRepo(repoURL, credentialsID, branch, browser, browserURL, browserVersion)
				
				echo 'INFO: Setting environment variable for pom version'
				env.POM_VERSION = getPOMVersion(pomDir)

				preStepsStatus = executeBuildSteps(preStepsScript, 'Pre Steps')

				if (preStepsStatus != 'FAILURE') {
					mavenBuildStatus = executeBuildSteps({withMavenDeploy(pomDir, mavenOptions)}, 'Maven build')

					postStepsStatus = executeBuildSteps(postStepsScript, 'Post Steps')
				}

				postBuildActionStatus = executeBuildSteps(postBuildActionsScript, 'Post-build Actions')
			} catch (e) {
				echo 'ERROR: This job ended unexpectedly!\nStack trace:\n' + e
				currentBuild.result = 'FAILURE'
				throw e
			} finally {
				echo 'INFO: Sending email'
				emailBuildStatus()

				echo '\n'
				echo '************************************************'
				echo 'Pre steps status: ' + preStepsStatus
				echo 'Maven build status: ' + mavenBuildStatus
				echo 'Post steps status: ' + postStepsStatus
				echo 'Post build actions status: ' + postBuildActionStatus
				echo 'Job ends with current status: ' + currentBuild.result
				echo '************************************************'
			}
		}
	}
}