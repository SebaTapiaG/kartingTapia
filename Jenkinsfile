pipeline{
    agent any
    tools{
        maven "maven"

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