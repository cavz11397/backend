package co.com.sofka.questions.routers;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.usecases.question.CreateQuestionUseCase;
import co.com.sofka.questions.usecases.question.DeleteUseCase;
import co.com.sofka.questions.usecases.question.GetQuestionUseCase;
import co.com.sofka.questions.usecases.question.ListQuestionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CreateRoute {

    @Bean
    public RouterFunction<ServerResponse> createQuestion(CreateQuestionUseCase createUseCase) {
        return route(POST("/create").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(QuestionDTO.class)
                        .flatMap(questionDTO -> createUseCase.apply(questionDTO)
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(MediaType.TEXT_PLAIN)
                                        .bodyValue(result))
                        )
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getQuestion(GetQuestionUseCase getQuestionUseCase) {
        return route(
                GET("/get/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getQuestionUseCase.apply(
                                request.pathVariable("id")),
                                QuestionDTO.class
                        ))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> deleteQuestion(DeleteUseCase deleteUseCase) {
        return route(
                DELETE("/delete/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(deleteUseCase.apply(request.pathVariable("id")), Void.class))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllQuestions(ListQuestionUseCase listQuestionUseCase) {
        return route(
                GET("/getAll").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(listQuestionUseCase.get(), QuestionDTO.class))
        );
    }


}
