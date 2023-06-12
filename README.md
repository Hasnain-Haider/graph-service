Requires docker 

## Running

In the project directory, you can run:

**Build**
### `docker build -t graph-service .`

**Build for m1 Mac**
### `docker buildx build --platform=linux/amd64 -t graph-service:latest .`


### `docker run -p 8080:8080 --network ${NETWORK} --name graph-service graph-service`

### test it is working
`curl localhost:8080/health`