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
            }
        }
    }
}

post {
    always {
        archiveArtifacts artifacts: '**/*.war',
        allowEmptyArchive: true
    }
    success {
        echo 'Build and deployment is successful!'
    }
    failure {
        echo 'Damn!'
    }
}