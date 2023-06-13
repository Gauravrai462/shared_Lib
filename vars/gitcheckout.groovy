def call(Map stageParams){
  checkout([
    $class : 'gitscm',
    branches : [[name: stageParams.branch ]],
    userRemoteConfig : [[url: stageParams.url]]
  ])
}
