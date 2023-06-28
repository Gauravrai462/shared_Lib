def call(String project, String hubUser, String ImageTag) {
  sh '''
  docker image build --tag ${hubUser}/${project} .
  docker image tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}
   docker image tag ${hubUser}/${project} ${hubUser}/${project}:latest
  '''  
}
