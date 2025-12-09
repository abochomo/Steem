package com.es.unex.cum.mdai.Steem.Services;


import com.es.unex.cum.mdai.Steem.Modelo.Juego;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String modelId;

    private final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public String obtenerRecomendacion(String userPrompt, List<Juego> juegosDisponibles) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Convertimos la lista de juegos a un String legible para la IA
        String listaJuegosTexto = juegosDisponibles.stream()
                .map(j -> "- ID: " + j.getIdJuego() + ", Título: " + j.getTitulo() + ", Desc: " + j.getDescripcion() + ", Precio: " + j.getPrecio())
                .collect(Collectors.joining("\n"));

        // 2. Preparamos el Prompt del Sistema (Las instrucciones estrictas)
        String systemPrompt = "Eres un experto asistente de ventas de videojuegos de la tienda 'Steem'. " +
                "Tienes la siguiente lista de juegos disponibles en stock:\n" + listaJuegosTexto + "\n\n" +
                "INSTRUCCIONES:\n" +
                "1. Basado en el gusto del usuario, selecciona EL MEJOR juego de la lista.\n" +
                "2. Tu respuesta debe ser SOLO el ID del juego seleccionado. Nada más. Solo el número. \n" +
                "3. Si no hay ningún juego que coincida con los gustos del usuario, responde: -1\n";

        // 3. Construimos el cuerpo de la petición JSON
        Map<String, Object> body = new HashMap<>();
        body.put("model", modelId);

        Map<String, String> msgSystem = Map.of("role", "system", "content", systemPrompt);
        Map<String, String> msgUser = Map.of("role", "user", "content", "Me gustan este tipo de juegos: " + userPrompt);

        body.put("messages", List.of(msgSystem, msgUser));

        // 4. Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        // OpenRouter requiere referer/title opcionales, pero es buena práctica
        headers.set("HTTP-Referer", "http://localhost:8080");
        headers.set("X-Title", "Steem App");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // 5. Llamada a la API
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);

            // 6. Parsear respuesta (extraer el contenido del mensaje)
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String contenido = (String) message.get("content");

            return contenido.trim(); // Debería devolver el ID del juego (ej: "5")

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // Captura errores 4xx (como 401 Unauthorized o 402 Payment Required)
            System.err.println("Error Cliente HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            // Captura otros errores
            System.err.println("Error general conectando con IA:");
            e.printStackTrace();
            return null;
        }
    }
}
