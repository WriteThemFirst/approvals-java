package com.github.writethemfirst.approvals;

import java.util.stream.Stream;

import com.github.writethemfirst.approvals.ApprovalsFiles;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ApprovalsFilesTest {
    private ApprovalsFiles companion = new ApprovalsFiles(getClass());


    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = companion.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get(
                "src/test/resources/com/github/writethemfirst/approvals/ApprovalsFilesTest/approvedFileShouldBeCorrect.approved");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }


    @Test
    void approvedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        companion.writeApproved(content);

        //THEN
        String actualContent = companion.readApproved();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        companion.removeApproved();
    }


    @Test
    void receivedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        companion.writeReceived(content);

        //THEN
        String actualContent = companion.readReceived();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        companion.removeReceived();
    }


    @Test
    void readApprovedShouldBeEmptyWhenFileMissing() {
        //GIVEN
        companion.removeApproved();

        //WHEN
        String read = companion.readApproved();

        //THEN
        assertThat(read).isEqualTo("");
    }


    @Test
    void methodNameShouldBeEmpty() {
        final ApprovalsFiles stringCompanion = new ApprovalsFiles(String.class);
        assertThat(stringCompanion.methodName()).isEmpty();
    }


    @Test
    void methodNameShouldBeTheMethodName() {
        assertThat(companion.methodName()).contains("methodNameShouldBeTheMethodName");
    }


    @Test
    void methodNameShouldNotBeLambda() {
        Stream.of("whatever")
              .forEach(s -> assertThat(companion.methodName()).contains("methodNameShouldNotBeLambda"));
    }

    @Test
    void guessedClassShouldBeTestClass() {
        Class<?> guessed = Approvals.guessClass();
        assertThat(guessed).isEqualTo(getClass());
    }
}
