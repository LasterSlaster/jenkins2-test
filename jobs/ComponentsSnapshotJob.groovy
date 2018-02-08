package jobs

import javaposse.jobdsl.dsl.Job
import jobs.ConfigPipelineJob

public class ComponentsSnapshotJob {

    static void createJob(def dslFactory) {
         def pipelineJob = new BasePipelineJob(
            name: '03. HPS APF Components Snapshot',
            description: 'Pipeline Job for the components snapshot build',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            scriptPath: 'pipelineBuilds/ComponentsSnapshot'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}