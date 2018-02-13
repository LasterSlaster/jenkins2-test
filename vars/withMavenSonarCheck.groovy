def call(String options = '', String pomDir = '.') {
	
	echo 'INFO: Executing maven sonar check'

	dir (pomDir) {
	    withMaven {
	    	bat 'mvn sonar:sonar ' + options
	    }
	}
}