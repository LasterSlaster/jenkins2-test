def call (mavenOptions = '', pomDir = '.') {
//TODO: Replace withMaven step to remove the dependeny to a plugin

	echo 'INFO: Executing maven deploy'
	dir (pomDir) {
		withMaven {
			error('eeeeeeeeeerror')
			sh 'mvn clean deploy ' + mavenOptions
		}
	}
}