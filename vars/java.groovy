def call () {
  
  pipeline {
  
  agent any
    
    options { 
            disableConcurrentBuilds() 
        }
    environment {
    
    }
    stages {
    
      stage ('clean workspace') {
        step {
          cleanWS()
        
        }
      }
      
      stage ('checkout') {
      
        step {
         
        }
      }
    
    
    }
  }
}
