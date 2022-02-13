pipeline {

  agent none
  options { 
    timestamps() 
    preserveStashes(buildCount: 5) 
  }
  environment {
    SBT_CREDENTIALS = credentials('SBT')
    // CODACY_PROJECT_TOKEN = credentials('recommendation-service-codacy')
  }

  stages {
    stage('Build & Test'){
        parallel{
            stage('Lint') {
                agent {
                    docker {
                    image 'firstbird/jenkins-scala'
                    args '--net=host --group-add docker -v /var/run/docker.sock:/var/run/docker.sock -v ${HOME}/.cache/coursier:/home/jenkins/.cache/coursier -v ${HOME}/.ivy2:/home/jenkins/.ivy2 -v ${HOME}/.sbt:/home/jenkins/.sbt'
                    }
                }
                steps {
                    scmSkip(skipPattern: '.*\\[NO-CI\\].*')
                    sh 'build-setup'
                    sh 'sbt fbrdComplianceCheck'
                    stash(includes: '**/target/**', name: 'compile-output')
                }
            }
            // stage('Test') {
            //     agent {
            //         docker {
            //         image 'firstbird/jenkins-scala'
            //         args '--net=host --group-add docker -v /var/run/docker.sock:/var/run/docker.sock -v ${HOME}/.cache/coursier:/home/jenkins/.cache/coursier -v ${HOME}/.ivy2:/home/jenkins/.ivy2 -v ${HOME}/.sbt:/home/jenkins/.sbt'
            //         }
            //     }
            //     steps {
            //         scmSkip(skipPattern: '.*\\[NO-CI\\].*')
            //         sh 'build-setup'
            //         sh 'sbt coverage dockerComposeTest coverageAggregate'
            //         junit '**/test-reports/*.xml'
            //         sh 'bash <(curl -Ls https://coverage.codacy.com/get.sh) report'
            //     }
            // }
        }
    }
    

    stage('Release') {
      when { branch 'master' }
      agent {
        docker {
          image 'firstbird/jenkins-scala'
          args '--net=host --group-add docker -v /var/run/docker.sock:/var/run/docker.sock -v ${HOME}/.cache/coursier:/home/jenkins/.cache/coursier -v ${HOME}/.ivy2:/home/jenkins/.ivy2 -v ${HOME}/.sbt:/home/jenkins/.sbt'
        }
      }      
      steps {
        sh 'build-setup'
        unstash('compile-output')
        sshagent (credentials: ['Bitbucket']) { sh 'sbt "release with-defaults"' }
        stash(includes: 'version.properties', name: 'release-version')
        stash(includes: 'docs/target/site/**/*', name: 'docs-site')
        script { releaseVersion = readProperties(file: 'version.properties')['release.version'] }
        // sh "docker push firstbird/recommendation-service:${releaseVersion} && docker rmi firstbird/recommendation-service:${releaseVersion}"
      }
    }

    stage('Upload Docs') {
      when { branch 'master' }
      agent { docker { image 'firstbird/jenkins-scala:7' } }
      steps {
        unstash('docs-site')
        withAWS(credentials: 'AWS Jenkins Prod', region: 'eu-central-1') {
          s3Upload(bucket: 'docs.firstbird.com', file: 'docs/target/site', path: 'jobapi')
        }
      }
    }

    // stage("Deploy QA") {
    //   when { branch 'master' }
    //   agent { docker { image 'firstbird/jenkins-scala' } }
    //   steps {
    //     unstash('release-version')
    //     script { releaseVersion = readProperties(file: 'version.properties')['release.version'] }
    //     echo "Deploying version ${releaseVersion} to QA."
    //     withAWS(credentials: 'AWS Jenkins QA', region: 'eu-central-1') {
    //       sh "aws cloudformation update-stack --stack-name recommendation-service-qa --use-previous-template --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=Cluster,UsePreviousValue=true ParameterKey=DockerRepositorySecretArn,UsePreviousValue=true ParameterKey=Environment,UsePreviousValue=true ParameterKey=InfrastructureStackName,UsePreviousValue=true ParameterKey=ListenerArn,UsePreviousValue=true ParameterKey=ListenerRuleDomain,UsePreviousValue=true ParameterKey=ListenerRulePriority,UsePreviousValue=true ParameterKey=ServiceCpu,UsePreviousValue=true ParameterKey=ServiceDesiredInstances,UsePreviousValue=true ParameterKey=ServiceDockerImage,ParameterValue=docker.io/firstbird/recommendation-service:${releaseVersion} ParameterKey=ServiceMemory,UsePreviousValue=true ParameterKey=SubnetA,UsePreviousValue=true ParameterKey=SubnetB,UsePreviousValue=true ParameterKey=VPC,UsePreviousValue=true"
    //       sh 'aws cloudformation wait stack-update-complete --stack-name recommendation-service-qa'
    //     }
    //   }
    // }

    // stage("Deploy Staging") {
    //   when {
    //     branch 'master'
    //     beforeInput true
    //   }
    //   input { message "Deploy to staging?" }
    //   agent { docker { image 'firstbird/jenkins-scala' } }
    //   options { timeout(time: 7, unit: 'DAYS') }
    //   steps {
    //     unstash('release-version')
    //     script { releaseVersion = readProperties(file: 'version.properties')['release.version'] }
    //     echo "Deploying version ${releaseVersion} to Staging."
    //     withAWS(credentials: 'AWS Jenkins QA', region: 'eu-central-1') {
    //       sh "aws cloudformation update-stack --stack-name recommendation-service-staging --use-previous-template --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=Cluster,UsePreviousValue=true ParameterKey=DockerRepositorySecretArn,UsePreviousValue=true ParameterKey=Environment,UsePreviousValue=true ParameterKey=InfrastructureStackName,UsePreviousValue=true ParameterKey=ListenerArn,UsePreviousValue=true ParameterKey=ListenerRuleDomain,UsePreviousValue=true ParameterKey=ListenerRulePriority,UsePreviousValue=true ParameterKey=ServiceCpu,UsePreviousValue=true ParameterKey=ServiceDesiredInstances,UsePreviousValue=true ParameterKey=ServiceDockerImage,ParameterValue=docker.io/firstbird/recommendation-service:${releaseVersion} ParameterKey=ServiceMemory,UsePreviousValue=true ParameterKey=SubnetA,UsePreviousValue=true ParameterKey=SubnetB,UsePreviousValue=true ParameterKey=VPC,UsePreviousValue=true"
    //       sh 'aws cloudformation wait stack-update-complete --stack-name recommendation-service-staging'
    //     }
    //   }
    // }

    // stage("Deploy Production") {
    //   when {
    //     branch 'master'
    //     beforeInput true
    //   }
    //   input { message "Deploy to production?" }
    //   agent { docker { image 'firstbird/jenkins-scala' } }
    //   options { timeout(time: 7, unit: 'DAYS') }
    //   steps {
    //     unstash('release-version')
    //     script { releaseVersion = readProperties(file: 'version.properties')['release.version'] }
    //     echo "Deploying version ${releaseVersion} to Production."
    //     withAWS(credentials: 'AWS Jenkins Prod', region: 'eu-central-1') {
    //       sh "aws cloudformation update-stack --stack-name recommendation-service-prod --use-previous-template --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=Cluster,UsePreviousValue=true ParameterKey=DockerRepositorySecretArn,UsePreviousValue=true ParameterKey=Environment,UsePreviousValue=true ParameterKey=InfrastructureStackName,UsePreviousValue=true ParameterKey=ListenerArn,UsePreviousValue=true ParameterKey=ListenerRuleDomain,UsePreviousValue=true ParameterKey=ListenerRulePriority,UsePreviousValue=true ParameterKey=ServiceCpu,UsePreviousValue=true ParameterKey=ServiceDesiredInstances,UsePreviousValue=true ParameterKey=ServiceDockerImage,ParameterValue=docker.io/firstbird/recommendation-service:${releaseVersion} ParameterKey=ServiceMemory,UsePreviousValue=true ParameterKey=SubnetA,UsePreviousValue=true ParameterKey=SubnetB,UsePreviousValue=true ParameterKey=VPC,UsePreviousValue=true"
    //       sh 'aws cloudformation wait stack-update-complete --stack-name recommendation-service-prod'
    //     }
    //   }
    //    post {
    //     success {
    //       slackSend(channel: '#nest_core_monitoring', color: 'good', message: "Deployment <$RUN_DISPLAY_URL|#$BUILD_NUMBER> of <$JOB_DISPLAY_URL|$JOB_NAME> succeeded.")
    //     }
    //     failure {
    //       slackSend(channel: '#nest_core_monitoring', color: 'danger', message: "Deployment <$RUN_DISPLAY_URL|#$BUILD_NUMBER> of <$JOB_DISPLAY_URL|$JOB_NAME> failed.")
    //     }
    //   } 
    // }

  }
}