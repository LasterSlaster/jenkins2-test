def call (String altDeployRepo = 'snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/', String pomDir = '.', String options = '') {
//TODO: Replace withMaven step to remove the dependeny to a plugin
	dir (pomDir) {
		withMaven {
			sh "mvn clean deploy ${options}"
		}
	}
}