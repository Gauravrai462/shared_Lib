def call(def PipelineParams) {
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
      PROJECT_NAME="${PipelineParams.PROJECT_NAME}"
      REGION="${PipelineParams.REGION}"  
      AWS_ACCESS_KEY_ID="credentials_${PipelineParams.AWS_ACCESS_KEY_ID}"
      AWS_SECRET_ACCESS_KEY="credentials_${PipelineParams.AWS_SECRET_ACCESS_KEY}"
  }
   stages{

     stage('clean'){
        steps{
         cleanWs()
        }
      }
    stage('Git Checkout'){
                 
            steps{
            gitcheckout(
                branch: "${BRANCH}",
                url: "${GIT_REPO}"
            )
          }
        }

   stage('maven-build'){
      steps{
        sh 'mvn clean install'
     }
     
   }

   stage('upload_to_S3'){
     steps{
       
        sh """
            export AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
            export AWS_SECRET_ACCESS_KEY==${AWS_SECRET_ACCESS_KEY}
            export AWS_DEFAULT_REGION=${REGION}
            aws s3 cp ${PROJECT_NAME} s3://${BUCKET_NAME} --region ${REGION}
          """
           
     }
     
   } 
    
     }
    
    }
  }
