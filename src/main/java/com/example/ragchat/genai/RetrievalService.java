
package com.example.ragchat.genai;

import java.util.List;

public interface RetrievalService {
    List<RetrievedChunk> retrieve(String query, int k);
}
