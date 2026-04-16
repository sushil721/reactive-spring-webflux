package com.movie.info.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MovieInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String movieInfoId;

    @NotBlank(message = "MovieInfo name must not be blank")
    private String name;

    @NotNull(message = "MovieInfo year must be positive")
    private Integer year;

    @NotEmpty
    private List<@NotBlank(message = "MovieInfo cast must be present") String> casts;

    @NotNull(message = "MovieInfo release_date must be present")
    private LocalDate release_date;

}
