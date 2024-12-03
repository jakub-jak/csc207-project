# SmartNews: AI News Digest

## Contributors

Ali Elbadrawy - alihosam-dev

Jakub Jakuszewski - slendyduck

Chet Petro - chetpetro

Dimitar Toshev - toshoboss

Hriday Chhaochharia - hridaychh

## Table of Contents
- [Purpose](#purpose)
- [Software Features](#software-features)
- [Installation Instructions](#installation-instructions)
- [Usage Guide](#usage-guide)
- [Feedback](#feedback)
- [Contributions](#contributions)


## Purpose

- **What it does:**
    - Allows users to enter keywords and receive relevant news articles.
    - Generates concise summaries of these news articles with AI.
    - Users can save or share the summarized articles via email.

- **Why it was made:**
    - To address the challenge of information overload.
    - Helps users quickly stay updated on important news without sifting through long articles.

- **What problem it solves:**
    - Saves time by providing relevant, easy-to-digest summaries.
    - Makes it easier for users to stay informed and share news with others.

## Software Features

### Keyword-Based Article Summaries
- Enter keywords (e.g., "technology", "health", etc.) and press generate. The app will fetch relevant news articles from the web and generate a concise, easy-to-read summary of each article.

### Save Summaries for Later
- Users can save the generated summarized articles for later reading by pressing the save button. This feature is helpful for revisiting important articles or keeping a record of the summaries.

### Share Summaries via Email
- Users can share the generated summarized articles directly via email, making it easy to send key news to friends, family, or colleagues.


## Installation Instructions
- Clone the Project from GitHub to your local repository.
- Create a .env file in the project directory.
- Fill in the correct environment variables in the .env file corresponding to the .env.template file. (You can find temporary .env files [here](https://docs.google.com/document/d/1lAfy1HOj_1hAgsB34hC2Ef0vrUziLUotw0me-yYG4-A/edit?usp=sharing))
- Make sure all maven dependencies are installed if they have not been installed automatically.

## Usage Guide
- Navigate to src/main/java/app/MainApplication.java and run the file.
- A browser tab will open on your computer asking you to log in to a Gmail account. Log in with the following credentials:
  - email: newsbuddyapp@gmail.com
  - password: CSC207project
- Agree to the requested permissions displayed on the browser.
- The browser will then display that the user may continue back to the application. Open the application window.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Feedback
- Fill out this [Google Form](https://forms.gle/B58iNJ1ZAMXUJHsR7) to submit feedback the project maintainers.

## Contributions
- How to Contribute:
    - Fork this Repository (click <a href="https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/working-with-forks/fork-a-repo">here</a> for instructions on how to do this).
    - Clone your fork using git clone. Create a new branch, make your changes, and commit.
    - Push your changes to GitHub, and crate a pull request (PR)  (click <a href="https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork">here</a> for instructions on how to do this).
- Guidelines for Pull Requests:
    - Adhere to Clean Architecture and SOLID design principles.
    - Use the Checkstyle plugin to highlight all style issues and address them.
    - Provide a detailed description of what your PR accomplishes, what it has changed, and why it is valuable.
    - PR should only add one feature.
- Review Protocol:
    - A project maintainer will review your PR, comment on any necessary features, and then choose to either approve your PR, make changes to your PR, or deny your PR. 



