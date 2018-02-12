def call(def jobName, def parameters, def wait = true) {

	def jobStatus
	def parametersList = parameters.inject([]){list, key, value -> list.add(text(name:key, value: value))}
	echo 'INFO: Triggering job ' + jobName	
   	jobStatus = build job: jobName, parameters: parametersList, wait: wait, propagate: false
    if (jobStatus.result == 'FAILURE') {
        currentBuild.result = 'FAILURE'
        //Similar to throwing an error but no stack trace is printed
        error('ERROR: Triggered Job ' + jobName + ' FAILED')
    } else if (jobStatus.result == 'UNSTABLE') {
        currentBuild.result = 'UNSTABLE'
        echo 'WARNING: Triggered Job ' + jobName + ' is UNSTABLE'
    }
}