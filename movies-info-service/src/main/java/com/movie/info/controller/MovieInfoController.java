package com.movie.info.controller;

import com.movie.info.cache.MovieInfoServiceCacheHandler;
import com.movie.info.model.MovieInfo;
import com.movie.info.service.MovieInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/v1")
@Slf4j
public class MovieInfoController {

    //@Autowired
    private MovieInfoService movieInfoService;

    private MovieInfoServiceCacheHandler movieInfoServiceCacheHandler;

    //Replay all added data to all subscriber (old and news)
    //Sinks.Many<MovieInfo> moviesInfoSink = Sinks.many().replay().all();

    // Only latest data found for new subscribers
    Sinks.Many<MovieInfo> moviesInfoSink = Sinks.many()
            .replay()
            .latest();

    public MovieInfoController(MovieInfoService movieInfoService, MovieInfoServiceCacheHandler movieInfoServiceCacheHandler) {
        this.movieInfoService = movieInfoService;
        this.movieInfoServiceCacheHandler = movieInfoServiceCacheHandler;
    }

    @PostMapping("/movieInfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo){

        log.info("MovieInfo going to be add : {} ", movieInfo);
        //normal flow
        //return movieInfoService.addMovieInfo(movieInfo).log();

        //Sink FLow : pushblish continew to new moviews to subscribers
        return movieInfoService.addMovieInfo(movieInfo)
                .doOnNext(savedInfo -> moviesInfoSink.tryEmitNext(savedInfo));

    }

    @GetMapping(value = "/movieInfos/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MovieInfo> getMovieInfoStream() {

	 return moviesInfoSink.asFlux().log();
    }

    @GetMapping("/movieInfos")
    public Flux<MovieInfo> getAllMovieInfos( @RequestParam(value = "year", required = false) Integer year,
                                             @RequestParam(value = "name", required = false) String name){
        if(year !=null) {
            log.info("Search Param Year : {} ", year);
            return movieInfoService.getMovieInfoByYear(year);
        }
        if(name !=null) {
            log.info("Search Param Name : {} ", name);
            return movieInfoService.getMovieInfoByName(name);
        }
        return movieInfoService.getAllMovieInfos().log();
    }
    /*
        @GetMapping("/movieInfos/{id}")
        public Mono<MovieInfo> getMovieInfoById(@PathVariable String id){
            return movieInfoService.getMovieInfoById(id).log();
        }
    */
    @GetMapping("/movieInfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById_approach2(@PathVariable("id") String id) {
        System.err.println("In Controller: ");
		/*
	    return movieInfoServiceCacheHandler.getMovieInfoById(id)
            .map(movieInfo1 -> ResponseEntity.ok()
            .body(movieInfo1))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .log();
        */
	 // Using Redis Cache
        return Mono.just(ResponseEntity.ok().body( movieInfoServiceCacheHandler.getMovieInfoById(id)));
    }

    @PutMapping("/movieInfos/{id}")
    public Mono<MovieInfo> updateMovieInfo(@RequestBody MovieInfo movieInfo, @PathVariable String id){

        return movieInfoService.updateMovieInfo(movieInfo, id).log();
    }

    @PutMapping("/movieInfos2/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo_approach2(@RequestBody MovieInfo movieInfo, @PathVariable String id){

        return movieInfoService.updateMovieInfo(movieInfo, id)
                .map( ResponseEntity.ok()::body) //parse by method reference
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();

	/* Parse by lambda expression
	 * 	return movieInfoService.updateMovieInfo(movieInfo,  id)
				.map(mInfo -> {
					return ResponseEntity.ok().body(mInfo);
				}).log();
	*/
    }

    @DeleteMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id){

        return movieInfoService.deleteMovieInfo(id).log();
    }

}
