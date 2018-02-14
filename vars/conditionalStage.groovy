def call(jobName, stageName, stages, step) {

	def jobResult

    stage(stageName) {
        if (stages.contains(step)) {
            echo 'INFO: Triggering job ' + jobName
            jobResult = build job: jobName, propagate: false
            if (jobResult.result == 'UNSTABLE') {
                echo 'ERROR: Stage ' + stageName + ' is UNSTABLE'
            }
            if (jobResult.result == 'FAILURE') {
                error('ERROR: Stage ' + stageName + ' FAILED')
            }
        }
    }
    return jobResult
}