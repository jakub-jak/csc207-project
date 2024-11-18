package use_case.add_category;

import data_access.InMemoryUserDataAccessObject;

import entity.CommonUser;
import entity.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;


public class AddCategoryInteractorTest {

    @Test
    public void testAddCategory() {
        AddCategoryInputData inputData = new AddCategoryInputData("Paul", "Horror");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), Collections.emptyMap());
        userRepository.save(user);

        // This creates a successPresenter that test whether the test case is as we expect.
        AddCategoryOutputBoundary successPresenter = new AddCategoryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddCategoryOutputData outputData) {
                assertTrue(user.getCategories().contains(outputData.getCategory()));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(errorMessage);
            }
        };

        AddCategoryInputBoundary interactor = new AddCategoryInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureCategoryAlreadyExists() {
        AddCategoryInputData inputData = new AddCategoryInputData("Paul", "Horror");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), Collections.emptyMap());
        userRepository.save(user);
        // add the test category
        userRepository.saveAddedCategory("Paul", "Horror");

        // This creates a successPresenter that test whether the test case is as we expect.
        AddCategoryOutputBoundary successPresenter = new AddCategoryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddCategoryOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Category already exists.", errorMessage);
            }
        };

        AddCategoryInputBoundary interactor = new AddCategoryInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureEmptyCategory() {
        AddCategoryInputData inputData = new AddCategoryInputData("Paul", "");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), Collections.emptyMap());
        userRepository.save(user);

        // This creates a successPresenter that test whether the test case is as we expect.
        AddCategoryOutputBoundary successPresenter = new AddCategoryOutputBoundary() {
            @Override
            public void prepareSuccessView(AddCategoryOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Please enter a valid category.", errorMessage);
            }
        };

        AddCategoryInputBoundary interactor = new AddCategoryInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

}
