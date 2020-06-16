pipeline{
     // 定义groovy脚本中使用的环境变量
    environment{
        IMAGE_TAG =  sh(returnStdout: true,script: 'echo $image_tag').trim()
        ORIGIN_REPO =  sh(returnStdout: true,script: 'echo $origin_repo').trim()
        REPO =  sh(returnStdout: true,script: 'echo $repo').trim()
        BRANCH =  sh(returnStdout: true,script: 'echo $branch').trim()
        GIT_URL =  sh(returnStdout: true,script: 'echo $git_url').trim()
        DEPLOY =  sh(returnStdout: true,script: 'echo $deploy').trim()
        scannerHome = tool 'SonarQube';
    }

    // 定义本次构建使用哪个标签的构建环境，本示例中为 “slave-pipeline”，K8S Jenkins salve 自动创建K8S pod
    agent{
        node{
            label 'local-slave-pipeline'
        }
    }

    //附加选项
    options {
        //超时时间20分钟
        timeout(20)
    }

    // "stages"定义项目构建的多个模块，可以添加多个 “stage”， 可以多个 “stage” 串行或者并行执行
    stages{
        // 添加第一个stage， 拉取指定分支代码
        stage('Git'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '${BRANCH}']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'GitTagMessageExtension', useMostRecentTag: true]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '7974b32b-f5c5-42da-bb0e-76699afe9c2f', url: '${GIT_URL}']]])
            }
        }
        
        // 添加第二个stage， 运行源码打包命令
        stage('Build Package'){
            parallel {
                stage('mvn test Package'){
                    when {
                        expression {
                            "$IMAGE_TAG" ==~ "^test_.*"
                        }
                    }
                    steps{
                        container("node-maven") {
                            sh "mvn clean package -B -P test"
                        }
                    }
                }
                stage('mvn prod Package'){
                    when {
                        expression {
                            "$IMAGE_TAG" ==~ "^prod_.*"
                        }
                    }
                    steps{
                        container("node-maven") {
                            sh "mvn clean package -B -P prod"
                        }
                    }
                }
            }
        }

        // 添加第三个stage, 运行SonarQube 扫描
        stage('SonarQube'){
            steps{
                container("node-maven") {
                    withSonarQubeEnv('SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner -Dproject.settings=sonar-scanner.properties"
                    }
                }
            }
        }


        // 添加第四个stage, 运行容器镜像构建推送命令
        stage('Image Build And Publish'){
            steps{
                container("kaniko") {
                    sh label: '', script: '''kaniko -f `pwd`/Dockerfile-ccs -c `pwd` --destination=${origin_repo}/${repo}:${image_tag}_ccs
                                            kaniko -f `pwd`/Dockerfile-tcp-monitor -c `pwd` --destination=${origin_repo}/${repo}:${image_tag}_tcp-monitor'''
                }
            }
        }

        // 添加第五个stage, 发布至K8S；根据分支判断，k8s yaml配置发布
        stage('Deploy to Kubernetes') {
            //并行执行
            parallel {
                //发布至正式环境（master分支）
                stage('Deploy to Prod') {
                    when {
                        expression {
                            "$IMAGE_TAG" ==~ "^prod_.*"
                        }
                    }
                    steps {
                        container('kubectl') {
                            kubernetesDeploy (configs: 'prod.yaml', kubeconfigId: '2af62a02-14ca-441f-8850-5d033a9ad78f')
                        }
                    }
                }
                //发布至测试环境（）
                stage('Deploy to Test') {
                    when {
                        expression {
                            "$IMAGE_TAG" ==~ "^test_.*"
                        }
                    }
                    steps {
                        container('kubectl') {
                            kubernetesDeploy (configs: 'test.yaml', kubeconfigId: '30b6024a-480c-4841-83f0-07cafba99c9c')
                        }
                    }
                }
            }
        }
    }

    //附加步骤，发送构建情况
    post {
        //构建成功
        success {
            withSonarQubeEnv('SonarQube') {
                sh label: '', script: 'python dingding.py pass'
            }
        }
        //构建失败
        unsuccessful {
            withSonarQubeEnv('SonarQube') {
                sh label: '', script: 'python dingding.py fail'
            }
        }
    }
}
