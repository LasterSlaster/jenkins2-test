def call (pomDir = './') {

	if (pomDir[-1] != '/' || pomDri[-1] != '\\') {
		pomDir += '/'
	}

	echo 'INFO: Setting build name'
    pom = readMavenPom file: pomDir + 'pom.xml'
    currentBuild.displayName = "${pom.version}(${env.BUILD_NUMBER})"
}