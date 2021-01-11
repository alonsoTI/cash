
   node {

      stage('Preparation') {
         
      }
      stage('Sonar') {
        sh 'mvn sonar:sonar \
        -Dsonar.projectKey=cash-demo \
        -Dsonar.host.url=http://192.168.1.76:9000 \
        -Dsonar.login=6f95a2da8caec2d3e9a0afab6e2298b19a8e1f6e'
      }
      stage("Deploy to" +deploymentEnvironment){
         
      }
	  stage('Post Execution') {
        
      }

   }
