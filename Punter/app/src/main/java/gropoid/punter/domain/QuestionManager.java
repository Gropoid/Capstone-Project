package gropoid.punter.domain;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import gropoid.punter.data.Repository;
import gropoid.punter.domain.Question.Type;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class QuestionManager {
    private static final int MAX_TYPES = 3;
    public static final int POSSIBLE_ANSWERS = 4;
    public static final int MAX_LOOPS = 15;
    private List<Game> gamePool;
    private Hashtable<Long, Platform> platformPool;

    @Inject
    GameManager gameManager;

    @Inject
    Repository repository;

    @Inject
    public QuestionManager(GameManager gameManager, Repository repository) {
        this.gameManager = gameManager;
        this.repository = repository;
    }

    private int randType() {
        return (int) Math.floor(Math.random() * MAX_TYPES);
    }

    private int randGame() {
        return randomIntUnder(gamePool.size());
    }

    private int randOptions() {
        return randomIntUnder(POSSIBLE_ANSWERS);
    }

    private int randomIntUnder(int value) {
        return (int) Math.floor(Math.random() * value);
    }


    @DebugLog
    public void generateQuestions(int questionPoolSize) {
        gamePool = gameManager.getAllGames();
        buildPlatformPool();
        if (gamePool.size() < 20) {
            Timber.w("Not enough games in db to generate questions");
            return;
        }
        if (platformPool.size() < 20) {
            Timber.w("Not enough games in db to generate questions");
            return;
        }
        while(repository.getQuestionsCount() < questionPoolSize) {
            Question question = generateQuestion();
            if (question != null) {
                repository.save(generateQuestion());
            }
            Timber.v("Questions count in db : %s ", repository.getQuestionsCount());
        }
    }

    private void buildPlatformPool() {
        platformPool = new Hashtable<>();
        for(Platform platform: gameManager.getAllPlatforms()) {
            platformPool.put(platform.getId(), platform);
        }
    }

    private Question generateQuestion() {
        Question question = new Question();
        question.setType(randType());
        Game correctAnswer = gamePool.get(randGame());
        question.setCorrectAnswer(correctAnswer);
        question.setCorrectAnswerCriterion(makeUpCriterionForQuestion(question));
        question.setWording(buildQuestionWording(question));
        question.getGames()[randOptions()] = correctAnswer;
        for (int i = 0; i < POSSIBLE_ANSWERS; i++) {
            Game game;
            int time_out = 0;
            do {
                game = gamePool.get(randGame());
                if (++time_out > MAX_LOOPS) {
                    Timber.w("Could not find wrong answers for this question, dropping (type=%s) (criterion=%s)",
                            question.getType(), question.getCorrectAnswerCriterion());
                    return null;
                }
            } while (isGameCorrectAnswerFor(game, question));
            if (question.getGames()[i] == null) {
                question.getGames()[i] = game;
            }
        }
        return question;
    }

    private String buildQuestionWording(Question question) {
        switch (question.getType()) {
            case Type.RELEASE_DATE:
                return buildReleaseDateWording(question.getCorrectAnswerCriterion());
            case Type.WAS_RELEASED_ON_PLATFORM:
                return buildReleasedOnPlatformWording(question.getCorrectAnswerCriterion());
            case Type.WAS_NEVER_RELEASED_ON_PLATFORM:
                return buildNotReleasedOnPlatformWording(question.getCorrectAnswerCriterion());
            default:
                throw new UnsupportedOperationException("Trying to build question wording without a type set");
        }
    }

    private String buildNotReleasedOnPlatformWording(long correctAnswerCriterion) {

        return String.format("Which of these games was never released on %s?", platformPool.get(correctAnswerCriterion).getName());

    }

    private String buildReleasedOnPlatformWording(long correctAnswerCriterion) {
        return String.format("Which of these games was released on %s?", platformPool.get(correctAnswerCriterion).getName());
    }

    private String buildReleaseDateWording(long correctAnswerCriterion) {
        return String.format("Which of these games was released in %s?", correctAnswerCriterion);
    }

    private long makeUpCriterionForQuestion(Question question) {
        switch (question.getType()) {
            case Type.RELEASE_DATE:
                Calendar c = Calendar.getInstance();
                c.setTime(question.getCorrectAnswer().getOriginalReleaseDate());
                return c.get(Calendar.YEAR);
            case Type.WAS_RELEASED_ON_PLATFORM:
                List<Platform> platforms = question.getCorrectAnswer().getPlatforms();
                return platforms.get(randomIntUnder(platforms.size())).getId();
            case Type.WAS_NEVER_RELEASED_ON_PLATFORM:
                return findPlatformTheGameWasNeverReleasedOn(question.getCorrectAnswer());
            default:
                throw new InvalidParameterException("Question needs to have a defined type");
        }
    }

    private long findPlatformTheGameWasNeverReleasedOn(Game game) {
        Platform platform;
        do {
            platform = (Platform) platformPool.values().toArray()[randomIntUnder(platformPool.size())];
            Timber.d("Trying to find a platform the game was not released on");
        } while (gameWasReleasedOnPlatformWithId(game, platform.getId()));
        return platform.getId();
    }

    public boolean isGameCorrectAnswerFor(Game game, Question question) {
        switch (question.getType()) {
            case Type.RELEASE_DATE:
                Calendar c = Calendar.getInstance();
                c.setTime(game.getOriginalReleaseDate());
                return c.get(Calendar.YEAR) == question.getCorrectAnswerCriterion();
            case Type.WAS_RELEASED_ON_PLATFORM:
                return gameWasReleasedOnPlatformWithId(game, question.getCorrectAnswerCriterion());
            case Type.WAS_NEVER_RELEASED_ON_PLATFORM:
                return !gameWasReleasedOnPlatformWithId(game, question.getCorrectAnswerCriterion());
            default:
                return false;
        }
    }

    private boolean gameWasReleasedOnPlatformWithId(Game game, long platformId) {
        boolean result = false;
        for (Platform platform : game.getPlatforms()) {
            if (platform.getId() == platformId) {
                result = true;
            }
        }
        return result;
    }

    public List<Question> getQuestions(int count) {
        return repository.findQuestions(count);
    }

}
