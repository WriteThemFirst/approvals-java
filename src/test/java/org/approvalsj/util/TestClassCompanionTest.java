package org.approvalsj.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class TestClassCompanionTest {
    private TestClassCompanion testClassCompanion = new TestClassCompanion(getClass());

    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = testClassCompanion.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get("src/test/resources/org/approvalsj/util/TestClassCompanionTest/approvedFileShouldBeCorrect.approved");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }

    @Test
    void approvedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        testClassCompanion.writeApproved(content);

        //THEN
        String actualContent = testClassCompanion.readApproved();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        testClassCompanion.removeApproved();
    }

    @Test
    void receivedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        testClassCompanion.writeReceived(content);

        //THEN
        String actualContent = testClassCompanion.readReceived();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        testClassCompanion.removeReceived();
    }

    @Test
    void readApprovedShouldBeNullWhenFileMissing() {
        //GIVEN
        testClassCompanion.removeApproved();

        //WHEN
        String read = testClassCompanion.readApproved();

        //THEN
        assertThat(read).isNull();
    }
}