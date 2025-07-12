package br.com.dio;

import br.com.dio.ui.custom.screen.MainScreen;
import br.com.dio.util.SudokuGenerator;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class UIMain {

    public static void main(String[] args) {
        final var gameConfig = new SudokuGenerator().generate();

        var mainsScreen = new MainScreen(gameConfig);
        mainsScreen.buildMainScreen();
    }

}