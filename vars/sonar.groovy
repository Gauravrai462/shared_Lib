def call(credentialsId){

    withSonarQubeEnv(credentialsId: sonarqube) {
         sh 'mvn clean package sonar:sonar'
    }
}
