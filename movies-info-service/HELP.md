# Getting Started

### Movie-Info Service - API Reference

## Movie-Info Service Run setup
#### 1. Start MongoDB and check Port in application.properties file.
#### 2. Start Redis and check Port in application.properties file.
#### 3. Start Application and check below requests.

## Movie-Info Service Requests
#### 1. POST-CREATE-MOVIE-INFO
```bash
curl -i -d '{"movieInfoId":1, "name": "Batman Begins", "year":2005,"casts":["Christian Bale", "Michael Cane"],"release_date": "2005-06-15"}' -H "Content-Type: application/json" -X POST http://localhost:8080/v1/movieInfos
```

```bash
curl -i \
-d '{"movieInfoId":2, "name": "The Dark Knight", "year":2008,"casts":["Christian Bale", "HeathLedger"],"release_date": "2008-07-18"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/movieInfos
```

```bash
curl -i \
-d '{"movieInfoId":null, "name": "Dark Knight Rises", "year":2012,"casts":["Christian Bale", "Tom Hardy"],"release_date": "2012-07-20"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/movieInfos
```

#### 2. GET-ALL-MOVIE-INFO

```bash
curl -i http://localhost:8080/v1/movieInfos
```
#### 3. GET-MOVIE-INFO-BY-ID

```bash
curl -i http://localhost:8080/v1/movieInfos/1
```

#### 4. GET-MOVIE-INFO-STREAM

```bash
curl -i http://localhost:8080/v1/movieInfos/stream
```

#### 5. UPDATE-MOVIE-INFO

```bash
curl -i \
-d '{"movieInfoId":1, "name": "Batman Begins", "year":2005,"casts":["Christian Bale", "Michael Cane", "Liam Neeson"],"release_date": "2005-06-15"}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8080/v1/movieInfos/1
```

#### 6. DELETE-MOVIE-INFO

```bash
curl -i -X DELETE http://localhost:8080/v1/movieInfos/1
```

#### 7. STREAM-MOVIE-INFO

```bash
curl -i http://localhost:8080/v1/movieInfos/stream
```

