def call () {

	if (currentBuild.result == 'FAILURE') {
		echo 'INFO: Sending email'
		//TODO: Update list of recipients, make it variable
		emailext body: 'Hello everyone,\nthere is a Jenkins build failing!', recipientProviders: [[$class: 'RequesterRecipientProvider'], [$class: 'FirstFailingBuildSuspectsRecipientProvider']], subject: 'ACN APF Jenkins: Build Failed'
	} else {
		emailext body: 'Hello everyone,\nthe build is STABLE', recipientProviders: [[$class: 'UpstreamComitterRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: 'ACN HPS APF Jenkins Build Status'
	}
}