def call(String options = '', String pomDir = '.') {
	
	echo 'INFO: Executing maven sonar check'

	dir (pomDir) {
	    withMaven {
	    	sh 'mvn sonar:sonar ' + options
	    }
	}
}