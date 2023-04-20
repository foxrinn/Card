package org.model;

import java.util.Date;
import java.util.Objects;

public class Card {

    private int id;
    private String question;
    private String answer;
    private int idCategory;
    private Date creationDate;

    public Card() {
    }

    public Card(int id, String question, String answer, int idCategory, Date creationDate) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.idCategory = idCategory;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && idCategory == card.idCategory && Objects.equals(question, card.question) && Objects.equals(answer, card.answer) && Objects.equals(creationDate, card.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answer, idCategory, creationDate);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", idCategory=" + idCategory +
                ", creationDate=" + creationDate +
                '}';
    }
}
