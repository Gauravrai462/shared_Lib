def call(def PipelineParams) {
  pipeline{

    agent any

    environment{

      GIT_REPO="${PipelineParams.GIT_REPO}"
      BRANCH="${PipelineParams.BRANCH}"
      DOCKER_IMAGE = "raigaurav95/test:${BUILD_NUMBER}"
      DOCKER_TOKEN= "jen-doc"
      DOCKER_USERNAME= "raigaurav95"
      DOCKER_REGISTRY= "https://hub.docker.com"
      DOCKER_TAG= "raigaurav95/test:"
      IMAGE_VERSION= "${OLD_BUILD_NUMBER}"
      SONAR_URL = "http://172.17.0.1:9000"
      SONAR_TOKEN= "SONAR_AUTH_TOKEN"
        
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

   stage('Static Code Analysis') {
      steps {
        withSonarQubeEnv(installationName: 'Sonar', credentialsId: 'sonar') {
         sh 'mvn clean package sonar:sonar'
        //withSonarQubeEnv(credentialsId: 'sonar') {
         //sh 'mvn clean package sonar:sonar'
        //withCredentials([string(credentialsId: 'sonar', variable: 'SONAR_AUTH_TOKEN')]) 
        //sh 'mvn clean package sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
        }
      }
    }
   

    stage('quality Gate'){

     steps{
      withCredentials([string(credentialsId: 'sonarqube', variable: "${SONAR_TOKEN}")]){ 
      waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
     }
    }
    }   

   stage('Docker Build'){

     steps{
        script{
            sh """
             docker build -t $DOCKER_IMAGE .
             //docker image build -t ${hubUser}/${project} . 
             //docker image tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}
             //docker image tag ${hubUser}/${project} ${hubUser}/${project}:latest
          """
        }
      } 
    }  

    stage('Docker Scan'){

     steps{
        script{
            sh """   
              trivy image "${DOCKER_IMAGE}" > scan.txt
              cat scan.txt
            """
        }
      } 
    }   

   stage('Docker Push'){

     steps{
        script{
            withCredentials([string(credentialsId: 'jen-doc', variable: 'jen-doc', /*usernameVariable: 'DOCKER_USERNAME'*/)]) {
            sh """
              docker login -u ${DOCKER_USERNAME} -p ${DOCKER_TOKEN},
              docker push ${DOCKER_IMAGE}
            """
        }
      } 
    }   
   }  

  stage('Docker clean'){

     steps{
        script{
            sh """
             docker rmi ${DOCKER_IMAGE}:${IMAGE_VERSION}
             //docker rmi ${hubUser}/${project}:latest
          """
        }
      } 
    }    


      
     }  
    
    }
 
  }
