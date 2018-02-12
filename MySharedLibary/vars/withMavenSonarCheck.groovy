def call(String pomDir = '.', String options = '') {
	
	dir (pomDir) {
	    withMaven {
	    	sh "mvn sonar:sonar ${options}"
	    }
	}
}