package jobs

import javaposse.jobdsl.dsl.Job
import jobs.ConfigPipelineJob

public class DSLPluginSnapshotJob {

    static void createJob(def dslFactory) {
         def pipelineJob = new BasePipelineJob(
            name: '01. HPS APF DSL Plugins Snapshot',
            description: 'Pipeline Job for the DSL plugin snapshot build',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            scriptPath: 'pipelineBuilds/DSLPluginSnapshot.groovy'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}