def call(Map stageParams) {
 
    checkout([
        $class: 'GITSCM',
        branches: [[name: stageParams.branch ]],
        userRemoteConfigs: [[url: stageParams.url]]
     ])
  }

/*def call(Map stageParams) {
 
    checkout([
        $class: 'GitSCM',
        branches: [[name:  stageParams.branch ]],
        userRemoteConfigs: [[ url: stageParams.url ]]
    ])
  }*/
