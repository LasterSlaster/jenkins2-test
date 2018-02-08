package jobs

import javaposse.jobdsl.dsl.Job
import jobs.*

public class ConfigPipelineJob {

    Job createJob (def factory) {
        factory. with{
            logRotator {
                numToKeep(15)
            }
            triggers {
                cron('H/5 * * * *')
            }
        }
    }
}

public class ConfigJob {

    Job createJob (def factory) {
        factory. with{
            logRotator {
                numToKeep(15)
            }
            triggers {
                cron('H/5 * * * *')
            }
            wrappers {
                timestamps()
                preBuildCleanup()
            }
        }
    }
}

public class BaseTriggerJob {

    String name
    String githubOwnerRepo
    String credentialID
    String triggerPipeline
    String stage

    Job createJob(def dslFactory) {
        def triggerJob = dslFactory.job(this.name) {
            description('Job to watch the repository ' + this.githubOwnerRepo + ' and trigger the pipeline ' + this.triggerPipeline + ' at stage ' + this.stage)
            scm {
                git {
                    remote {
                        github(this.githubOwnerRepo) //TODO: Add GitHub URL
                        credentials(this.credentialID)
                    }
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            publishers {
                downstreamParameterized {
                    trigger(triggerPipeline) {
                        parameters {
                            predefinedProp('stage', stage)
                        }
                    }
                }
            }
        }
    }
}

public class BasePipelineJob {

    String name
    String description
    String githubOwnerRepo
    String credentialID
    String scriptPath

    Job createJob(def dslFactory) {
        dslFactory.pipelineJob(this.name) {
            description(this.description)

            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                github(this.githubOwnerRepo) //TODO: Add GitHub URL
                                credentials(this.credentialID) //TODO: Add Creadential ID
                            }
                            extensions {
                                cleanBeforeCheckout()
                            }
                        }
                    }
                    scriptPath(this.scriptPath)
                }
            }
        }
    }
}

public class ComponentsPipelineTriggerJob {

    static void createJob(def dslFactory) {
        def baseTriggerJob = new BaseTriggerJob(
            name: 'ComponentsPipelineTriggerJob',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            triggerPipeline: 'Architecture Pipeline',
            stage: '2'
            ).createJob(dslFactory)
        def configuredTriggerJob = new ConfigJob().createJob(baseTriggerJob)
    }
}

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

public class ComponentsSnapshotJob {

    static void createJob(def dslFactory) {
         def pipelineJob = new BasePipelineJob(
            name: '03. HPS APF Components Snapshot',
            description: 'Pipeline Job for the components snapshot build',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            scriptPath: 'pipelineBuilds/ComponentsSnapshot.groovy'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}

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

public class ReferenceAppSnapshotBuildDeployJob {

    static void createJob(def dslFactory) {
           def pipelineJob = new BasePipelineJob(
            name: '6. HPS APF ReferenceApp Snapshot Build Deploy',
            description: 'Pipeline Job for the ReferenceApp snapshot build',
            githubOwnerRepo: 'LasterSlaster/jenkins2-test',
            credentialID: 'ID',
            scriptPath: 'pipelineBuilds/ReferenceAppSnapshotBuildDeploy.groovy'
            ).createJob(dslFactory)
            
        def configuredPipelineJob = new ConfigPipelineJob().createJob(pipelineJob)
    }
}

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
    }
}

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
            external('jobs/SeedJob.groovy')
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