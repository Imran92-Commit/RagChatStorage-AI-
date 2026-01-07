
package com.example.ragchat.genai;

public class RetrievedChunk {
    public String id;
    public String text;
    public double score;
    public String source;
    public RetrievedChunk(String id, String text, double score, String source){ this.id=id; this.text=text; this.score=score; this.source=source; }
}
