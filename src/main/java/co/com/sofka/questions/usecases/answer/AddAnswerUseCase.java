package co.com.sofka.questions.usecases.answer;

import co.com.sofka.questions.mappers.MapperUtils;
import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.usecases.interfaces.SaveAnswer;
import co.com.sofka.questions.usecases.question.GetQuestionUseCase;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Validated
public class AddAnswerUseCase implements SaveAnswer {
    private final AnswerRepository answerRepository;
    private final MapperUtils mapperUtils;
    private final GetQuestionUseCase getQuestionUseCase;

    public AddAnswerUseCase(MapperUtils mapperUtils, GetQuestionUseCase getQuestionUseCase, AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
        this.getQuestionUseCase = getQuestionUseCase;
        this.mapperUtils = mapperUtils;
    }

    public Mono<QuestionDTO> apply(AnswerDTO answerDTO) {
        Objects.requireNonNull(answerDTO.getQuestionId(), "Id of the answer is required");
        return getQuestionUseCase.apply(answerDTO.getQuestionId()).flatMap(question ->
                answerRepository
                        .save(mapperUtils.mapperToAnswer().apply(answerDTO))
                        .map(answer -> {
                            question.getAnswers().add(answerDTO);
                            return question;
                        })
        );
    }

}