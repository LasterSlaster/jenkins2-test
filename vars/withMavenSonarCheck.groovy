def call(mavenOptions = '', pomDir = '.') {
	
	echo 'INFO: Executing maven sonar check'

	dir (pomDir) {
	    withMaven {
	    	sh 'mvn sonar:sonar ' + mavenOptions
	    }
	}
}