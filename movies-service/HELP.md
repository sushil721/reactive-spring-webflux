# Getting Started

### Movie-Service - API Reference

## Movie-Service Run setup
#### 1. Start MongoDB and check Port in application.yml file.
#### 2. Start Redis and check Port in application.yml file.
#### 3. Start Application and check below requests.
#### 4. Start Movie-Review and Movie-Info Services and check below requests.

## Movie-Service Requests

Movie-Service
===========

#### 1. POST-CREATE-MOVIE-INFO
```bash
curl -i \
-d '{"movieInfoId":1, "name": "Batman Begins", "year":2005,"cast":["Christian Bale", "Michael Cane"],"release_date": "2005-06-15"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/movieinfos
```
```bash
curl -i \
-d '{"movieInfoId":2, "name": "The Dark Knight", "year":2008,"cast":["Christian Bale", "HeathLedger"],"release_date": "2008-07-18"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/movieinfos
```

#### 2. POST-REVIEW
```bash
curl -i \
-d '{"reviewId":1, "movieInfoId":1, "comment": "Awesome Movie", "rating":9.0}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/v1/reviews
```
```bash
curl -i \
-d '{"reviewId":2, "movieInfoId":1, "comment": "Excellent Movie", "rating":8.0}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/v1/reviews
```