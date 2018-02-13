def call (String mavenOptions = 'snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/', String pomDir = '.') {
//TODO: Replace withMaven step to remove the dependeny to a plugin

	echo 'INFO: Executing maven deploy'

	dir (pomDir) {
		withMaven {
			bat 'mvn clean deploy ' + options
		}
	}
}