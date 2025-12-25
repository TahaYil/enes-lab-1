pipeline {
  agent {
    // Use an official Maven image with JDK 21 so `mvn` is available in the environment.
    docker {
      image 'maven:3.9.4-jdk-21'
      // Optionally add docker args here if your Jenkins environment supports a persistent m2 cache.
      // args '-v /var/jenkins_home/.m2:/root/.m2'
    }
  }

  // Poll SCM every 5 minutes (adjust if you prefer webhooks)
  triggers {
    pollSCM('H/5 * * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        echo "Checking out source..."
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        // Print Maven version from inside the container (use shell so Groovy doesn't interpolate $)
        sh 'mvn -v || true'
        echo 'Running mvn test inside container'
        // Run Maven in batch mode; fail the step if tests or build fail
        sh 'mvn -B -DskipTests=false test'
      }
      post {
        always {
          // Publish JUnit test results; allowEmptyResults avoids failing the pipeline when no reports exist
          junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
          // Archive artifacts if any
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
    cleanup {
      echo 'Pipeline finished.'
    }
  }
}
