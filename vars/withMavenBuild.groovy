def call (String options = '', String pomDir = '.') {
//TODO: Replace withMaven step to remove the dependeny to a plugin
	dir (pomDir) {
		withMaven {
			sh 'mvn clean build ' + options
		}
	}
}