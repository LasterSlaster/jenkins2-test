def call (pomDir = './') {

	if (pomDir[-1] != '/' || pomDri[-1] != '\\') {
		pomDir += '/'
	}

	echo 'INFO: Setting build name'
    def pom = readMavenPom file: pomDir + 'pom.xml'
    env.POM_VERSION = pom.version
    currentBuild.displayName = "#${pom.version}(${env.BUILD_NUMBER})"
}