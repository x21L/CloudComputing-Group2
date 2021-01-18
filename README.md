# Cloud Computing Group2

## Team Members
* Christoph Burghuber
* Markus Unterdechler
* Lukas Wais

# Bookstore

## Overview

The bookstore is spliited up into 3 Microservices:
* Frontend
* Book Search
* Shopping Cart

The Microservices are running in GoogleCloud.

The aim of this project was to automate the entire process from source-code change to GoogleCloud deployment.

The Microservices are created in different programming languages:
* Java (Microservice)
* .NET C# (Microservice)
* React (Frontend)

...blablabla

## Microservice Frontend

The frontend is implemented in React.
This minimalized implementation of a web-shop-page contains a search-field a book list and a shopping cart. Books can be added to the shopping cart by pressing a button. The shopping cart contains a list of books that have been added and a remove button.

![alt tag](frontend-pic.png)

### Docker image

The dockerfile for building up the docker image basically copies the needed code, installs needed Node.js and react-scrips and starts the application with 'npm starts'.

```console
FROM node:13.12.0-alpine

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH

COPY package.json ./
COPY package-lock.json ./
RUN npm install --silent
RUN npm install react-scripts@3.4.1 -g --silent

COPY . ./
CMD ["npm", "start"]
```

### Google Cloud

For the frontend microservice a Deployment and a Service are created.

The Deployment takes care that the image is always available. We use 'replicas: 2' and 'type: RollingUpdate' to have at least one available pod at each time when doing a rollup update.

```console
...
spec:
  replicas: 2
  selector:
    matchLabels:
      app: bookstore-frontend
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
...
```

It is also defined that the Deployment should provide the docker image 'cloudcompdocker/bookstore-frontend:latest' and that this image should be pulled even if docker-tag not changed with 'imagePullPolicy: Always'. (cause in this project it is always :latest)

```console
...
      containers:
      - name: bookstore-frontend
        image: cloudcompdocker/bookstore-frontend:latest
        imagePullPolicy: Always
...
```

The Service provides a fixed access point for the service. Since the Service should be reachable from outside a 'LoadBalancer' is used. The external port 80 (HTTP) is forwarded to internal port 3000.

```console
...
spec:
  ports:
  - port: 80
    protocol: TCP
    targetPort: 3000
  selector:
    app: bookstore-frontend
  type: LoadBalancer
```

### GitHub Actions

The .github\workflows\frontend.yml contains the configuration for the frontend github actions.

First we define the event that automatically triggers the GitHub-Actions. In this case after every 'push' request inside of the frontend-bookstore folder.

```console
name: frontend_CICD

on:
  workflow_dispatch:
  push:
    paths:
    - 'frontend-bookstore/**'
...
```

At next we define the jobs which should be executed. 'runs-on: ubuntu-latest' means that the jobs are executed on a fresh virtual machine hosted by GitHub. All jobs are listed under 'steps'.

```console
...

jobs:
  path-context:
    runs-on: ubuntu-latest
    steps:

...
```

The first job is to checkout the code.

```console
...
    -
        name: Checkout
        uses: actions/checkout@v2
...
```

At next we build the docker image, login to docker hub and push the created docker image. The docker image is called 'cloudcompdocker/bookstore-frontend'.

```console
...
      -
        name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v1
      -
        name: Login to DockerHub
        uses: docker/login-action@v1
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PWD }}
      -
        name: Build and push
        uses: docker/build-push-action@v2
        with:
          context: ./frontend-bookstore/bookstore
          push: true
          tags: cloudcompdocker/bookstore-frontend:latest
...
```

For deploying to google cloud we have first to connect to the corresponding google cloud cluster. For this a service-account-key was created. With service-account-key, project-id, cluster-name and Location it is possible to get a connection to the cluster. After that 'kubectl' can be used for modifications.

```console
...
      - 
        uses: google-github-actions/setup-gcloud@master
        with:
          version: '290.0.1'
          service_account_key: ${{ secrets.GK_KEY }}
          project_id: ${{ secrets.GK_PROJECT_ID }}
      - 
        run: |-
          gcloud container clusters get-credentials "cluster-1" --zone "us-central1-c"
...
```

First we apply the 'namespace.yaml' for setting up the frontend-environment namespace. The 'deployment.yaml' will apply Deployment and 'service.yaml' the Cloud Service.

```console
...
- 
        name: Update Deployment and Services
        run: |-
          kubectl apply -f ./frontend-bookstore/namespace.yaml
          kubectl apply -f ./frontend-bookstore/deployment.yaml
          kubectl apply -f ./frontend-bookstore/service.yaml
...
```

At the and we start the rollout.

```console
...
- 
        name: Deploy
        run: |-
          kubectl rollout restart -f ./frontend-bookstore/deployment.yaml --namespace frontend-environment
```

## Microservice Search

## Microservice Shopping Cart

The microservice shopping cart consists of two major components.

* An Apache Tomcat
* A MySQL Database

![shopping-cart diagramm](/shopping-cart/img/shoppingcart.png)

Each of them is one pod in kubernetes, and they are in the same namespace. The Tomcat is the RestFul API and the MySQL
holds the actual data for the shopping cart.

### Docker image

#### Apache Tomcat

~~~shell
FROM tomcat

COPY target/shopping-cart-1.0.war /usr/local/tomcat/webapps/
COPY mysql-connector-java-8.0.22.jar /usr/local/tomcat
CMD ["catalina.sh", "run"]
~~~

Very important are the two copy statements. The first one copies the generated `War` file from the build to the
`webapps` directory. It makes your programed service availabele und `http://<server-ip>/name-version`. The name and the
version are specified in the `pom.xml` file.

The second copy statement copies the mysql connector to the `CATALINA_HOME` directory. The docker equievalent to this is
`/usr/local/tomcat`.

#### MySQL

~~~shell
FROM mysql

ENV MYSQL_ROOT_PASSWORD=password
~~~

This is a very simple Dockerfile. It just gets the MySQL image and sets the root password to password. This is of
course **not** recommended for production.

## Google Cloud

For the shopping cart microservice a Deployment and a Service are also created, like the frontend microservice.

In addition to the frontend we use `replicas: 1` and we also use the `RollingUpdate` strategy.

~~~shell 
...
spec:
  replicas: 1
  selector:
    matchLabels:
      app: tomcat-shoppingcart
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
...
~~~

We also want to pull the latest image from `cloudcompdocker/shoppingcart-webapi`. This image should be pulled even if
docker-tag not changed with `imagePullPolicy: Always`. Moreover, the port is set to 8080.

~~~shell
...
containers:
        - name: shoppingcart-environment
          image: cloudcompdocker/shoppingcart-webapi:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
...
~~~

The deployment for the mysql looks very similar we also use 1 replica, but the app is called mysql-shoppingcart. You can
spot differences in the container section. For the database we use the port 3306.

~~~shell
...
metadata:
  namespace: shoppingcart-environment
  name: mysql-shoppingcart
spec:
  ports:
    - port: 3306
      targetPort: 3306
...
~~~

As an attentive reader you will have noticed that both pods are in the same namespace called `shoppingcart-environment`.
The Tomcat server should be available from outside, thus we use a load balancer, and a fixed access point.

~~~shell
 ...
  selector:
    app: tomcat-shoppingcart
  type: LoadBalancer
...
~~~

The database is not public available, for this we are using a static cluster ip address.

~~~shell
...
  selector:
    app: mysql-shoppingcart
  clusterIP: 10.8.11.20
  type: ClusterIP
~~~

### GitHub Actions

In general, this works exactly like the frontend microservice. It gets triggerd by a push to `shopping-cart` directory.

~~~shell
on:
  workflow_dispatch:
  push:
    paths:
     - 'shopping-cart/**'
...
~~~

One major difference is there to point out. As job we can automatically build the War file with Apache Maven.

~~~shell
...
- name: Set up JDK 1.8
        uses: actions/setup-java@v1
        with:
          java-version: 1.8
      - name: Build War File
        run: cd ./shopping-cart && mvn package && cd ..
...
~~~

After the build succeeds the webservice is available under   
`http://35.239.83.61/shopping-cart-1.0/ShoppingCart?action=<parameter>`
