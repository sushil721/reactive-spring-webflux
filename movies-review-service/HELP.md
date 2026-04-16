# Getting Started

### Movie-Review Service - API Reference

## Movie-Review Service Run setup
#### 1. Start MongoDB and check Port in application.properties file.
#### 2. Start Redis and check Port in application.properties file.
#### 3. Start Application and check below requests.

## Movie-Review Service Requests

Movie-Review
============

#### 1. POST-REVIEW:
```bash
curl -i \
-d '{"reviewId":1, "movieInfoId":1, "comment": "Excellent Movie", "rating":8.0}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/v1/reviews
```
```bash
curl -i \
-d '{"reviewId":2, "movieInfoId":2, "comment": "Excellent Movie", "rating":8.0}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/v1/reviews
```
```bash
curl -i \
-d '{"reviewId":null, "movieInfoId":1, "comment": "Awesome Movie", "rating":9.0}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/v1/reviews
```

#### 2. GET-ALL-REVIEWS:
```bash
curl -i http://localhost:8081/v1/reviews
```

#### 3. GET-ALL-REVIEWS-BY-MOVIE-INFO-ID:
```bash
curl -i http://localhost:8081/v1/reviews?movieInfoId=1
```
```bash
curl -i http://localhost:8081/v1/reviews?movieInfoId=2
```
#### 4. GET-ALL-REVIEWS-STREAM:
```bash
curl -i http://localhost:8081/v1/reviews/stream
```

#### 5. UPDATE-REVIEW:
```bash
curl -i \
-d '{"reviewId":1, "movieInfoId":1, "comment": "Excellent Movie Update", "rating":8.5}' \
-H "Content-Type: application/json" \
-X PUT http://localhost:8081/v1/reviews/1
```

#### 6. DELETE-MOVIE-REVIEW:
```bash
curl -i -X DELETE http://localhost:8081/v1/reviews/1
```