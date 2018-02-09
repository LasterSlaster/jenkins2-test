def call (String altDeployRepo = 'snapshots::default::http://172.31.22.80:8081/nexus/content/repositories/snapshots/', String pomDir = '.') {
//TODO: Replace withMaven step to remove the dependeny to a plugin
	dir (pomDir) {
		withMaven {
			sh "mvn clean deploy -Dmaven.buildmode=ci -Dfile.encoding=UTF-8 -DaltDeploymentRepository=${deployRepo} -B -X"
		}
	}
}