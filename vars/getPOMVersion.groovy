def call(pomDir = './') {

	if (pomDir[-1] != '/' || pomDri[-1] != '\\') {
		pomDir += '/'
	}
	readMavenPom file: pomDir + 'pom.xml'
}