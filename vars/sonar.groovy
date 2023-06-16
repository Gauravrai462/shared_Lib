def call(credentialsId){
  withSonarQubeEnv([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
  sh ' mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url="http://172.17.0.1:9000"'
  }

}
