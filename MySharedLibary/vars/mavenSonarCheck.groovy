def call(String pomDir = '.') {
	
	dir (pomDir) {
	    withMaven {
	    	sh "mvn sonar:sonar -B"
	    }
	}
}