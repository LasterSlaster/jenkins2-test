def call (pomDir = './') {

    currentBuild.displayName = '#' + getPOMVersion(pomDir) + '(' + env.BUILD_NUMBER + ')'
    echo 'INFO: Setting build name to: ' + currentBuild.displayName
}