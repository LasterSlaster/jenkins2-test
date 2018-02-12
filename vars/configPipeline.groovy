package vars

def call (body) {
	def config = [:]
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = config
	body()

	def buildScript = config.buildScript ?: {}
	def testScript = config.testScript ?: {}
	def deployScript = config.deployScript ?: {}

	enum Steps {
	    BUILD(0, 'Build'),
	    TEST(1, 'Test'),
	    DEPLOY(2, 'Deploy'),

	    Steps(int id, String name) {
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

	    public static Steps getByName(String name) {
	        echo 'INFO: getting by name ' + name
	        for(Steps step : Steps.values()) {
	            if(step.name.equalsIgnoreCase(name)) {
	                return step
	            }
	        }
	        throw new IllegalArgumentException()
	    }
	}

	def prepareStages(def startPoint){
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
		timestamps {
			try {
				cleanWs()
				currentBuild.result = 'SUCCESS'
				
			    stage('Build') {
		        	if (stages.contains(Steps.BUILD)) {
			            echo 'INFO: Execute \\\'Build\\\''
			            buildScript()
			        }
			    }

			    stage('Tests') {
			        if (stages.contains(Steps.TEST)) {
			        	echo 'INFO: Execute stage \\\'Tests\\\''
			           	testScript()
			        }
			    }

			    stage('Deploy') {
			        if (stages.contains(Steps.DEPLOY)) {
			            echo 'INFO: Execute stage \\\'Deploy\\\''
			            deployScript()
			        }
			    }
			} catch (e) {
				error('ERROR: This job ended unexpectedly!\nStack trace:\n' + e)
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
}