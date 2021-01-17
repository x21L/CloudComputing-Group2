### Microservice-Search

##### Setting up using Kind

Delete cluster if already running
```console
kind delete cluster
```

Create cluster and mocked docker registry called localhost:5000 (Use Git Bash on Windows)

```console
./kind-local-registry.sh
```

Apply namespace for the ``frontend-environment``

```console
kubectl apply -f namespace.yaml
```

Build the react frontend 

```console
cd bookstore
docker build -f Dockerfile -t localhost:5000/bookstore-frontend:1.0 .
```

Push the created Frontend Docker image into the mocked docker registry called localhost:5000  

```console
docker push localhost:5000/bookstore-frontend:1.0
```

Go back to the folder frontend-bookstore

```console
cd .. 
```

Apply the frontend deployment settings for the ``frontend-environment``

```console
kubectl apply -f deployment.yaml
```

Apply the frontend service settings for the ``frontend-environment``

```console
kubectl apply -f service.yaml
```

Check if everything is working
```console
kubectl get all --namespace frontend-environment
```

```console
NAME                                      READY   STATUS    RESTARTS   AGE
pod/bookstore-frontend-657b577686-x422s   1/1     Running   0          14m

NAME                         TYPE           CLUSTER-IP   EXTERNAL-IP    PORT(S)        AGE
service/bookstore-frontend   LoadBalancer   10.8.10.8    34.121.83.50   80:30911/TCP   35h

NAME                                 READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/bookstore-frontend   1/1     1            1           35h

NAME                                            DESIRED   CURRENT   READY   AGE
replicaset.apps/bookstore-frontend-657b577686   1         1         1       14m
```

