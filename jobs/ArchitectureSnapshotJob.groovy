package jobs

import javaposse.jobdsl.dsl.Job
import jobs.ConfigPipelineJob

public class ArchitectureSnapshotJob {

    static void createJob(def dslFactory) {
        def pipelineJob = new BasePipelineJob(
            name: '02. HPS APF Architecture Snapshot',
            description: 'Pipeline Job for the architecture snapshot build',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            scriptPath: 'pipelineBuilds/ArchitectureSnapshot.groovy'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}