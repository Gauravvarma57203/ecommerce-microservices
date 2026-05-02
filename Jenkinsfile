pipeline {
  agent any

  environment {
    IMAGE = 'gauravvarma57202/auth-service:latest'
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build') {
      steps {
        dir('auth-service') {
          sh 'chmod +x mvnw || true'
          sh './mvnw clean package'
        }
      }
    }

    stage('Test') {
      steps {
        dir('auth-service') {
          sh './mvnw test'
        }
      }
    }

    stage('Docker Build') {
      steps {
        dir('auth-service') {
          sh 'docker build -t $IMAGE .'
        }
      }
    }

    stage('Docker Push') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'dockerhub-creds',
          usernameVariable: 'USER',
          passwordVariable: 'PASS'
        )]) {
          sh 'echo $PASS | docker login -u $USER --password-stdin'
          sh 'docker push $IMAGE'
        }
      }
    }

    stage('Deploy') {
      steps {
        sh '''
          docker pull $IMAGE
          docker-compose down || true
          docker-compose up -d
        '''
      }
    }
  }
}