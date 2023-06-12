Requires docker 

## Running

In the project directory, you can run:

### `docker build -t graph-service .`

### `docker run -p 8080:8080 --network ${NETWORK} --name graph-service graph-service`

### test it is working
`curl localhost:8080/health`