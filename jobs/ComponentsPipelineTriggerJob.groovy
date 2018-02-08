package jobs

import javaposse.jobdsl.dsl.Job
import jobs.ConfigJob

public class ComponentsPipelineTriggerJob {

    static void createJob(def dslFactory) {
        def baseTriggerJob = new BaseTriggerJob(
            name: 'ComponentsPipelineTriggerJob'
            githubOwnerRepo: 'LasterSlaster/jenkins2-test'
            credentialID: 'ID'
            triggerPipeline: 'Architecture Pipeline'
            stage: 2
            ).createJob(dslFactory)
        def configuredTriggerJob = new ConfigJob().createJob(baseTriggerJob)
    }
}