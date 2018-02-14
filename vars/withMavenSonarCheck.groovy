def call(options = '', pomDir = '.') {
	
	echo 'INFO: Executing maven sonar check'

	dir (pomDir) {
	    withMaven {
	    	bat 'mvn sonar:sonar ' + options
	    }
	}
}