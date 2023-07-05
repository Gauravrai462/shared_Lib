def call (call pipelineParams) {
pipeline{

  agent any
  
  options{
     disableConcurrentBuilds() 
  }

  environment{

      GIT_REPO="${PipelineParams.GIT_REPO}"
      BRANCH="${PipelineParams.BRANCH}"
      IMAGE_VERSION= "v_${BUILD_NUMBER}"
      BUILD_NUMBER="${BUILD_NUMBER}"
      BUCKET_NAME="${PipelineParams.BUCKET_NAME}"
      SONAR_URL = "http://172.17.0.1:9000"
      SONAR_TOKEN= "SONAR_AUTH_TOKEN"
      
    
  }

  stages{

    stage('cleanworkspace'){
      steps{
      cleanWs()
      sh 'printEnv'
      }
    }

   stage('checkout'){
    step{
      gitchekout{
        branch:"${BRANCH}",
        url: "${GIT_REPO}"  
      }
    }
  } 

  stage('maven-build'){
     steps{
       sh 'mvn build'
     }
     
   }

   stage('upload_to_S3'){
     steps{
       withCredentials([file(credentialsId: 'aws_credentials', variable: 'aws')]) {
        sh """
            aws s3 cp 
        }      
     }
     
   } 

    
      
  


    
    
  }
    
}
  
}
