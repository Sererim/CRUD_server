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
                script {
                    def warFile = 'target/Server-0.9.8.war'
                    def tomcatDir = "/opt/tomcat/latest/webapps"
                    sh "sudo cp ${warFile} ${tomcatDir}"
                }
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