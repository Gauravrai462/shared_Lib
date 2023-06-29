def call(String project, String ImageTag, String hubUser, String dockerCredentialId){

    withCredentials([string(credentialsId: 'dockerCredentialId', Variable: 'jen-doc')]) {
     sh "docker login -u '$USER' -p '$PASS'"
     sh "docker image push ${hubUser}/${project}:${ImageTag}"
     sh "docker image push ${hubUser}/${project}:latest"  
 }
}
