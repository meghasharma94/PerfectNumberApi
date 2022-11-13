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
        int sum = 1;
        if (number < 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0)
                sum += i + number / i;

            if (sum > number)
                return false;
        }
        return sum == number;
    }

    private Optional<List<Integer>> calculatePerfectNumbersList(int start, int end) {
        return Optional.ofNullable(IntStream.range(start, end)
                .filter(this::isNumberPerfect)
                .boxed()
                .collect(Collectors.toList()));
    }

}
