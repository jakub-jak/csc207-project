package use_case.digest;

import java.io.IOException;

public interface DigestCohereDataAccessInterface {

    String summarize(String inputText) throws IOException;
}
