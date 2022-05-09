# URL shortener service

* Creates short URL from provided (long) URL
* Redirect requests with short URL to (long) URL
* Short URL key is created from random characters  
* Short URL key default length is 5
* Short url keys and long urls are persisted in a Postgres database (running locally)
* Requests to URL redirects are cached in Redis (running locally)

### Build and run

Requires Java 17 and Maven

#### Docker

`mvn clean package`

`docker-compose up -d`

#### Tests

`mvn clean test`

### Usage

The application will accept these http requests to port `8080`

#### GET /{urlKey}

Response status:`307 Temporary redirect`, `404 Not found`, `400 Bad request`

Example: `curl -v "localhost:8080/1uYZj"`


#### POST /url_shortener

Response status: `200 OK`, `400 Bad request`

Example: `curl -v -H 'Content-Type: application/json' "localhost:8080/url_shortener" -X POST -d '{"url": "https://www.dice.se"}'`
