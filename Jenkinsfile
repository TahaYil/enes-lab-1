pipeline {
  agent any

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
        // Try to run mvn locally; if not present, fall back to running maven via docker run.
        script {
          sh '''
            set -e
            echo "Detecting build environment..."
            if command -v mvn >/dev/null 2>&1; then
              echo "Found local mvn:"; mvn -v || true
              mvn -B -DskipTests=false test
            elif command -v docker >/dev/null 2>&1; then
              echo "No local mvn found; using dockerized Maven image"
              # Use the workspace mount so container sees the repo. Use current user to avoid root-owned files in workspace.
              docker run --rm -u $(id -u):$(id -g) -v "$PWD":/workspace -w /workspace maven:3.9.4-jdk-21 mvn -B -DskipTests=false test
            else
              echo "ERROR: Neither 'mvn' nor 'docker' are available on this agent. Please install Maven or enable Docker on the node." >&2
              exit 127
            fi
          '''
        }
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
