def call(def jobName,def parameters,def wait = true) {

	def parametersList = parameters.inject([]){list, key, value -> list.add(text(name:key, value: value))}
	echo 'INFO: Triggering job ${jobName}'	
    build job: "{jobName}", parameters: parametersList, wait: wait, propagate: false
}