package br.com.dio.enums;

public enum EGameStatus {

    NON_STARTED("O jogo não foi iniciado"),
    INCOMPLETE("O jogo está incompleto"),
    COMPLETE("O jogo está completo");

    private final String label;

    EGameStatus(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}