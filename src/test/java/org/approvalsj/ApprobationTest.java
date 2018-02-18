package org.approvalsj;

import org.approvalsj.approbation.Approver;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ApprobationTest {
    private Approver approver = mock(Approver.class);
    private Approvals approvals = new Approvals(getClass(), approver);
    private FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvalsShouldCallApproverWhenMismatch() {
        fileUtils.writeApproved("some text");
        try {
            approvals.verify("diferent text");
        } catch (AssertionError e) {
            verify(approver).approve(fileUtils.approvedFile(), fileUtils.receivedFile());
            fileUtils.removeApproved();
            fileUtils.removeReceived();
        }
    }

}