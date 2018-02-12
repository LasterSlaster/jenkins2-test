def call(Closure script = {}, String name = 'Step') {

	echo "INFO: Execute ${name}"
	try {
		script()
	} catch (e) {
		echo 'ERROR: ${name} FAILED\n' + e.getMessage
		currentBuild.result = 'FAILURE'
	} finally {
		def status = currentBuild.result
		if (status == 'UNSTABLE') {
			echo 'WARNING: ${name} is UNSTABLE'
		} else if (currentBuild.result == 'SUCCESS' || currentBuild.result == null) {
			echo 'INFO: ${name} is SUCCESS'
			currentBuild.result = 'SUCCEESS'
			status = 'SUCCESS'
		}
		return status
	}
}