def call(String options = '', String pomDir = '.') {
	
	dir (pomDir) {
	    withMaven {
	    	sh 'mvn sonar:sonar ' + options
	    }
	}
}