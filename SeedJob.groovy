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
            external('SeedJob.groovy')
            removeAction('DELETE')
            additionalClasspath 'jobs/*'
        }
    }
}

jobs.ArchitecturePipelineJob.createJob(this)

jobs.DSLPluginSnapshotJob.createJob(this)
jobs.ArchitectureSnapshotJob.createJob(this)
jobs.ComponentsSnapshotJob.createJob(this)
jobs.ReferenceAppSnapshotBuildDeployJob.createJob(this)

jobs.ComponentsPipelineTriggerJob.createJob(this)