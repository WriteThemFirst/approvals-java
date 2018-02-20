package org.approvalsj.util;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class TestClassCompanionTest {
    private TestClassCompanion companion = new TestClassCompanion(getClass());


    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = companion.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get(
                "src/test/resources/org/approvalsj/util/TestClassCompanionTest/approvedFileShouldBeCorrect.approved");
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
        final TestClassCompanion stringCompanion = new TestClassCompanion(String.class);
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
}