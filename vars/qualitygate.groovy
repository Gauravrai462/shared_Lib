def call(credentialsId){
  withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]){ 
  waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
  }
}
