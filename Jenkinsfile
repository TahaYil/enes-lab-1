pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build & Test') {
      steps {
        // make wrapper executable (idempotent)
        sh 'chmod +x mvnw || true'
        // use wrapper to run tests
        sh './mvnw -B -DskipTests=false test'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }
  }
}
