package jobs

import javaposse.jobdsl.dsl.Job

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