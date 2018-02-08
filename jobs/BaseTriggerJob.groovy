package jobs

import javaposse.jobdsl.dsl.Job

public class BaseTriggerJob {

    String name
    String githubOwnerRepo
    String credentialID
    String triggerPipeline
    String stage

    Job createJob(def dslFactory) {
        def triggerJob = dslFactory.job(this.name) {
            description('Job to watch the repository ' + this.githubOwnerRepo + ' and trigger the pipeline ' + this.triggerPipeline  + ' at stage ' + this.stage)
            scm {
                git {
                    remote {
                        github(this.githubOwnerRepo) //TODO: Add GitHub URL
                        credentials(this.credentialID)
                    }
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            publishers {
                downstreamParameterized {
                    trigger(triggerPipeline) {
                        parameters {
                            predefinedProp('stage', stage)
                        }
                    }
                }
            }
        }
    }
}