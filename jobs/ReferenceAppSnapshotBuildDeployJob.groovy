package main.jobs

import javaposse.jobdsl.dsl.Job

public class ReferenceAppSnapshotBuildDeployJob {

    static void createJob(def dslFactory) {
           def pipelineJob = new BasePipelineJob(
            name: '6. H&PS APF ReferenceApp Snapshot Build Deploy'
            description: 'Pipeline Job for the ReferenceApp snapshot build'
            githubOwnerRepo: 'LasterSlaster/jenkins2-test'
            credentialID: 'ID'
            scriptPath: 'pipelineBuilds/ReferenceAppSnapshotBuildDeploy'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}