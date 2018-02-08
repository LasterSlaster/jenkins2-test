package main.jobs

import javaposse.jobdsl.dsl.Job

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