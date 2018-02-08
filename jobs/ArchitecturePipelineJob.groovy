package jobs

import javaposse.jobdsl.dsl.Job
import jobs.ConfigPipelineJob

public class ArchitecturePipelineJob {

    static void createJob(def dslFactory) {
        def pipelineJob = new BasePipelineJob(
            name: 'Architecture Pipeline',
            description: 'Pipeline Job for the hps apf architecture pipeline',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            scriptPath: 'pipelines/ArchitecturePipeline'
            ).createJob(dslFactory)
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}
