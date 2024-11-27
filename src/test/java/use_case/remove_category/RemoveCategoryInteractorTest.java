package use_case.remove_category;

import data_access.InMemoryUserDataAccessObject;

import entity.CommonUser;
import entity.User;
import org.junit.Test;
import use_case.add_category.*;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;


public class RemoveCategoryInteractorTest {
    @Test
    public void testRemoveCategory() {
        RemoveCategoryInputData inputData = new RemoveCategoryInputData("Horror");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), Collections.emptyMap());
        userRepository.save(user);
        userRepository.setCurrentUser("Paul");
        // add the desired category to remove
        userRepository.saveCategory("Horror");

        // This creates a successPresenter that test whether the test case is as we expect.
        RemoveCategoryOutputBoundary successPresenter = new RemoveCategoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RemoveCategoryOutputData outputData) {
                assertFalse(user.getCategories().contains(outputData.getCategory()));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected");
            }
        };

        RemoveCategoryInputBoundary interactor = new RemoveCategoryInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    // This case is not expected to arise in our program
    @Test
    public void failCategoryDoesNotExist() {
        RemoveCategoryInputData inputData = new RemoveCategoryInputData("Horror");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), Collections.emptyMap());
        userRepository.save(user);
        userRepository.setCurrentUser("Paul");
        // add some random category
        userRepository.saveCategory("Mystery");

        // This creates a successPresenter that test whether the test case is as we expect.
        RemoveCategoryOutputBoundary successPresenter = new RemoveCategoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RemoveCategoryOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Category does not exist.", errorMessage);
            }
        };

        RemoveCategoryInputBoundary interactor = new RemoveCategoryInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }
}
