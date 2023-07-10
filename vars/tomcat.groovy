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
      //PATH="${PipelineParams.PATH}"
      FILE="${PipelineParams.FILE}"
       
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

    stage('Maven_Build'){
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
            aws s3 cp  target/${FILE} s3://${BUCKET_NAME} --region ${REGION}
            '''
           
     }
     
   }

   stage('uploade to ec2') {
     steps{
       withCredentials([sshUserPrivateKey(credentialsId: 'Tomcat', keyFileVariable: 'SSH_PRIVATE_KEY')]) {

      sh '''
       sudo -S ssh -i Downloads/vprofile.pem ubuntu@43.204.24.104 'systemctl stop tomcat9'
       sudo -S scp -i Downloads/vprofile.pem /target/${FILE} ubuntu@43.204.24.104 cp target/${FILE} /var/lib/tomcat9/webapps/ROOT.war
       sudo -S ssh -i Downloads/vprofile.pem ubuntu@43.204.24.104 'systemctl restart tomcat9'
       '''

       }
     }
     
   }

     }
    
    }
  }
