def call (def pipelineParams){

  pipeline{

    agent any

    environment {
      GIT_REPO="${pipelineParams.GIT_REPO}"
      BRANCH="${pipelineParams.BRANCH}"
      //AWS_REGION="${pipeline.Params.AWS_REGION}"
      //AWS_ACCOUNT="${pipelineParams.AWS_ACCOUNT}"
      //ECR_REPO="${pipelineParams.ECR_REPO}"
      //ACCESS_KEY = credentials('AWS_ACCESS_KEY_ID')
      //SECRET_KEY = credentials('AWS_SECRET_ACCESS_KEY')
      
        
    }

  options { 
            disableConcurrentBuilds() 
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

    stage('Unit Test maven'){

            steps{
               script{
                   
                   sh 'mvn test'
               }
            }
        }  

             
  stage('mvn initigration test'){

           steps{

             sh 'mvn verify -DskipUnitTests'
           }
      
     }

   stage('Maven_Build'){
     steps{
       sh 'mvn clean install'
     }
   }    

  stage('Docker Build'){

     steps{
        sh """
          export AWS_ACCESS_KEY_ID=AKIAZ7LDYGOXELHOTIUR
          export AWS_SECRET_ACCESS_KEY=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
          export AWS_DEFAULT_REGION=us-east-1
          aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 685793358766.dkr.ecr.us-east-1.amazonaws.com
          docker build -t java .
          docker tag java:latest 685793358766.dkr.ecr.us-east-1.amazonaws.com/java:latest
          """
            
        
      } 
    }  

    /*stage('Docker Scan'){

     steps{
        script{
            sh """   
              trivy image "${DOCKER_IMAGE}" > scan.txt
              cat scan.txt
            """
        }
      } 
    }*/  

   stage('Push Docker Image') {
            steps {
              sh """
                aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 685793358766.dkr.ecr.us-east-1.amazonaws.com
            
                docker push 685793358766.dkr.ecr.us-east-1.amazonaws.com/java:latest 
                """
                }
            }
        

  stage('Docker clean'){

     steps{
        script{
            sh """
             docker rmi ${env.DOCKER_REGISTRY}/${env.DOCKER_TAG}:${IMAGE_VERSION}
             
          """
        }
      } 
    }    



   }    
  }
}
