package pipelines
@Library('ACN_HPS_APF_Library')_

import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException

enum Steps {
    DLS_BUILD(0, 'DSL Plugin Build'),
    ARCH_BUILD(1, 'Architecture Build'),
    COMPONENTS_BUILD(2, 'Components Build'),
    REFAPP_BUILD(3, 'ReferenceApp Build')

    Steps(id, name) {
        this.id = id
        this.name = name
    }

    private final int id
    private final String name

    int getId() {
        id
    }

    String getName() {
        name
    }

    public static Steps getByName(name) {
      //  echo 'getting by name ' + name.toString()
        for(Steps step : Steps.values()) {
            if(step.name.equalsIgnoreCase(name)) {
                return step
            }
        }
        throw new IllegalArgumentException()
    }
}

def prepareStages(startPoint){
    echo 'INFO: Preparing build steps starting from ' + startPoint
    Set steps = new LinkedHashSet()
    steps.add(Steps.DLS_BUILD)
    steps.add(Steps.ARCH_BUILD)
    steps.add(Steps.COMPONENTS_BUILD)
    steps.add(Steps.REFAPP_BUILD)
    List finalSteps = new ArrayList()
    steps.each{
        step ->
            if (step.id >= startPoint.id) {
                echo ('DEBUG: Adding stage ' + step)
                finalSteps.add(step)
            }
    }
    return finalSteps
}

def stages = prepareStages(Steps.getByName(startStage))

node {
    def dslResult
    def archResult
    def compResult
    def refAppResult

    timestamps {
        try {
            cleanWs()
            currentBuild.result = 'SUCCESS'

            stage('DSL Plugin Build') {
                if (stages.contains(Steps.DLS_BUILD)) {
                    echo 'INFO: Triggering job 01. HPS APF DSL Plugins Snapshot'
                    dslResult = build job: '01. HPS APF DSL Plugins Snapshot', propagate: false
                    if (dslResult.result == 'UNSTABLE') {
                        error('ERROR: Stage DSL Plugin Build is UNSTABLE')
                    }
                    if (dslResult.result == 'FAILURE') {
                        error('ERROR: Stage DSL Plugin Build is FAILURE')
                    }
                }
            }
            
            stage('Architecture Build') {
                if (stages.contains(Steps.ARCH_BUILD)) {
                    echo 'INFO: Triggering job 02. HPS APF Architecture Snapshot'
                        archResult = build job: '02. HPS APF Architecture Snapshot', propagate: false
                    if (archResult.result == 'UNSTABLE') {
                        echo 'WARNING: Stage Architecture Build is UNSTABLE'
                        currentBuild.result = 'UNSTABLE'
                    }
                    if (archResult.result == 'FAILURE') {
                        error('ERROR: Stage Architecture Build is FAILURE')
                    }
                }
            }

            stage('Components Build') {
                if (stages.contains(Steps.COMPONENTS_BUILD)) {
                    echo 'INFO: Triggering job 03. HPS APF Components Snapshot'
                    compResult = build job: '03. HPS APF Components Snapshot', propagate: false
                    if (compResult.result == 'UNSTABLE') {
                        echo 'WARNING: Stage Components Build is UNSTABLE'
                        currentBuild.result = 'UNSTABLE'
                    }
                    if (compResult.result == 'FAILURE') {
                        error('ERROR: Stage Components Build is FAILURE')
                    }
                }
            }

            stage('ReferenceApp Build') {
                if (stages.contains(Steps.REFAPP_BUILD)) {
                    echo 'INFO: Triggering job 6. HPS APF ReferenceApp Snapshot Build Deploy'
                    refAppResult = build job: '6. HPS APF ReferenceApp Snapshot Build Deploy', propagate: false
                    if (refAppResult.result == 'UNSTABLE') {
                        echo 'WARNING: Stage ReferenceApp Build is UNSTABLE'
                        currentBuild.result = 'UNSTABLE'
                    }
                    if (refAppResult.result == 'FAILURE') {
                        error('ERROR: Stage ReferenceApp Build is FAILURE')
                    }
                }
            }
        } catch (FlowInterruptedException interruptEx) {
            echo 'WARNING: This job was INTERRUPTED'
            currentBuild.result = 'ABORTED'
            throw interruptEx
        } catch (e) {
            echo 'ERROR: This job ended unexpectedly!\nStack trace:\n' + e
            currentBuild.result = 'FAILURE'
            throw e
        } finally {
            echo 'INFO: Sending email'
            emailBuildStatus()
            
            echo '\n'
            echo '************************************************'
            echo 'Pipeline ends with status: ' + currentBuild.result
            echo '************************************************'
        }
    }
}