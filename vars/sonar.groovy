/*def call(credentialsId){
  
  withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
  mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url="http://172.17.0.1:9000"
  }

}*/

/*def call(credentialsId){

    withSonarQubeEnv(sonarqube) {
      sh 'mvn clean package sonar:sonar'
      //sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url="http://172.17.0.1:9000"'
    }
}*/


def call(credentialsId){
  withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
  sh ' mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url="http://192.168.1.4:9000"'
  }
}
