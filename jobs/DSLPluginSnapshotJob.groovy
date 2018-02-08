package main.jobs

import javaposse.jobdsl.dsl.Job

public class DSLPluginSnapshotJob {

    static void createJob(def dslFactory) {
         def pipelineJob = new BasePipelineJob(
            name: '01. H&PS APF DSL Plugins Snapshot'
            description: 'Pipeline Job for the DSL plugin snapshot build'
            githubOwnerRepo: 'LasterSlaster/jenkins2-test'
            credentialID: 'ID'
            scriptPath: 'pipelineBuilds/DSLPluginSnapshot'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}