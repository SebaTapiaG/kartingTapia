pipeline{
    agent any
    tools{
        maven "maven"

    }
      environment {
        // Configuración de la base de datos para pruebas
        DB_HOST = 'postgres' // o la IP de tu contenedor PostgreSQL si usas Docker
        DB_PORT = '5432'
        DB_NAME = 'karting'
        DB_USER = 'postgres' // usuario de tu base de datos
        DB_PASSWORD = 'clave' // contraseña de tu base de datos
    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/SebaTapiaG/kartingTapia']])
                dir("backend"){
                    bat "mvn clean install"
                }
            }
        }
        stage("Test"){
            steps{
                dir("backend"){
                    bat "mvn test"
                }
            }
        }        
        stage("Build and Push Docker Image"){
            steps{
                dir("backend"){
                    script{
                         withDockerRegistry(credentialsId: 'docker-credentials'){
                            bat "docker build -t sebatapiag/backend-image ."
                            bat "docker push sebatapiag/backend-image ."
                        }
                    }                    
                }
            }
        }
    }
}