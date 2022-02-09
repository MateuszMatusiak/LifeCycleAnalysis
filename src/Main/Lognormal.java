package Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class Lognormal implements Initializable {

    @FXML
    private VBox table;

    @FXML
    private VBox charts;

    ArrayList<LognormalData> data = new ArrayList<>();

    private void computeRanks() {
        for (int i = 0; i < data.size(); ++i)
            data.get(i).rank = data.size() - i;
    }

    private void computeEstimateOfSurvival() {
        data.get(0).estimateOfSurvival = ((double) (data.get(0).rank) / (double) (data.get(0).rank + 1));
        for (int i = 1; i < data.size(); ++i) {
            data.get(i).estimateOfSurvival = (((data.get(i - 1).estimateOfSurvival) * (double) (data.get(i).rank)) / (double) (data.get(i).rank + (data.get(i - 1).estimateOfSurvival)));
        }
        for (LognormalData datum : data) {
            datum.estimateOfSurvival = 1.0 - datum.estimateOfSurvival;
        }

    }

    private void computePlottingPositions() {

        NormInv normInv = new NormInv();
        for (LognormalData datum : data) {
            datum.plottingPositions = normInv.compute(datum.estimateOfSurvival, 0, 1);
        }
    }

    private void computeLogTime() {
        for (LognormalData datum : data) {
            datum.logTime = Math.log(datum.timeToFailure);
        }
    }

    private void setTable() {
        for (LognormalData datum : data) {
            HBox entry = new HBox();

            Label l = new Label(datum.id);
            l.setPrefWidth(80);
            l.setMinWidth(80);
            l.setMaxWidth(80);
            entry.getChildren().add(l);

            l = new Label(datum.action);
            l.setPrefWidth(37);
            l.setMinWidth(37);
            l.setMaxWidth(37);
            entry.getChildren().add(l);

            l = new Label(datum.timeToFailure.toString());
            l.setPrefWidth(85);
            l.setMinWidth(85);
            l.setMaxWidth(85);
            entry.getChildren().add(l);

            l = new Label(datum.rank.toString());
            l.setPrefWidth(30);
            l.setMinWidth(30);
            l.setMaxWidth(30);
            entry.getChildren().add(l);

            l = new Label(datum.estimateOfSurvival.toString());
            l.setPrefWidth(130);
            l.setMinWidth(130);
            l.setMaxWidth(130);
            entry.getChildren().add(l);

            l = new Label(datum.plottingPositions.toString());
            l.setPrefWidth(110);
            l.setMinWidth(110);
            l.setMaxWidth(110);
            entry.getChildren().add(l);

            l = new Label(datum.logTime.toString());
            l.setMaxWidth(80);
            l.setMinWidth(80);
            l.setPrefWidth(80);
            entry.getChildren().add(l);

            entry.setSpacing(25);
            table.getChildren().add(entry);
        }
    }

    private void setLineChart() {
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Lognormal");
        for (LognormalData datum : data) {
            series1.getData().add(new XYChart.Data<>(datum.logTime, datum.plottingPositions));
        }
        final NumberAxis xAxis = new NumberAxis(8, 9.75, 0.5);
        xAxis.setLabel("Elapsed time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Probit");


        final LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
        sc.getData().addAll(series1);
        charts.getChildren().add(sc);


        double[] x = new double[data.size()];
        double[] y = new double[data.size()];
        for (int i = 0; i < data.size(); ++i) {
            x[i] = data.get(i).logTime;
            y[i] = data.get(i).plottingPositions;
        }

        double a = LeastSquaresLine.calculateA(x, y);
        double b = LeastSquaresLine.calculateB(x, y);
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Least Squares Line");
        series2.getData().add(new XYChart.Data<>(data.get(0).logTime, a * (data.get(0).logTime) + b));
        series2.getData().add(new XYChart.Data<>(data.get(data.size() - 1).logTime, a * (data.get(data.size() - 1).logTime) + b));

        sc.getData().addAll(series2);
        System.out.println(a+" x + " + b);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 0; i<Controller.data.size(); ++i)
        {
            data.add(new LognormalData(Controller.data.get(i)));
        }
        Collections.sort(data);
        computeRanks();
        computeEstimateOfSurvival();
        computePlottingPositions();
        computeLogTime();
        setTable();
        setLineChart();
    }


}
