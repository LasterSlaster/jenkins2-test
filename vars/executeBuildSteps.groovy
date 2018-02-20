def call(script = {}, name = 'Step') {

	echo 'INFO: Execute ' + name
	try {
		error('test')
		script()
	} catch (e) {
		echo 'ERROR: ' + name + ' FAILED\n' + e.getMessage()
		if (currentBuild.result != 'UNSTABLE') {
			currentBuild.result = 'FAILURE'
		}
	} finally {
		if (currentBuild.result == 'UNSTABLE') {
			echo 'WARNING: ' + name + ' is UNSTABLE'
		} else if (currentBuild.result == 'SUCCESS' || currentBuild.result == null) {
			echo 'INFO: ' + name + ' is SUCCESS'
			currentBuild.result = 'SUCCESS'
		}
	}
	return currentBuild.result
}