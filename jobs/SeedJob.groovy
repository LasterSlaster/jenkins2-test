package jobs

import javaposse.jobdsl.dsl.Job
import jobs.*

job('SeedJob') {
    concurrentBuild(false)
    scm {
        git {
            remote {
                github('LasterSlaster/jenkins2-test')
                credentials('ID')
            }
        }
    }
    steps {
        dsl {
            external('jobs/SeedJobAll.groovy')
            removeAction('DELETE')
        }
    }
}


ArchitecturePipelineJob.createJob(this)

DSLPluginSnapshotJob.createJob(this)
ArchitectureSnapshotJob.createJob(this)
ComponentsSnapshotJob.createJob(this)
ReferenceAppSnapshotBuildDeployJob.createJob(this)

ComponentsPipelineTriggerJob.createJob(this)

//Empty jobs which get triggered
pipelineJob('hpsapf-referenceapplication-deployment-staticcontent')
pipelineJob('hpsapf-referenceapplication-deployment')
pipelineJob('hpsapf referenceapp batches linux deploy')
pipelineJob('hpsapf referenceapp batches windows deploy')