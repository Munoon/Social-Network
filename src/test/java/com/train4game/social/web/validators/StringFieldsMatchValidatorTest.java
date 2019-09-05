package com.train4game.social.web.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class StringFieldsMatchValidatorTest {
    private static Validator validator;
    private StringFieldsMatchValidatorTestData testData;

    @BeforeAll
    static void beforeAll() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        testData = new StringFieldsMatchValidatorTestData();
    }

    @Test
    void testValid() {
        testData.setFirst("Test");
        testData.setSecond("Test");
        Set<ConstraintViolation<StringFieldsMatchValidatorTestData>> violations = validator.validate(testData);
        assertThat(violations).hasSize(0);
    }

    @Test
    void testInValidStrings() {
        testData.setFirst("Test");
        testData.setSecond("Tes");
        Set<ConstraintViolation<StringFieldsMatchValidatorTestData>> violations = validator.validate(testData);
        assertThat(violations).hasSize(1);
        ConstraintViolation<StringFieldsMatchValidatorTestData> violation =
                violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Strings don't match");
    }
}