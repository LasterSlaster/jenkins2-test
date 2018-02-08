package main.jobs

import javaposse.jobdsl.dsl.Job

public class DSLPluginSnapshotJob {

    static void createJob(def dslFactory) {
        def pipelineJob = dslFactory.pipelineJob('01. H&PS APF DSL Plugins Snapshot')
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