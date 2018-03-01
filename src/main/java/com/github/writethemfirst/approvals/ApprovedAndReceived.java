package com.github.writethemfirst.approvals;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;

class ApprovedAndReceived {
    final Path approvedFile;
    final Path receivedFile;


    public ApprovedAndReceived(Path approvedFile, Path receivedFile) {
        this.approvedFile = approvedFile;
        this.receivedFile = receivedFile;
    }

    public boolean haveSameContent() {
        String receivedContent = silentRead(receivedFile);
        String approvedContent = silentRead(approvedFile);
        return receivedContent.equals(approvedContent);
    }

}
