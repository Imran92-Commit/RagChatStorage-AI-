
package com.example.ragchat.genai;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgVectorRetrievalService implements RetrievalService {
    private final NamedParameterJdbcTemplate jdbc;
    private final EmbeddingClient embeddings;

    public PgVectorRetrievalService(NamedParameterJdbcTemplate jdbc, EmbeddingClient embeddings){ this.jdbc=jdbc; this.embeddings=embeddings; }

    @Override
    public List<RetrievedChunk> retrieve(String query, int k) {
        float[] vec = embeddings.embed(query);
        if (vec == null) return List.of();
        String sql = "SELECT id::text, text, metadata->>'source' as source, (embedding <-> :q) as score FROM doc_chunks ORDER BY embedding <-> :q LIMIT :k";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("q", vec).addValue("k", k);
        try {
            return jdbc.query(sql, params, (rs, i) -> new RetrievedChunk(rs.getString("id"), rs.getString("text"), rs.getDouble("score"), rs.getString("source")));
        } catch (Exception e) {
            return List.of(); // extension missing or table absent
        }
    }
}
