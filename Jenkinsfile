pipeline {
  agent {
    // Use an official Maven image with JDK 21 so `mvn` is available in the environment.
    docker {
      image 'maven:3.9.4-jdk-21'
      // Keep a local .m2 cache across runs (optional and requires Docker volume support in Jenkins)
      args '-v $HOME/.m2:/root/.m2'
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
        echo "Running mvn test inside container: $(mvn -v || true)"
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
