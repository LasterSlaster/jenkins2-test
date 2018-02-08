import javaposse.jobdsl.dsl.Job

public class ArchitectureSnapshotJob {

    static void createJob(def dslFactory) {
        def pipelineJob = new BasePipelineJob(
            name: '02. H&PS APF Architecture Snapshot'
            description: 'Pipeline Job for the architecture snapshot build'
            githubOwnerRepo: 'LasterSlaster/jenkins2-test'
            credentialID: 'ID'
            scriptPath: 'pipelineBuilds/ArchitectureSnapshot'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}