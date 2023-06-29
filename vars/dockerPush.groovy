def call(String project, String ImageTag, String hubUser){

    withCredentials([string(credentialsId: 'jen-doc', variable: 'jen-doc', /*usernameVariable: 'DOCKER_USERNAME'*/)]) {
     sh "docker login -u raigaurav95 -p  Gaurav@12"
     sh "docker image push ${hubUser}/${project}:${ImageTag}"
     sh "docker image push ${hubUser}/${project}:latest"  
 }
}
