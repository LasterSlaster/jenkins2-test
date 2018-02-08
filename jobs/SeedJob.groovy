package main.jobs

import javaposse.jobdsl.dsl.Job
import main.ArchitectureSnapshot

job('SeedJob') {
    concurrentBuild(false)
    scm {
        git {
            remote {
                //TODO: Add parameters
                github('')
                credentials('')
            }
        }
    }
    steps {
        dsl {
            external('SeedJob.groovy')
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