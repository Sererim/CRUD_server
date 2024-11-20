pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('deploy') {
            steps {
                echo 'WIP'
// Add deployment steps here, e.g., deploying to Tomcat
// deploy adapters: [tomcat9(credentialsId: 'tomcat-credentials', path: '', url: 'http://your-tomcat-server:8080')], contextPath: '/', war: '**/*.war'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/*.war', allowEmptyArchive: true
        }
        success {
            echo 'Build and deployment is successful!'
        }
        failure {
            echo 'Damn!'
        }
    }
}