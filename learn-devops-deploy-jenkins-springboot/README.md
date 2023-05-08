# learn-jenkins-deploy-springboot
- learn deployment springboot app using jenkins
- Medium article [https://deni-setiawan.medium.com/deploy-springboot-app-using-jenkins-11715ae68ec2](https://deni-setiawan.medium.com/deploy-springboot-app-using-jenkins-11715ae68ec2)

Link :
- [Stage Job](#stage)
- [Jenkins Pipline](#jenkins-pipeline)

 

# Stage
![image](https://user-images.githubusercontent.com/11941308/235840875-3b2a7a7c-90c6-4f36-8448-6bd2ec0c3d88.png)


#### Clone Repo
- Jenkins will clone repository to github
- jenkins will save the repo into folder jenkins_home/workspace/[repository name]

#### Build Project
- jenkins will open folder jenkins_home/workspace/[repository name]
- jenkins will run syntax "mvn clean install" 
- and then .jar already created

#### Build Docker Image
- jenkins will open folder jenkins_home/workspace/[repository name]
- jenkins will run syntax "docker build ."
- and then docker image alreadey created

#### Deploy Docker Image
- jenkins will run syntax "docker run container .... image:image name"


# jenkins Pipeline 
```
node {

    def mvnHome = tool 'maven-3.5.2'
    def dockerImage
    def dockerImageTag = "app${env.BUILD_NUMBER}"

    stage('Clone Repo') {
      echo "Clone Repo from a GitHub repo"

    checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'denitiawan', url: 'https://github.com/denitiawan/learn-jenkins-deploy-springboot']])

      mvnHome = tool 'maven-3.5.2'
    }

    stage('Build Project') {
      echo "build project via maven"
      
      
      // jika container belum dihapus dan maven sudah pernah download dependecie maka boleh aktifka mode offline
      //sh "'${mvnHome}/bin/mvn' clean install -o"
      
      // jika container telah dihapus dan dibuat ulang maka wajib untuk aktifkan maven mode online
      sh "'${mvnHome}/bin/mvn' clean install "
    }

    stage('Build Docker Image') {

      echo "Build docker image"
      sh "ls -lrt"
      sh "docker image build -t deployed_springboot_by_jenkins:${env.BUILD_NUMBER} . "
      sh "docker images"

    }


    stage('Deploy Docker Image'){
      echo "Deploy docker image"
      
      def inspectExitCode = sh script: "docker container inspect deployed_springboot_by_jenkins", returnStatus: true
        if (inspectExitCode == 0) {
            // remove container if exist
            sh "docker stop deployed_springboot_by_jenkins"
            sh "docker rm deployed_springboot_by_jenkins"
        } 
        
         // create and run container
         sh "docker run  --detach  --name deployed_springboot_by_jenkins  --publish 1234:1234 --network=deployment_network  -e APP_HOST=deployed_springboot_by_jenkins   -e APP_PORT=1234 -e APP_DB_HOST=deployment_db_mysql -e APP_DB_PORT=3366  -e APP_DB_USER=user -e APP_DB_PASSWORD=password -e APP_DB_NAME=db_spring deployed_springboot_by_jenkins:${env.BUILD_NUMBER}"
      
      
    }


}


```
