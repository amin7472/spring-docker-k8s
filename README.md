## Welcome to user-service
In this project we have simple contact service.

###Technologies
* Spring-boot
* Postgres
* H2
* Liquibase
* Junit
* Mockito
* Docker
* Kubernetes

##How to run ? 
1. Install [docker](https://phoenixnap.com/kb/how-to-install-docker-on-ubuntu-18-04)
2. Install [minikube](https://phoenixnap.com/kb/install-minikube-on-ubuntu)
3. Run below command to start minikube
    > minikube start
4. Run below command to access minikube docker images
    > eval $(minikube docker-env)
5. Get to user-service path on commandline
    > cd user-service
6. Build user-service docker image (please wait for a few minutes)
    > docker build -t user-service:1.0 .
7. Run postgres kubernetes deployment
    > kubectl apply -f k8s/postgres/
8. Run user-service kubernetes deployment
    > kubectl apply -f k8s/service/deployment.yml
9. Then run below command to get access service on you localhost
    > kubectl  port-forward svc/user-service-k8s-service 8080:8080
                                                                    
10. click [here](localhost:8080/swagger-ui.html) to access swagger


> **Note:**
>- Recomand
>- If use the minikube as kubernetes node , step 3,4,5,6 must be run on same terminal command line.


