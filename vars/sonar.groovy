def call('credentialsId') {
      environment {
        SONAR_URL = "http://172.17.0.1:9000"
      }
        withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
          sh 'mvn clean package sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
        }
      }
    }

/*def call(credentialsId){

    withSonarQubeEnv(sonarqube) {
      sh 'mvn clean package sonar:sonar'
      //sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url="http://172.17.0.1:9000"'
    }
}*/


/*def call(credentialsId){

    withSonarQubeEnv(credentialsId: Sonarqube) {
         sh 'mvn clean package sonar:sonar'
    }
}*/
