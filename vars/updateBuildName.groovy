def call (pomDir = './') {

	echo 'INFO: Setting build name'
    currentBuild.displayName = '#' + getPOMVersion(pomDir) + '(' + env.BUILD_NUMBER + ')'
}