package jobs

import javaposse.jobdsl.dsl.Job

public class ArchitecturePipelineJob {

    String name
    String description
    String gitubOwnerRepo
    String credentialID
    String scriptPath

    Job createJob(def dslFactory) {
        dslFactory.pipelineJob(this.name) {
            description(this.description)

            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                github(this.githubOwnerRepo) //TODO: Add GitHub URL
                                credentials(this.credentialID) //TODO: Add Creadential ID
                            }
                            extensions {
                                cleanBeforeCheckout()
                            }
                        }
                    }
                    scriptPath(this.scriptPath)
                }
            }
        }
    }
}
