package main.jobs

//TODO: Create Configurable Trigger Job
public class ComponentsPipelineTriggerJob {

    static void createJob(def dslFactory) {
        dslFactory.job('ComponentsPipelineTriggerJob') {
            scm {
                git {
                    name('Components')
                    url('http://172.31.22.80/gitweb/hpsapf-components.git')
                }
            }
            triggers {
                cron('H/5 * * * *')
            }
            publishers {
                downstreamParameterized {
                    trigger('Architecture Pipeline') {
                        parameters {
                            predefinedProp('stage', '2')
                        }
                    }
                }
            }
        }
    }
}