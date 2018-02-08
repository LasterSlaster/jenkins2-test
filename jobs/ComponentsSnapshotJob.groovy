package main.jobs

import javaposse.jobdsl.dsl.Job

public class ComponentsSnapshotJob {

    static void createJob(def dslFactory) {
        def pipelineJob = dslFactory.pipelineJob('03. H&PS APF Components Snapshot')
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