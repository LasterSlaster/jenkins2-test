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
            scriptPath: 'pipelines/ArchitecturePipeline.groovy'
            ).createJob(dslFactory)
        
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
        
        configuredPipelineJob.with {
            parameters {
                stringParam('startStage', '0', 'Set the stage to start the pipeline with. Possible values: \n 0 : JobDSL Plugin Snapshot\n 1 : Architecture Snapshot\n 2 : Components Snapshot\n 3 : ReferenceApp build deploy')
            }
        }
    }
}
