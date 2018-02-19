def call(pomDir = './') {

	if (pomDir[-1] != '/' || pomDir[-1] != '\\') {
		pomDir += '/'
	}
	getVersion(readMavenPom( file: pomDir + 'pom.xml'))
}

@NonCPS
def getVersion(pom) {
	pom.version
}