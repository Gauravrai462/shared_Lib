def call (def PipelineParams){

  pipeline{

    agent any

    environment {
      GIT_REPO="${PipelineParams.GIT_REPO}"
      BRANCH="${PipelineParams.BRANCH}"
      AWS_REGION="${PipelineParams.AWS_REGION}"
      DOCKER_REGISTRY="${PipelineParams.DOCKER_REGISTRY}"
      DOCKER_TAG="${PipelineParams.DOCKER_TAG}"
      BUILD_NUMBER="${BUILD_NUMBER}"
      IMAGE_VERSION= "v_${BUILD_NUMBER}"
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
          export AWS_ACCESS_KEY_ID=${aws_credentails}
          export AWS_SECRET_ACCESS_KEY=${aws_credentails}
          export AWS_DEFAULT_REGION=${AWS_REGION}
          aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
          docker build -t ${DOCKER_REGISTRY}/${DOCKER_TAG}:${IMAGE_VERSION} .
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
                export AWS_ACCESS_KEY_ID=AKIAZ7LDYGOXELHOTIUR
                export AWS_SECRET_ACCESS_KEY=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
                export AWS_DEFAULT_REGION=${AWS_REGION}
                aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
            
                docker push ${DOCKER_REGISTRY}/${DOCKER_TAG}:${IMAGE_VERSION}
                """
                }
            }
        

  stage('Docker clean'){

     steps{
        script{
            sh """
             docker rmi ${DOCKER_REGISTRY}/${DOCKER_TAG}:${IMAGE_VERSION} 
             
          """
        }
      } 
    }    



   }    
  }
}
