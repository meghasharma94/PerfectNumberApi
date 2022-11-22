package net.test.perfect.number.service;


import lombok.extern.slf4j.Slf4j;
import net.test.perfect.number.exception.InvalidRequestException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PerfectNumberService {

    public boolean checkForPerfectNumber(Integer number) {
        log.info("validating a number for its perfectness {}", number);
        return isNumberPerfect(number.intValue());
    }

    public List<Integer> listAllPerfectNumber(Integer start, Integer end) {
        if (end < start) {
            log.error("Invalid range passed end - {}  , start - {}", end, start);
            throw new InvalidRequestException("Correct the given range!!");
        }
        Optional<List<Integer>> perfectNumberList = calculatePerfectNumbersList(start, end);
        return perfectNumberList.isPresent() ? perfectNumberList.get() : Collections.emptyList();
    }

    private boolean isNumberPerfect(int number) {
        return number == IntStream.rangeClosed(2, (int) Math.sqrt(number)).
                filter(i -> number % i == 0).
                flatMap(i -> IntStream.of(i, number / i)).sum() + 1;

    }

    private Optional<List<Integer>> calculatePerfectNumbersList(int start, int end) {
        return Optional.ofNullable(IntStream.range(start, end)
                .filter(this::isNumberPerfect)
                .boxed()
                .collect(Collectors.toList()));
    }

}
