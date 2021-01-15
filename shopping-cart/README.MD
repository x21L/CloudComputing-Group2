### Microservice-Shopping-Cart

##### Setting up using Kind

Delete cluster if already running

~~~console
kind delete cluster
~~~

Create cluster and mocked docker registry called localhost:5000 (Use Git Bash on Windows)

~~~console
./kind-local-registry.sh
~~~

Apply namespace for the ``search-environment``

~~~console
kubectl apply -f namespace.yaml
~~~

### We start with the tomcat server

First run

~~~console
docker build --no-cache -f Dockerfile -t localhost:5000/tomcat-shoppingcart:1.0 .
~~~

> ***NOTE***: Do not forget the . at the end. It tells docker to use a local dir.

Now we are going to push the mocked docker registry called localhost:5000

~~~console
docker push localhost:5000/tomcat-shoppingcart:1.0
~~~

We apply the tomcat deployment file to get our desired environment.

~~~console
kubectl apply -f tomcat.yaml
~~~

Check if everything is up and running

~~~console
kubectl get all --namespace shoppingcart-environment

NAME                                       READY   STATUS    RESTARTS   AGE
pod/shoppingcart-tomcat-5ff6545b8b-gcctz   1/1     Running   0          8s

NAME                          TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)   AGE
service/tomcat-shoppingcart   ClusterIP   10.110.228.26   <none>        80/TCP    8s

NAME                                  READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/shoppingcart-tomcat   1/1     1            1           8s

NAME                                             DESIRED   CURRENT   READY   AGE
replicaset.apps/shoppingcart-tomcat-5ff6545b8b   1         1         1       8s
~~~

### In the next step we set up the database

Change into the `db` dir and run the Dockerfile

~~~console
cd db   
docker build --no-cache -f Dockerfile -t localhost:5000/mysql-shoppingcart:1.0 .
~~~

The next step is pushing the docker file to our registry called localhost:5000.

~~~console
docker push localhost:5000/mysql-shoppingcart:1.0
~~~

We apply the mysql deployment file

~~~
cd ..
kubectl apply -f mysql.yaml
~~~

Check if everything is up and running

~~~console
kubectl get all --namespace shoppingcart-environment
NAME                                       READY   STATUS    RESTARTS   AGE
pod/shoppingcart-mysql-5ccf6ddd5d-wwmcg    1/1     Running   0          14s
pod/shoppingcart-tomcat-5ff6545b8b-gcctz   1/1     Running   0          25m

NAME                          TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/mysql-shoppingcart    ClusterIP   10.111.224.155   <none>        3306/TCP   14s
service/tomcat-shoppingcart   ClusterIP   10.110.228.26    <none>        80/TCP     25m

NAME                                  READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/shoppingcart-mysql    1/1     1            1           14s
deployment.apps/shoppingcart-tomcat   1/1     1            1           25m

NAME                                             DESIRED   CURRENT   READY   AGE
replicaset.apps/shoppingcart-mysql-5ccf6ddd5d    1         1         1       14s
replicaset.apps/shoppingcart-tomcat-5ff6545b8b   1         1         1       25m
~~~
