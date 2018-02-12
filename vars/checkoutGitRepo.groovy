//TODO: Check parameter defalts
def call(repoURL = 'http://172.31.22.80/gitweb/', credentialsID = '', branch = 'refs/heads/master', browser = 'GitWeb', browserURL = '', browserVersion = '') {

	checkout([$class: 'GitSCM', branches: [[name: branch]], browser: [$class: browser, repoUrl: browserURL version: browserVersion], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: credentialsId, url: repoURL]]])
}