package gropoid.punter.domain;

import java.util.List;

import javax.inject.Inject;

public class QuestionManager {
    private static final int MAX_TYPES = 2;
    private List<Game> gamePool;

    @Inject
    GameManager gameManager;

    @Inject
    public QuestionManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private int randType() {
        return (int)(Math.random() * 1000) % MAX_TYPES - 1;
    }

    public class Type {
        public static final int RELEASE_DATE = 0;
        public static final int CHARACTER = 1;
    }

    public void generateQuestions() {
        gamePool = gameManager.getAllGames();
    }


    private Question generateQuestion() {
        Question question = new Question();
        question.setType(randType());
        return null;
    }

}
