package vars

def call (body) {

	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	
	config.mavenOptions = config.mavenOptions ?: ''
	config.pomDir = config.pomDir ?: ''
	config.repoURL = config.repoURL ?: ''	
	config.branch = config.branch ?: ''
	config.browser = config.browser ?: ''
	config.browserURL = config.browserURL ?: ''
	config.browserVersion = config.browserVersion ?: ''
	config.credentialsID = config.credentialsID ?: ''
	config.preStepsScript = config.preStepsScript ?: {}
	config.preStepsScript.delegate = this
	config.postStepsScript = config.postStepsScript ?: {}
	config.postStepsScript.delegate = this
	def test = config.postStepsScript
	config.postBuildActionsScript = config.postBuildActionsScript ?: {}
	config.postBuildActionsScript.delegate = this

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

				checkoutGitRepo(config.repoURL, config.credentialsID, config.branch, config.browser, config.browserURL, config.browserVersion)
				
				echo 'INFO: Setting environment variable for pom version'
				env.POM_VERSION = getPOMVersion(config.pomDir)

				preStepsStatus = executeBuildSteps(config.preStepsScript, 'Pre Steps')

				if (preStepsStatus != 'FAILURE') {
					mavenBuildStatus = executeBuildSteps({withMavenDeploy(config.pomDir, config.mavenOptions)}, 'Maven build')
					test()
					postStepsStatus = executeBuildSteps(config.postStepsScript, 'Post Steps')
				}

				postBuildActionStatus = executeBuildSteps(config.postBuildActionsScript, 'Post-build Actions')
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