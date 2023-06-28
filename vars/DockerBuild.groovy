def call(string project, string hubUser, string ImageTag) {
  sh '''
  docker build -t ${hubUser}/${project} .
  docker image tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}
   docker image tag ${hubUser}/${project} ${hubUser}/${project}:latest
  '''  
}
