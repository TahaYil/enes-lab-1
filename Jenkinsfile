pipeline {
  agent any

  // Poll SCM every 5 minutes (cron syntax). Adjust as needed.
  triggers {
    // Polling syntax: "H/5 * * * *" means every 5 minutes.
    pollSCM('H/5 * * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        // Checkout the repository configured in the Jenkins job
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        // Run Maven tests; fail the build on test failures
        sh 'mvn -B -DskipTests=false test'
      }
      post {
        always {
          // Publish JUnit test results so Jenkins can show them
          junit '**/target/surefire-reports/*.xml'
          // Archive common build artifacts if produced (allow empty)
          archiveArtifacts artifacts: 'target/*.jar, target/*.war', allowEmptyArchive: true
        }
      }
    }
  }

  post {
    success {
      echo 'Pipeline succeeded.'
    }
    failure {
      echo 'Pipeline failed.'
    }
  }
}

