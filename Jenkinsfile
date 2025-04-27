pipeline {
    agent any

    tools {
        maven "maven"
    }

    environment {
        // Variables de base de datos (por si tu app las necesita en runtime)
        DB_HOST = 'postgres'
        DB_PORT = '5432'
        DB_NAME = 'karting'
        DB_USER = 'postgres'
        DB_PASSWORD = 'clave'
    }

    stages {
        stage("Checkout Code") {
            steps {
                checkout scmGit(
                    branches: [[name: 'main']],  // Puedes dejarlo así si es solo 'main'
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/SebaTapiaG/kartingTapia']]
                )
            }
        }

        stage("Build JAR File") {
            steps {
                dir("backend") { // Entra al backend
                    bat "mvn clean install" // Compila
                }
            }
        }

        stage("Test") {
            steps {
                dir("backend") {
                    bat "mvn test" // Corre tests
                }
            }
        }

        stage("Build and Push Docker Image") {
            steps {
                dir("backend") {
                    script {
                        withDockerRegistry(credentialsId: 'docker-credentials') {
                            bat "docker build -t sebatapiag/backend-image ." // Construye imagen
                            bat "docker push sebatapiag/backend-image" // Empuja imagen (sin el punto final)
                        }
                    }
                }
            }
        }
    }
}
