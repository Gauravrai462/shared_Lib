def call(String project, String ImageTag, String hubUser){

    withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'raigaurav95', userpasswordVariable: 'Gaurav@12')]) {
     sh "docker login -u raigaurav95 -p  Gaurav@12"
     sh "docker image push ${hubUser}/${project}:${ImageTag}"
     sh "docker image push ${hubUser}/${project}:latest"  
 }
}
