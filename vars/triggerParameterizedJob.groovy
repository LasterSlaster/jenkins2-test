def call(def jobName, def parameters, def wait = true) {

	def jobStatus
	def parametersList = parameters.inject([]){list, key, value -> list << string(name:key, value: value)}
	echo 'INFO: Triggering job ' + jobName	
   	jobStatus = build job: jobName, parameters: parametersList, wait: wait, propagate: false
    if (wait) {
        switch (jobStatus.result) {
            case 'FAILURE':
                currentBuild.result = 'FAILURE'
                error('ERROR: Triggered Job ' + jobName + ' FAILED')
                break
            case 'UNSTABLE':
                currentBuild.result = 'UNSTABLE'
                echo 'WARNING: Triggered Job ' + jobName + ' is UNSTABLE'
                break
            case 'SUCCESS':
                currentBuild.result = 'SUCCESS'
                echo 'INFO: Triggered Job ' + jobName + ' was SUCCESSFULL'
        }
    }
}