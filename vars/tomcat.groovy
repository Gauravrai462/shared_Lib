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
      //AWS_ACCESS_KEY_ID= credentials('')
      //AWS_SECRET_ACCESS_KEY= credentials('')
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
            aws_credentials=$(aws sts assume-role --role-arn arn:aws:iam::685793358766:role/Jenkins_AWS_role --role-session-name "AWSCLI-Session")
            export AWS_ACCESS_KEY_ID=\$(echo $aws_credentials|jq '.Credentials.AccessKeyId'|tr -d '"')
            export AWS_SECRET_ACCESS_KEY=\$(echo $aws_credentials|jq '.Credentials.SecretAccessKey'|tr -d '"')
            export AWS_SESSION_TOKEN=\$(echo $aws_credentials|jq '.Credentials.SessionToken'|tr -d '"')

            aws s3 cp ${PROJECT_NAME} s3://${BUCKET_NAME} --region ${REGION}
          """
           
     }
     
   } 
    
     }
    
    }
  }