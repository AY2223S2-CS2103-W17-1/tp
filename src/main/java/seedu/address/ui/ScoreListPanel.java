package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.score.Score;

/**
 * Panel containing the list of scores.
 */

public class ScoreListPanel extends UiPart<Region> {
    private static final String FXML = "ScoreListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ScoreListPanel.class);

    @FXML
    private ListView<Score> scoreListView;
    @FXML
    private Label name;
    @FXML
    private Label nameChart;
    @FXML
    private LineChart scoreChart;
    @FXML
    private Axis xAxis;
    @FXML
    private Axis yAxis;

    /**
     * Creates a {@code ScoreListPanel} with the given {@code ObservableList}.
     */
    public ScoreListPanel(Person person) {
        super(FXML);

        name.setText("No student being checked now");
        nameChart.setText("No student being checked now");

        if (person != null) {
            scoreListView.setItems(person.getScoreList().getInternalList());
        }
        scoreListView.setCellFactory(listView -> new ScoreListPanel.ScoreListViewCell());

        if (person != null) {
            if (person.getScoreList().getInternalList().size() != 0) {
                name.setText("Score history for " + person.getName().fullName);
                nameChart.setText("Chart for " + person.getName().fullName);
                scoreChart.setVisible(true);
                xAxis.setLabel("Date");
                yAxis.setLabel("Score");
                XYChart.Series series = new XYChart.Series();
                for (int i = 0; i < person.getScoreList().getInternalList().size() && i < 5; i++) {
                    //String label = person.getScoreList().getInternalList().get(i).getLabel().toString();
                    String date = person.getScoreList().getInternalList().get(i).getDate().toString();
                    Double value = person.getScoreList().getInternalList().get(i).getValue().value;
                    series.getData().add(new XYChart.Data<>(date, value));
                }
                scoreChart.getData().add(series);
            } else {
                name.setText("No score history found for " + person.getName().fullName);
                nameChart.setText("No score chart for " + person.getName().fullName);
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Score} using a {@code ScoreCard}.
     */
    class ScoreListViewCell extends ListCell<Score> {
        @Override
        protected void updateItem(Score score, boolean empty) {
            super.updateItem(score, empty);

            if (empty || score == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ScoreCard(score, getIndex() + 1).getRoot());
            }
        }
    }
}
