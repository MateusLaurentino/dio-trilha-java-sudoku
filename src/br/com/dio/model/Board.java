package br.com.dio.model;

import br.com.dio.enums.EGameStatus;

import java.util.Collection;
import java.util.List;

import static br.com.dio.enums.EGameStatus.COMPLETE;
import static br.com.dio.enums.EGameStatus.INCOMPLETE;
import static br.com.dio.enums.EGameStatus.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public record Board(List<List<Space>> spaces) {

    public EGameStatus getStatus() {
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))) {
            return NON_STARTED;
        }

        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors() {
        if (getStatus() == NON_STARTED) {
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public void reset() {
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus().equals(COMPLETE);
    }

}