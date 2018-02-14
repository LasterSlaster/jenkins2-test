def call (testResults = '**/target/surefire-reports/TEST-*.xml', artifacts = '**/target/\*.jar') {

    echo 'INFO: Archiving build artifacts'
    step([$class: 'ArtifactArchiver', artifacts: artifacts, fingerprint: true])
    step([$class: 'JUnitResultArchiver', testResults: testResults])
    if (currentBuild.result == 'UNSTABLE') {
        echo 'WARNING: Maven build test results contain failures'
        echo 'WARNING: Setting build result to UNSTABLE'
    }
}