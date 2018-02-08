package main.jobs

import javaposse.jobdsl.dsl.Job

public class ArchitecturePipelineJob {

    static void createJob(def dslFactory) {
        def pipelineJob = dslFactory.pipelineJob('Architecture Pipeline')
        def configuredPipelineJob = new ConfigJob().build(pipelineJob)

        configuredPipelineJob.with {
            definition {
                cpsScm {
                    scm {
                        git {
                            github("") //TODO: Add GitHub URL
                            credentials('') //TODO: Add Creadential ID
                            extensions {
                                cleanBeforeCheckout()
                            }
                        }
                    }
                }
            }
        }
    }
}
