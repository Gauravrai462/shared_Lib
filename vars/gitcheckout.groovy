def call(Map stageParams){
  checkout([
    $class: 'GITSCM',
    branches: [[name: stageParams.branch ]],
    userRemoteConfig: [[url: stageParams.url]]
  ])
}
