package com.alok.ai.spring.ollama;

import com.alok.ai.spring.model.Message;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ollama/chat")
@RestController
public class OllamaChatController {

    private final OllamaApi ollamaApi;

    private final String model;

    private final OllamaApi.Message geographyTeacherRoleContextMessage = OllamaApi.Message.builder(OllamaApi.Message.Role.ASSISTANT)
            .withContent("You are geography teacher. You are talking to a student.")
            .build();

    private final OllamaApi.Message historyTeacherRoleContextMessage = OllamaApi.Message.builder(OllamaApi.Message.Role.ASSISTANT)
            .withContent("You are history teacher. You are talking to a student.")
            .build();

    @Autowired
    public OllamaChatController(OllamaApi ollamaApi, @Value("${spring.ai.ollama.chat.model}") String model) {
        this.ollamaApi = ollamaApi;
        this.model = model;
    }

    @PostMapping(value = "/teacher/geography")
    public ResponseEntity<OllamaApi.Message> chatWithGeographyTeacher(
            @RequestBody Message message
    ) {
        return ResponseEntity.ok(ollamaApi.chat(OllamaApi.ChatRequest.builder(model)
                .withStream(false) // not streaming
                .withMessages(List.of(
                        geographyTeacherRoleContextMessage,
                        OllamaApi.Message.builder(OllamaApi.Message.Role.USER)
                                .withContent(message.question())
                                .build()
                        )
                )
                .withOptions(OllamaOptions.create().withTemperature(0.9f))
                .build()).message());
    }

    @PostMapping(value = "/teacher/history")
    public ResponseEntity<OllamaApi.Message> chatWithHistoryTeacher(
            @RequestBody Message message
    ) {
        return ResponseEntity.ok(ollamaApi.chat(OllamaApi.ChatRequest.builder(model)
                .withStream(false) // not streaming
                .withMessages(List.of(
                                historyTeacherRoleContextMessage,
                                OllamaApi.Message.builder(OllamaApi.Message.Role.USER)
                                        .withContent(message.question())
                                        .build()
                        )
                )
                .withOptions(OllamaOptions.create().withTemperature(0.9f))
                .build()).message());
    }
}
