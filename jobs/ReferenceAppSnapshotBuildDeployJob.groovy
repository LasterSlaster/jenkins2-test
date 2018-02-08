package main.jobs

import javaposse.jobdsl.dsl.Job

public class ReferenceAppSnapshotBuildDeployJob {

    static void createJob(def dslFactory) {
        def pipelineJob = dslFactory.pipelineJob('6. H&PS APF ReferenceApp Snapshot Build Deploy')
        def configuredPipelineJob = new ConfigJob().createJob(pipelineJob)

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