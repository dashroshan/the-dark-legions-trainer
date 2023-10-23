package com.dashroshan.dltrainer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Label wood;

    @FXML
    private Label stone;

    @FXML
    private Label gold;

    @FXML
    private Label troop;

    private DarkLegions darkLegions = new DarkLegions();

    private String format(int val) {
        return String.format("%d", val);
    }

    @FXML
    protected void refresh() {
        int[] values = darkLegions.read();
        wood.setText(format(values[0]));
        stone.setText(format(values[1]));
        gold.setText(format(values[2]));
        troop.setText(format(values[3]));
    }
}