def call(String project, String ImageTag, String hubUser, String dockerCredentialId){

    withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'USER', userpasswordVariable: 'PASS')]) {
     sh "docker login -u '$USER' -p '$PASS'"
     sh "docker image push ${hubUser}/${project}:${ImageTag}"
     sh "docker image push ${hubUser}/${project}:latest"  
 }
}
