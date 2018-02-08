package main.jobs
//TODO: Create Configurable pipelineJob for Jobs
import javaposse.jobdsl.dsl.Job

public class ArchitectureSnapshotJob {

    static void createJob(def dslFactory) {
        def pipelineJob = dslFactory.pipelineJob('02. H&PS APF Architecture Snapshot')
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