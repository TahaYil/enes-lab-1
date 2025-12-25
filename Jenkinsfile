pipeline {
  agent any

  // Optional: poll SCM (adjust or remove if you prefer webhooks)
  triggers {
    pollSCM('H/5 * * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        echo 'Checking out source...'
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        script {
          // Use a shell block to try local mvn first, then fall back to docker run.
          sh '''
            set -e
            echo "Detecting build environment..."
            if command -v mvn >/dev/null 2>&1; then
              echo "Using local mvn:"; mvn -v || true
              mvn -B -DskipTests=false test
            elif command -v docker >/dev/null 2>&1; then
              echo "No local mvn found; using dockerized Maven image"
              # Run container as current user to avoid root-owned files in workspace
              docker run --rm -u $(id -u):$(id -g) -v "$PWD":/workspace -w /workspace maven:3.9.6-eclipse-temurin-17 mvn -B -DskipTests=false test
            else
              echo "ERROR: Neither 'mvn' nor 'docker' are available on this agent. Please install Maven or enable Docker on the node." >&2
              exit 127
            fi
          '''
        }
      }
    }
  }

  post {
    always {
      // Publish JUnit test results; allowEmptyResults avoids failing the post step when no reports exist
      junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
    }
    success {
      echo 'Pipeline succeeded.'
    }
    failure {
      echo 'Pipeline failed.'
    }
  }
}
