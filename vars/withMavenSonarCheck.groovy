def call(options = '', pomDir = '.') {
	
	echo 'INFO: Executing maven sonar check'

	dir (pomDir) {
	    withMaven {
	    	sh 'mvn sonar:sonar ' + options
	    }
	}
}