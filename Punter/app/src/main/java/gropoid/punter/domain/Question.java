package gropoid.punter.domain;


public class Question {
    private long id;

    private int type;

    private Game[] games = new Game[4];

    private Game correctAnswer;

    private long correctAnswerCriterion;

    private String wording;

    public class Type {
        public static final int RELEASE_DATE = 0;
        public static final int WAS_RELEASED_ON_PLATFORM = 1;
        public static final int WAS_NEVER_RELEASED_ON_PLATFORM = 2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }

    public Game getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Game correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCorrectAnswerCriterion(long correctAnswerCriterion) {
        this.correctAnswerCriterion = correctAnswerCriterion;
    }

    public long getCorrectAnswerCriterion() {
        return correctAnswerCriterion;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

}
