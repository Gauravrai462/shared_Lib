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
      PATH="${PipelineParams.PATH}"
      WAR="${PipelineParams.WAR}"
       
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
       
            sh '''
            aws_credentials=$(aws sts assume-role --role-arn arn:aws:iam::685793358766:role/Jenkins_AWS_role --role-session-name "AWSCLI-Session" --output json ) 
            export AWS_ACCESS_KEY_ID=\$(echo $aws_credentials|jq '.Credentials.AccessKeyId')
            export AWS_SECRET_ACCESS_KEY=\$(echo $aws_credentials|jq '.Credentials.SecretAccessKey')
            aws s3 cp target/${WAR} s3://${BUCKET_NAME} --region ${REGION}
            '''
           
     }
     
   }

   /*stage('uploade to ec2') {
     steps{

      sh 'aws s3 cp s3://${BUCKET_NAME}/vprofile-v2.war  ${PATH}  --region ${REGION}'

     }
     
   }*/

     }
    
    }
  }
