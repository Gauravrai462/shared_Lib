def call (def PipelineParams){

  pipeline{

    agent any

  options { 
            disableConcurrentBuilds() 
    }
   stages{

      stage('clean'){
        steps{
         cleanWs()
        }
      }

     
   stage('Get Jobs in Project') {
            steps {
                script {
                    def project = Jenkins.instance.getItemByFullName(projectName)
                    
                    if (project instanceof hudson.model.Job) {
                        def jobList = project.items.findAll { it instanceof hudson.model.Job }
                        
                        jobList.each { job ->
                            echo "Job Name: ${job.fullName}, Type: ${job.class.simpleName}"
                        }
                    } else {
                        error "Project not found or not a Job"
                    }
                }
            }
        }

 

   


   }    
  }
}
