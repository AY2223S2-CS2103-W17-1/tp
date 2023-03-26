package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.score.Score;
import seedu.address.model.score.ScoreList.ScoreSummary;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

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
    private LineChart<String, Double> scoreChart;
    @FXML
    private Axis xAxis;
    @FXML
    private Axis yAxis;

    @FXML
    private TableView<ScoreSummary> scoreStatistic;

    @FXML
    private TableColumn<ScoreSummary, Double> maxScore;
    @FXML
    private TableColumn<ScoreSummary, Double> minScore;
    @FXML
    private TableColumn<ScoreSummary, Double> average;
    @FXML
    private TableColumn<ScoreSummary, Double> percentage;

    /**
     * Creates a {@code ScoreListPanel} with the given {@code ObservableList}.
     */
    public ScoreListPanel(Person person) {
        super(FXML);

        name.setText("No student being checked now");
        nameChart.setText("No student being checked now");

        scoreListView.setCellFactory(listView -> new ScoreListPanel.ScoreListViewCell());

        if (person != null) {
            scoreListView.setItems(person.getSortedScoreList());
            if (person.getSortedScoreList().size() != 0) {
                newChart(person);
                statisticTable(person);
            } else {
                name.setText("No score history found for " + person.getName().fullName);
                nameChart.setText("No score chart for " + person.getName().fullName);
            }
        }


    }

    private void statisticTable(Person person) {
        scoreStatistic.setVisible(true);

        maxScore.setCellValueFactory(new PropertyValueFactory<>("maxScore"));
        minScore.setCellValueFactory(new PropertyValueFactory<>("minScore"));
        average.setCellValueFactory(new PropertyValueFactory<>("average"));
        percentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));

        maxScore.setSortable(false);
        minScore.setSortable(false);
        average.setSortable(false);
        percentage.setSortable(false);
        //scoreStatistic.getItems().addAll(person.getScoreSummary());
        scoreStatistic.setItems(person.getScoreSummary());
        /*
        maxScore.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScoreSummary, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<ScoreSummary, Double> param) {
                return param.getValue().maxProperty();
            }
        });
        */

        /*
        maxScore.setCellValueFactory(new PropertyValueFactory<>("maxScore"));
        minScore.setCellValueFactory(new PropertyValueFactory<>("minScore"));
        average.setCellValueFactory(new PropertyValueFactory<>("average"));
        percentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));

        scoreStatistic.setItems(person.getScoreSummary());

        TableColumn maxScore = new TableColumn("Maximum Score");
        TableColumn minScore = new TableColumn("Minimum Score");
        TableColumn averageScore = new TableColumn("Average Score");
        TableColumn percentage = new TableColumn("Percentage");
        */
        /*
        maxScore.setCellValueFactory(new PropertyValueFactory <>("max Score"));
        minScore.setCellValueFactory(new PropertyValueFactory <>("min score"));
        averageScore.setCellValueFactory(new PropertyValueFactory <>("max Score"));
        percentage.setCellValueFactory(new PropertyValueFactory <>("max Score"));
        */
        //scoreStatistic.getColumns().addAll(maxScore, minScore, averageScore, percentage);
    }

    private void newChart(Person person) {
        name.setText("Score history for " + person.getName().fullName);
        nameChart.setText("Recent 5 scores for " + person.getName().fullName);
        xAxis.setLabel("Date");
        yAxis.setLabel("Score");
        scoreChart.setVisible(true);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Region chartContent = (Region) scoreChart.lookup(".chart-content");
        for (Node node: chartContent.getChildrenUnmodifiable()) {
            if (node instanceof Group) {
                node.toFront();
                Group plotArea = (Group) node;
                plotArea.setClip(null);
            }
        }

        for (int i = 0; i < person.getRecentScoreList().size() && i < 5; i++) {
            String date = person.getRecentScoreList().get(i).getDate().toString();
            Double value = person.getRecentScoreList().get(i).getValue().value;
            String label = person.getRecentScoreList().get(i).getLabel().toString();
            XYChart.Data<String, Double> data = new XYChart.Data<>(date, value);
            data.setNode(new HoveredThresholdNode(data.getYValue(), label));
            series.getData().add(data);
         }

        scoreChart.getData().add(series);
    }

    private class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(Double scoreValue, String examLabel) {
            final Label label = createDataThresholdLabel(scoreValue, examLabel);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });

            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }

        private Label createDataThresholdLabel(Double scoreValue, String examLabel) {
            final Label label = new Label(examLabel + ": " + scoreValue);
            label.getStyleClass().addAll("chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: white; "
                    + "-fx-border-color: #605BF1; -fx-border-width: 2; ");

            if (scoreValue >= 80) {
                label.setTextFill(Color.rgb(126, 190, 97));
            } else if (50 <= scoreValue && scoreValue < 80) {
                label.setTextFill(Color.rgb(244, 181, 55));
            } else {
                label.setTextFill(Color.rgb(194, 47, 40));
            }

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
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
