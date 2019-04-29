/*
 * Approvals-Java - Approval testing library for Java. Alleviates the burden of hand-writing assertions.
 * Copyright © 2018 Write Them First!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.writethemfirst.approvals.testutils;

import com.github.writethemfirst.approvals.files.ApprovalFiles;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static java.nio.file.Paths.get;

public class SimpleTestUtils {
    public final Path received;
    public final Path approved;
    public final ApprovalFiles approvalFiles;

    public SimpleTestUtils(final String methodName, final Class<?> testClass) {
        this(
            methodName,
            get("src/test/resources/", testClass.getPackage().getName().split("\\.")).resolve(testClass.getSimpleName()).toString());
    }

    public SimpleTestUtils(final String methodName, final String testFolder) {
        final Path folderForClass = get(testFolder);
        approvalFiles = new ApprovalFiles(folderForClass, methodName);
        received = approvalFiles.received;
        approved = approvalFiles.approved;
    }

    public void writeReceived(final String content) {
        write(content, received);
    }

    public String readReceived() {
        return silentRead(received);
    }

    public void writeApproved(final String content) {
        write(content, approved);
    }

    public void cleanupPaths() {
        silentRemove(received);
        silentRemove(approved);
    }
}
