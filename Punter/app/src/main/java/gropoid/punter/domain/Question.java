package gropoid.punter.domain;


public class Question {
    private long id;


    private int type;

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;

    private Game correctAnswer;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Game getGame1() {
        return game1;
    }

    public void setGame1(Game game1) {
        this.game1 = game1;
    }

    public Game getGame2() {
        return game2;
    }

    public void setGame2(Game game2) {
        this.game2 = game2;
    }

    public Game getGame3() {
        return game3;
    }

    public void setGame3(Game game3) {
        this.game3 = game3;
    }

    public Game getGame4() {
        return game4;
    }

    public void setGame4(Game game4) {
        this.game4 = game4;
    }

    public Game getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Game correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
