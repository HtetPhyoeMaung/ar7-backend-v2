pipeline {
    agent any

    environment {
        APP_NAME = 'AR7 API Server'
        PROD_IP = '987.654.32.10'
        SSH_CRED_ID = 'ar7-server-ssh-key'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timestamps()
    }

    stages {
        stage('Deploy to Production') {
            steps {
                input "Deploy ${env.APP_NAME} to production?"

                sshagent([env.SSH_CRED_ID]) {
                    script {
                        echo "Connecting to Production Server: ${env.PROD_IP}"
                        
                        sh """
                            ssh -o StrictHostKeyChecking=no root@${env.PROD_IP} \
                            "cd ar7-app && git pull && docker compose up -d --build"
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            echo 'Pipeline finished.'
        }
        success {
            echo 'Deployment to Production Successful!'
        }
        failure {
            echo 'Deployment Failed. Please check if the server is reachable or Docker has errors.'
        }
    }
}