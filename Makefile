all: build

up:
	docker-compose --file docker/docker-compose.yml up

down:
	docker-compose --file docker/docker-compose.yml down

build:
	docker-compose --file docker/docker-compose.yml up --build

# is blocking as content will be application/json by default when Accept: text/event-stream not supplied
stream-classic-blocking:
	curl "http://localhost:8080/reactive/classic/employee/paged?page=0&size=100"

stream-classic-reactive:
	curl -H "Accept: text/event-stream" "http://localhost:8080/reactive/classic/employee/paged?page=0&size=100"

stream-handler-reactive:
	curl -H "Accept: text/event-stream" "http://localhost:8080/reactive/handler/employee/paged?page=0&size=100"