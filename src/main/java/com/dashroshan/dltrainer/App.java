package com.dashroshan.dltrainer;

import java.io.IOException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    KeyListener keyListener;

    @Override
    public void start(Stage stage) throws IOException, NativeHookException {
        GlobalScreen.registerNativeHook();
        keyListener = new KeyListener();
        GlobalScreen.addNativeKeyListener(keyListener);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle("The Dark Legions - Trainer");
        stage.setScene(scene);
        stage.setResizable(false);
        Image favicon = new Image(App.class.getResource("icon.png").toExternalForm());
        stage.getIcons().add(favicon);
        stage.show();
    }

    @Override
    public void stop() {
        GlobalScreen.removeNativeKeyListener(keyListener);
    }

    public static void main(String[] args) {
        launch();
    }
}