def call(script = {}, name = 'Step') {

	echo 'INFO: Execute ' + name
	def stepStatus
	try {
		script()
	} catch (e) {
		echo 'ERROR: ' + name + ' FAILED\n' + e.getMessage()
		stepStatus = 'FAILURE'
		currentBuild.result = 'FAILURE'
	}
	if (stepStatus != 'FAILURE') {
		echo 'INFO: ' + name + ' is SUCCESS'
		stepStatus = 'SUCCESS'
		currentBuild.result = 'SUCCESS'
	}
	return stepStatus
}